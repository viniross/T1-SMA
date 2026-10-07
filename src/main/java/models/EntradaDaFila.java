package src.main.java.models;

import java.util.Queue;

public class EntradaDaFila<T> {
    
    private Queue<T> queue;
    private int minArrival;
    private int maxArrival;

    public EntradaDaFila(Queue<T> queue, int minArrival, int maxArrival) {
        this.queue = queue;
        this.minArrival = minArrival;
        this.maxArrival = maxArrival;
    }

    public Queue<T> getQueue() {
        return queue;
    }

    public void setQueue(Queue<T> queue) {
        this.queue = queue;
    }

    public int getMinArrival() {
        return minArrival;
    }

    public void setMinArrival(int minArrival) {
        this.minArrival = minArrival;
    }

    public int getMaxArrival() {
        return maxArrival;
    }

    public void setMaxArrival(int maxArrival) {
        this.maxArrival = maxArrival;
    }
}