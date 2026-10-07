package src.main.java.config;

import java.util.List;
import java.util.Map;

public class Configuracao {
    private final List<Long> seeds;
    private final int numerosAleatoriosPorSeed;
    private final Map<String, FilaConfiguracao> filas;
    private final List<RotaConfiguracao> rotas;
    private final Map<String, Number> chegadas;

    public Configuracao(List<Long> seeds, int numerosAleatoriosPorSeed,
            Map<String, FilaConfiguracao> filas, List<RotaConfiguracao> rotas,
            Map<String, Number> chegadas) {
        this.seeds = seeds;
        this.numerosAleatoriosPorSeed = numerosAleatoriosPorSeed;
        this.filas = filas;
        this.rotas = rotas;
        this.chegadas = chegadas;
    }

    public List<Long> getSeeds() { return seeds; }
    public int getNumerosAleatoriosPorSeed() { return numerosAleatoriosPorSeed; }
    public Map<String, FilaConfiguracao> getFilas() { return filas; }
    public List<RotaConfiguracao> getRotas() { return rotas; }
    public Map<String, Number> getChegadas() { return chegadas; }

    public static class FilaConfiguracao {
        private final int servidores;
        private final int capacidade;
        private final double minimoServico;
        private final double maximoServico;
        private final double minimoChegada;
        private final double maximoChegada;
        private final boolean possuiChegada;

        public FilaConfiguracao(int servidores, int capacidade, double minimoServico,
                double maximoServico, double minimoChegada, double maximoChegada,
                boolean possuiChegada) {
            this.servidores = servidores;
            this.capacidade = capacidade;
            this.minimoServico = minimoServico;
            this.maximoServico = maximoServico;
            this.minimoChegada = minimoChegada;
            this.maximoChegada = maximoChegada;
            this.possuiChegada = possuiChegada;
        }

        public int getServidores() { return servidores; }
        public int getCapacidade() { return capacidade; }
        public double getMinimoServico() { return minimoServico; }
        public double getMaximoServico() { return maximoServico; }
        public double getMinimoChegada() { return minimoChegada; }
        public double getMaximoChegada() { return maximoChegada; }
        public boolean possuiChegada() { return possuiChegada; }
    }

    public static class RotaConfiguracao {
        private final String origem;
        private final String destino;
        private final double probabilidade;

        public RotaConfiguracao(String origem, String destino, double probabilidade) {
            this.origem = origem;
            this.destino = destino;
            this.probabilidade = probabilidade;
        }

        public String getOrigem() { return origem; }
        public String getDestino() { return destino; }
        public double getProbabilidade() { return probabilidade; }
    }
}
