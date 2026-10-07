package src.main.java.config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

public class ConfiguracaoLoader {
    public Configuracao carregar(String caminho) throws Exception {
        try (InputStream inputStream = new FileInputStream(caminho)) {
            Map<String, Object> dados = new Yaml().load(inputStream);
            return converter(dados);
        }
    }

    private Configuracao converter(Map<String, Object> dados) {
        List<Long> seeds = new ArrayList<>();
        for (Object seed : lista(dados.get("seeds"), "seeds")) {
            seeds.add(((Number) seed).longValue());
        }

        Map<String, Configuracao.FilaConfiguracao> filas = new java.util.LinkedHashMap<>();
        Map<?, ?> dadosFilas = mapa(dados.get("queues"), "queues");
        for (Map.Entry<?, ?> entrada : dadosFilas.entrySet()) {
            String nomeFila = (String) entrada.getKey();
            Map<?, ?> fila = mapa(entrada.getValue(), "queue");
            boolean possuiChegada = fila.containsKey("minArrival") && fila.containsKey("maxArrival");
            filas.put(nomeFila, new Configuracao.FilaConfiguracao(
                    numero(fila, "servers").intValue(),
                    fila.containsKey("capacity") ? numero(fila, "capacity").intValue() : -1,
                    numero(fila, "minService").doubleValue(),
                    numero(fila, "maxService").doubleValue(),
                    possuiChegada ? numero(fila, "minArrival").doubleValue() : -1.0,
                    possuiChegada ? numero(fila, "maxArrival").doubleValue() : -1.0,
                    possuiChegada));
        }

        List<Configuracao.RotaConfiguracao> rotas = new ArrayList<>();
        Object valorRotas = dados.get("network");
        if (valorRotas != null) {
            for (Object valorRota : lista(valorRotas, "network")) {
                Map<?, ?> rota = mapa(valorRota, "network entry");
                rotas.add(new Configuracao.RotaConfiguracao(
                        (String) rota.get("source"), (String) rota.get("target"),
                        numero(rota, "probability").doubleValue()));
            }
        }

        Map<String, Number> chegadas = new java.util.LinkedHashMap<>();
        for (Map.Entry<?, ?> chegada : mapa(dados.get("arrivals"), "arrivals").entrySet()) {
            chegadas.put((String) chegada.getKey(), (Number) chegada.getValue());
        }
        return new Configuracao(seeds, numero(dados, "rndnumbersPerSeed").intValue(),
                filas, rotas, chegadas);
    }

    private Number numero(Map<?, ?> dados, String chave) {
        return (Number) dados.get(chave);
    }

    private Map<?, ?> mapa(Object valor, String nome) {
        if (!(valor instanceof Map<?, ?>)) {
            throw new IllegalArgumentException("Expected a map for " + nome);
        }
        return (Map<?, ?>) valor;
    }

    private List<?> lista(Object valor, String nome) {
        if (!(valor instanceof List<?>)) {
            throw new IllegalArgumentException("Expected a list for " + nome);
        }
        return (List<?>) valor;
    }
}
