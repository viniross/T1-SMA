package src.main.java.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import src.main.java.models.Evento;
import src.main.java.models.Fila;

public class GlobalVars {
    
    public static int numAleatoriosPorSeed = 0;
    public static int count = 0;
    
    public static List<Long> seeds = new ArrayList<>(); 
    
    public static PriorityQueue<Evento<String>> eventos = new PriorityQueue<>();
    
    public static boolean running = true;
    public static double tempo = 0.0;
    
    public static Map<String, Fila> queues = new HashMap<>();
}