package src.main.java.simulation;

import src.main.java.models.Evento;
import src.main.java.models.Fila;
import src.main.java.models.enums.TipoDeEvento;
import src.main.java.utils.AleatorioGlobal;
import src.main.java.utils.GlobalVars;

public class Simulador {
    public void executar() {
        while (GlobalVars.running && !GlobalVars.eventos.isEmpty()
                && AleatorioGlobal.aleatorio.isRunning) {
            Evento<String> evento = GlobalVars.eventos.poll();
            Fila fila = GlobalVars.queues.get(evento.getFila());
            if (evento.getTipo() == TipoDeEvento.CHEGADA) {
                fila.chegada(evento);
            } else if (evento.getTipo() == TipoDeEvento.PASSAGEM) {
                fila.passagem(evento);
            } else if (evento.getTipo() == TipoDeEvento.SAIDA) {
                fila.saida(evento);
            }
        }
    }
}
