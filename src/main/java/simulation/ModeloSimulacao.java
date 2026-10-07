package src.main.java.simulation;

import java.util.Map;
import src.main.java.config.Configuracao;
import src.main.java.models.Evento;
import src.main.java.models.Fila;
import src.main.java.models.Network;
import src.main.java.models.enums.TipoDeEvento;
import src.main.java.utils.AleatorioGlobal;
import src.main.java.utils.GlobalVars;

public class ModeloSimulacao {
    public void inicializar(Configuracao configuracao) {
        GlobalVars.seeds.addAll(configuracao.getSeeds());
        GlobalVars.numAleatoriosPorSeed = configuracao.getNumerosAleatoriosPorSeed();
        GlobalVars.count = GlobalVars.numAleatoriosPorSeed * GlobalVars.seeds.size();
        AleatorioGlobal.inicializar();

        for (Map.Entry<String, Configuracao.FilaConfiguracao> entrada : configuracao.getFilas().entrySet()) {
            Configuracao.FilaConfiguracao dados = entrada.getValue();
            Fila fila = dados.possuiChegada()
                    ? new Fila(dados.getServidores(), dados.getCapacidade(), dados.getMinimoServico(),
                            dados.getMaximoServico(), dados.getMinimoChegada(), dados.getMaximoChegada())
                    : new Fila(dados.getServidores(), dados.getCapacidade(), dados.getMinimoServico(),
                            dados.getMaximoServico());
            GlobalVars.queues.put(entrada.getKey(), fila);
        }

        for (Configuracao.RotaConfiguracao rota : configuracao.getRotas()) {
            Fila origem = GlobalVars.queues.get(rota.getOrigem());
            origem.getSaidas().add(new Network(rota.getDestino(), rota.getProbabilidade()));
        }

        for (Map.Entry<String, Number> chegada : configuracao.getChegadas().entrySet()) {
            GlobalVars.eventos.add(new Evento<>(TipoDeEvento.CHEGADA,
                    chegada.getValue().doubleValue(), chegada.getKey()));
        }
        GlobalVars.running = true;
    }
}
