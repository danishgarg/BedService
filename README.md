# Bed Service (Event sourcing example)

## Pre-requisites

- JDK 1.8
- Kafka (Latest version)

## Steps:

- Start zookeeper and kafka broker
- Build the application by running `./gradlew clean build` from inside the cloned repository
- Run the application by running `java -jar build/libs/Bedservice-0.0.1-SNAPSHOT.jar`
- Navigate to the URL [localhost:8080/beds](http://localhost:8080/beds) and verify that it returns an empty collection
- Add a bed by running 
    ```bash
    curl -X POST -H "Content-Type: application/json" --data '{"id":"1234","location":"first floor general ward"}' "http://localhost:8080/create
    ```
- Navigate to the URL [localhost:8080/beds](http://localhost:8080/beds) and verify that it returns the bed that you just created with status **_Available_**
- Add a few more beds
- Book a bed by going to the kafka directory and executing 
    ```bash
    bin/kafka-console-producer.sh \
    --broker-list localhost:9092 \
    --topic bedRequestTopic \
    --property "parse.key=true" \
    --property "key.separator=:"
    ``` 
    and then entering `1234:{"aggregateId": "1234", "occupantId":"XYZ", "type":"bookBed"}` at the prompt
- Navigate to the URL [localhost:8080/beds](http://localhost:8080/beds) and verify that the status of the corresponding bed has changed to **_Booked_**.
- Verify that attempting to book the same bed again logs an error in the application console.

- *In case you want to see the events getting published to the kafka topic, execute the following command from the kafka directory*
    ```bash
    bin/kafka-console-consumer.sh --topic bedEvents --bootstrap-server localhost:9092 --property print.key=true --from-beginning
    ```