# T1-SMA — Simulador de redes de filas

Simulador de eventos discretos para redes de filas com topologia configurável
por meio de um arquivo YAML. O projeto foi desenvolvido em Java e utiliza a
biblioteca SnakeYAML, incluída no diretório `lib`.

## Pré-requisitos

* JDK instalado (Java 8 ou superior);
* terminal com acesso aos comandos `javac` e `java`;
* arquivo `lib/snakeyaml-2.2.jar`, já incluído neste repositório.

Para conferir a instalação do Java:

```text
java -version
javac -version
```

## Execução rápida

O ponto de entrada é `src.main.java.Main`. Por padrão, ele procura o arquivo
`model.yml` no diretório atual. Portanto, execute os comandos a partir da raiz
do projeto.

### Windows (PowerShell)

```powershell
Remove-Item -Recurse -Force build -ErrorAction SilentlyContinue
New-Item -ItemType Directory build | Out-Null

$sources = Get-ChildItem -Path src\main\java -Filter *.java -Recurse |
    ForEach-Object { $_.FullName }

javac -cp lib\snakeyaml-2.2.jar -d build $sources
java -cp "build;lib\snakeyaml-2.2.jar" src.main.java.Main
```

### Linux/macOS (Bash)

```bash
rm -rf build
mkdir build
javac -cp lib/snakeyaml-2.2.jar -d build \
  $(find src/main/java -name '*.java')
java -cp "build:lib/snakeyaml-2.2.jar" src.main.java.Main
```

O resultado é exibido na saída padrão. Para salvar o relatório em um arquivo,
redirecione a saída:

```powershell
java -cp "build;lib\snakeyaml-2.2.jar" src.main.java.Main `
    > resultado-simulacao.txt
```

No Linux/macOS, use `>` com o classpath separado por `:`.

## Arquivo de entrada (`model.yml`)

O arquivo YAML possui quatro seções principais e parâmetros de aleatoriedade:

```yaml
arrivals:
  Q1: 2.0

queues:
  Q1:
    servers: 1
    minArrival: 2.0
    maxArrival: 4.0
    minService: 1.0
    maxService: 2.0
  Q2:
    servers: 2
    capacity: 5
    minService: 4.0
    maxService: 6.0

network:
  - source: Q1
    target: Q2
    probability: 1.0

rndnumbersPerSeed: 100000
seeds:
  - 7
```

### Campos

* `arrivals`: mapa com as chegadas iniciais. A chave deve ser o nome de uma
  fila e o valor é o instante da primeira chegada.
* `queues`: definição das filas:
  - `servers` (obrigatório): quantidade de servidores;
  - `capacity` (opcional): capacidade máxima, incluindo clientes em serviço.
    Se omitido, a fila é considerada de capacidade ilimitada;
  - `minService` e `maxService` (obrigatórios): limites do tempo de serviço;
  - `minArrival` e `maxArrival` (opcionais, mas devem ser informados juntos):
    limites do intervalo entre chegadas. Esses campos devem existir nas filas
    que geram chegadas externas.
* `network`: lista de rotas entre filas. Cada rota contém `source`, `target`
  e `probability`. Para uma fila com várias saídas, as probabilidades devem
  representar as alternativas de roteamento e somar 1,0.
* `rndnumbersPerSeed`: quantidade de números pseudoaleatórios gerados para
  cada semente.
* `seeds`: lista de sementes. A implementação atual inicializa o gerador com
  a primeira semente da lista.

Os nomes usados em `arrivals`, `network.source` e `network.target` precisam
corresponder exatamente aos nomes declarados em `queues`.

## Modelo entregue para avaliação

O arquivo `model.yml` já contém o modelo solicitado para a simulação. Para
executá-lo, não é necessário alterar nenhum código: basta mantê-lo na raiz do
projeto e executar os comandos da seção **Execução rápida**.

O relatório apresenta:

* o tempo global final da simulação;
* cada fila e sua notação (`G/G/servidores` ou
  `G/G/servidores/capacidade`);
* o tempo e a probabilidade de permanência em cada estado da fila;
* o número de perdas por falta de capacidade.

Exemplo do início da saída:

```text
Tempo global final: ...
Queue:    Q1 (G/G/1)
...
Number of losses: ...
```

## Observações para reprodução

* A execução deve ser feita na raiz do projeto, pois o caminho de entrada é
  fixo como `model.yml` no código atual.
* A pasta `build` contém apenas os arquivos `.class` gerados pela compilação e
  pode ser removida e recriada a qualquer momento.
* Para testar outro modelo, substitua o conteúdo de `model.yml`, mantendo a
  estrutura descrita acima, e execute novamente o simulador.