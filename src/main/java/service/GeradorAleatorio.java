package src.main.java.service;


public class GeradorAleatorio {

    private long numeroPrevio;
    private long a;
    private long c;
    private long m;
    private int numerosAleatoriosUsados;
    private int maxNumbers;
    
    public boolean isRunning; 

    public GeradorAleatorio(int maxNumbers, long seed) {
        this.maxNumbers = maxNumbers;
        this.numeroPrevio = seed;             
        this.a = 1103515245L;              
        this.c = 12345L;                    
        this.m = 2147483648L;               
        this.numerosAleatoriosUsados = 0;
        this.isRunning = true;
    }

    public double nextRandom() {
        if (this.numerosAleatoriosUsados >= this.maxNumbers) {
            System.out.println("PARANDO POR CHEGAR NO NÚMERO MÁXIMO DE ITERAÇÕES: " + this.maxNumbers);
            this.isRunning = false;
            return -1.0; 
        }
        
        // X = (a * X + c) % m
        this.numeroPrevio = (this.a * this.numeroPrevio + this.c) % this.m;
        this.numerosAleatoriosUsados++;
        
        return (double) this.numeroPrevio / this.m;
    }
}
