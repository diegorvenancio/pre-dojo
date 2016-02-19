# Minhas Instruções


## Pre-requisitos

- Maven
- JDK 1.8

## instalação e execução

- No diretório do projeto insira: mvn package
- Para executar, vá para o diretório target e insira:  java -jar gamereviewer-1.0.jar <caminho para arquivo de log>

exemplo:  java -jar gamereviewer-1.0.jar test3.log

## Comentários

- Todas as funcionalidades foram implementadas.
- Para o requisito "Jogadores que matarem 5 vezes em 1 minuto" eu assumi que deveria dar 1 award para cada grupo de 5 mortes dentro de 1 minuto.
- Utilizei TDD e a cobertura de testes está em torno de 98%.
- Para o requisito "Identificar a maior sequência de assassinatos efetuadas por um jogador (streak) sem morrer" eu implementei como mais um award que é dado ao jogador que o obteve na partida. Indiquei no award quantas mortes estão contidas na sequência."

