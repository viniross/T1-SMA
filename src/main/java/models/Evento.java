package src.main.java.models;

import src.main.java.models.enums.TipoDeEvento; 

public class Evento<T> implements Comparable<Evento<T>> {

    private TipoDeEvento tipo;
    private double tempo;
    private T fila; 

    public Evento(TipoDeEvento tipo, double tempo, T fila) {
        this.tipo = tipo;
        this.tempo = tempo;
        this.fila = fila;
    }

    public TipoDeEvento getTipo() {
        return tipo;
    }

    public void setTipo(TipoDeEvento tipo) {
        this.tipo = tipo;
    }

    public double getTempo() {
        return tempo;
    }

    public void setTempo(double tempo) {
        this.tempo = tempo;
    }

    public T getFila() {
        return fila;
    }

    public void setFila(T fila) {
        this.fila = fila;
    }

    @Override
    public int compareTo(Evento<T> outroEvento) {
        return Double.compare(this.tempo, outroEvento.tempo);
    }
}