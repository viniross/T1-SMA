package src.main.java;

import src.main.java.config.Configuracao;
import src.main.java.config.ConfiguracaoLoader;
import src.main.java.report.RelatorioSimulacao;
import src.main.java.simulation.ModeloSimulacao;
import src.main.java.simulation.Simulador;

public class Main {

    public static void main(String[] args) {
        try {
            Configuracao configuracao = new ConfiguracaoLoader().carregar("model.yml");
            new ModeloSimulacao().inicializar(configuracao);
            new Simulador().executar();
            new RelatorioSimulacao().imprimir();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}