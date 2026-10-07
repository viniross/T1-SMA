package src.main.java.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import src.main.java.models.Fila;
import src.main.java.utils.GlobalVars;

public class RelatorioSimulacao {
    public void imprimir() {
        System.out.println("Tempo global final: " + GlobalVars.tempo);
        System.out.println();
        for (Map.Entry<String, Fila> entrada : GlobalVars.queues.entrySet()) {
            imprimirFila(entrada.getKey(), entrada.getValue());
        }
    }

    private void imprimirFila(String nome, Fila fila) {
        double tempoAtual = fila.getTimes().getOrDefault(fila.getCustomers(), 0.0);
        fila.getTimes().put(fila.getCustomers(),
                tempoAtual + (GlobalVars.tempo - fila.getLastEventTime()));

        System.out.println("--------------------------------------------------");
        String tipo = fila.getCapacity() == -1
                ? String.format("(G/G/%d)", fila.getServers())
                : String.format("(G/G/%d/%d)", fila.getServers(), fila.getCapacity());
        System.out.printf("Queue:    %s %s\n", nome, tipo);
        if (fila.getMinArrival() > 0 || fila.getMaxArrival() > 0) {
            System.out.printf("Arrival:  %.1f ... %.1f\n", fila.getMinArrival(), fila.getMaxArrival());
        }
        System.out.printf("Service:  %.1f ... %.1f\n", fila.getMinService(), fila.getMaxService());
        System.out.println("--------------------------------------------------");
        System.out.printf("%9s %21s %21s\n", "State", "Time", "Probability");

        ArrayList<Integer> estados = new ArrayList<>(fila.getTimes().keySet());
        Collections.sort(estados);
        for (int clientes : estados) {
            double tempo = fila.getTimes().get(clientes);
            System.out.printf("%9d %21.4f %20.2f%%\n", clientes, tempo,
                    100 * tempo / GlobalVars.tempo);
        }
        System.out.println();
        System.out.printf("Number of losses: %d\n\n", fila.getLoss());
    }
}
