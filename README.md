# Votacao
Uma RESTful API de Votação utilizando Spring e Kafka

###INSTRUÇÕES DE USO:

####Iniciar Kafka
À partir do seu diretório Kafka, execute:
````
bin/zookeeper-server-start.sh config/zookeeper.properties

bin/kafka-server-start.sh config/server.properties

bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 3 --topic example
````
OBS.: Tanto a produção quanto o consumo dos dados já serão realizados por parte da aplicação, porém há a possibilidade de testá-los e validá-los através dos scripts abaixo:
```` 
bin/kafka-console-producer.sh --bootstrap-server localhost:9092 --topic example

bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic example --from-beginning
````

#### Iniciar Aplicação
À partir de um diretório vazio, execute:
````
git clone https://github.com/Arionildo/Votacao.git
cd Votacao
mvn package
cd target
java -jar votacao-0.2.0-SNAPSHOT.jar
````

#### Swagger

````
http://localhost:8080/swagger-ui.html#/
````

#### Versionamento
    - Os endpoints são versionados pelo número da versão (v1) diretamente na URL de acesso.
