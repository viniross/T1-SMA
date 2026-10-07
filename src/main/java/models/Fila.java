package src.main.java.models;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import src.main.java.models.enums.TipoDeEvento;
import src.main.java.utils.AleatorioGlobal;
import src.main.java.utils.GlobalVars;


public class Fila {
        private int servers;
        private int capacity;
        private double minService;
        private double maxService;
        private double minArrival;
        private double maxArrival;
        
        private double lastEventTime;
        private int customers;
        private int loss;
        
        private Map<Integer, Double> times;
        
        private List<Network> saidas;
    
        public Fila(int servers, int capacity, double minService, double maxService, double minArrival, double maxArrival) {
            this.servers = servers;
            this.capacity = capacity;
            this.minService = minService;
            this.maxService = maxService;
            this.minArrival = minArrival;
            this.maxArrival = maxArrival;
            
            this.lastEventTime = 0.0;
            this.customers = 0;
            this.loss = 0;
            this.times = new HashMap<>();
            this.saidas = new ArrayList<>();
        }
    
        public Fila(int servers, int capacity, double minService, double maxService) {
            this(servers, capacity, minService, maxService, -1.0, -1.0);
        }
        
        public void chegada(Evento<String> evento) {
            double tempoAcumulado = this.times.getOrDefault(this.customers, 0.0);
            this.times.put(this.customers, tempoAcumulado + (evento.getTempo() - this.lastEventTime));
            
            this.lastEventTime = evento.getTempo();
            GlobalVars.tempo = evento.getTempo(); 
            
            if (this.capacity == -1 || this.customers < this.capacity) {
                this.customers++;
                if (this.customers <= this.servers) {
                    double numeroRandom = AleatorioGlobal.aleatorio.nextRandom(); 
                    double tempo = evento.getTempo() + this.minService + (this.maxService - this.minService) * numeroRandom;
                    
                    if (!this.saidas.isEmpty()) {
                        GlobalVars.eventos.add(new Evento<>(TipoDeEvento.PASSAGEM, tempo, evento.getFila()));
                    } else {
                        GlobalVars.eventos.add(new Evento<>(TipoDeEvento.SAIDA, tempo, evento.getFila()));
                    }
                }
            } else {
                this.loss++;
            }
    
            if (evento.getTipo() == TipoDeEvento.CHEGADA) {
                if (this.minArrival >= 0) {
                    double numeroRandom = AleatorioGlobal.aleatorio.nextRandom();
                    double tempo = evento.getTempo() + this.minArrival + (this.maxArrival - this.minArrival) * numeroRandom;
                    GlobalVars.eventos.add(new Evento<>(TipoDeEvento.CHEGADA, tempo, evento.getFila()));
                } else {
                    System.out.println("ERRO: CHEGADA INDEVIDA");
                }
            }
        }
    
        public void passagem(Evento<String> evento) {
            this.saida(evento);
            
            double aleatorio = AleatorioGlobal.aleatorio.nextRandom();
            double soma = 0.0;
            
            for (Network fila : this.saidas) {
                soma += fila.getProbability();
                if (soma >= aleatorio) {
                    evento.setFila(fila.getTarget());
                    Fila nextQueue = GlobalVars.queues.get(evento.getFila());
                    if (nextQueue != null) {
                        nextQueue.chegada(evento);
                    }
                    return;
                }
            }
        }
    
        public void saida(Evento<String> evento) {
            double tempoAcumulado = this.times.getOrDefault(this.customers, 0.0);
            this.times.put(this.customers, tempoAcumulado + (evento.getTempo() - this.lastEventTime));
            
            this.lastEventTime = evento.getTempo();
            GlobalVars.tempo = evento.getTempo();
            
            this.customers--;
            
            if (this.customers >= this.servers) {
                double numeroRandom = AleatorioGlobal.aleatorio.nextRandom();
                double tempo = evento.getTempo() + this.minService + (this.maxService - this.minService) * numeroRandom;
                
                if (!this.saidas.isEmpty()) {
                    GlobalVars.eventos.add(new Evento<>(TipoDeEvento.PASSAGEM, tempo, evento.getFila()));
                } else {
                    GlobalVars.eventos.add(new Evento<>(TipoDeEvento.SAIDA, tempo, evento.getFila()));
                }
            }
        }
    
        public List<Network> getSaidas() {
            return saidas;
        }

        public Map<Integer, Double> getTimes() {
            return times;
        }

        public int getCustomers() {
            return customers;
        }

        public double getLastEventTime() {
            return lastEventTime;
        }

        public int getLoss() {
            return loss;
        }

        public int getServers() { 
            return servers; 
        }
        
        public int getCapacity() { 
            return capacity; 
        }

        public double getMinArrival() { 
            return minArrival; 
        }

        public double getMaxArrival() { 
            return maxArrival; 
        }

        public double getMinService() { 
            return minService; 
        }

        public double getMaxService() { 
            return maxService; 
        }
    }



