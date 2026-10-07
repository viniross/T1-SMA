package src.main.java.utils;

import src.main.java.service.GeradorAleatorio;

public class AleatorioGlobal {
    
    public static GeradorAleatorio aleatorio;

    public static void inicializar() {
        aleatorio = new GeradorAleatorio(GlobalVars.count, GlobalVars.seeds.get(0));
    }
}