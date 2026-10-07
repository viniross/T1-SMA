package src.main.java;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

import src.main.java.models.Evento;
import src.main.java.models.Fila;
import src.main.java.models.Network;
import src.main.java.models.enums.TipoDeEvento;
import src.main.java.utils.AleatorioGlobal;
import src.main.java.utils.GlobalVars;

public class Main {

    public static void main(String[] args) {
        
        try {
            Yaml yaml = new Yaml();
            InputStream inputStream = new FileInputStream("model.yml");
            Map<String, Object> dados = yaml.load(inputStream);

            List<Integer> seedsYaml = (List<Integer>) dados.get("seeds");
            for (Integer s : seedsYaml) {
                GlobalVars.seeds.add(s.longValue());
            }
            
            GlobalVars.numAleatoriosPorSeed = (Integer) dados.get("rndnumbersPerSeed");
            GlobalVars.count = GlobalVars.numAleatoriosPorSeed * GlobalVars.seeds.size();
            
            AleatorioGlobal.inicializar();

            Map<String, Map<String, Object>> queuesData = (Map<String, Map<String, Object>>) dados.get("queues");
            for (Map.Entry<String, Map<String, Object>> entry : queuesData.entrySet()) {
                String queueKey = entry.getKey();
                Map<String, Object> queueVal = entry.getValue();

                int servers = ((Number) queueVal.get("servers")).intValue();
                double minService = ((Number) queueVal.get("minService")).doubleValue();
                double maxService = ((Number) queueVal.get("maxService")).doubleValue();
                
                int capacity = queueVal.containsKey("capacity") ? ((Number) queueVal.get("capacity")).intValue() : -1;

                Fila queue;
                if (queueVal.containsKey("minArrival") && queueVal.containsKey("maxArrival")) {
                    double minArrival = ((Number) queueVal.get("minArrival")).doubleValue();
                    double maxArrival = ((Number) queueVal.get("maxArrival")).doubleValue();
                    queue = new Fila(servers, capacity, minService, maxService, minArrival, maxArrival);
                } else {
                    queue = new Fila(servers, capacity, minService, maxService);
                }
                GlobalVars.queues.put(queueKey, queue);
            }

            List<Map<String, Object>> networkData = (List<Map<String, Object>>) dados.get("network");
            if (networkData != null) {
                for (Map<String, Object> net : networkData) {
                    String srcName = (String) net.get("source");
                    String targetName = (String) net.get("target");
                    double probability = ((Number) net.get("probability")).doubleValue();

                    Fila srcQueue = GlobalVars.queues.get(srcName);
                    Network saida = new Network(targetName, probability);
                    srcQueue.getSaidas().add(saida);
                }
            }

            Map<String, Number> arrivalsData = (Map<String, Number>) dados.get("arrivals");
            for (Map.Entry<String, Number> entry : arrivalsData.entrySet()) {
                String queueName = entry.getKey();
                double arrivalTime = entry.getValue().doubleValue();
                GlobalVars.eventos.add(new Evento<>(TipoDeEvento.CHEGADA, arrivalTime, queueName));
            }

            GlobalVars.running = true;
            
            while (GlobalVars.running && !GlobalVars.eventos.isEmpty() && AleatorioGlobal.aleatorio.isRunning) {
                Evento<String> evento = GlobalVars.eventos.poll();
                
                Fila fila = GlobalVars.queues.get(evento.getFila());
                
                switch (evento.getTipo()) {
                    case CHEGADA:
                        fila.chegada(evento);
                        break;
                    case PASSAGEM:
                        fila.passagem(evento);
                        break;
                    case SAIDA:
                        fila.saida(evento);
                        break;
                }
            }

           System.out.println("Tempo global final: " + GlobalVars.tempo);
            System.out.println();

            for (Map.Entry<String, Fila> entry : GlobalVars.queues.entrySet()) {
                String nomeFila = entry.getKey();
                Fila queue = entry.getValue();

                double tempoAcumuladoAtual = queue.getTimes().getOrDefault(queue.getCustomers(), 0.0);
                queue.getTimes().put(queue.getCustomers(), tempoAcumuladoAtual + (GlobalVars.tempo - queue.getLastEventTime()));

                System.out.println("--------------------------------------------------");
                
                String tipoFila = queue.getCapacity() == -1 ? 
                    String.format("(G/G/%d)", queue.getServers()) : 
                    String.format("(G/G/%d/%d)", queue.getServers(), queue.getCapacity());
                
                System.out.printf("Queue:    %s %s\n", nomeFila, tipoFila); 
                
                if (queue.getMinArrival() > 0 || queue.getMaxArrival() > 0) {
                    System.out.printf("Arrival:  %.1f ... %.1f\n", queue.getMinArrival(), queue.getMaxArrival()); 
                }
                
                System.out.printf("Service:  %.1f ... %.1f\n", queue.getMinService(), queue.getMaxService()); 
                System.out.println("--------------------------------------------------");
                
                System.out.printf("%9s %21s %21s\n", "State", "Time", "Probability");
                
                java.util.List<Integer> estadosOrdenados = new java.util.ArrayList<>(queue.getTimes().keySet());
                java.util.Collections.sort(estadosOrdenados);
                
                for (int numClientes : estadosOrdenados) {
                    double tempoOcupado = queue.getTimes().get(numClientes);
                    double porcentagem = 100 * tempoOcupado / GlobalVars.tempo;
                    
                    System.out.printf("%9d %21.4f %20.2f%%\n", numClientes, tempoOcupado, porcentagem);
                }
                
                System.out.println();
                System.out.printf("Number of losses: %d\n\n", queue.getLoss()); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}