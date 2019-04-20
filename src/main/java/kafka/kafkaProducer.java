package kafka;

import org.apache.kafka.clients.producer.*;
import pipeline.Config;

import java.util.Scanner;
import java.util.Properties;

public class kafkaProducer {
    private String server;
    private String topicName;
    private int msgID;
    private KafkaProducer<String,String> producerClient;

    public kafkaProducer(String server, String topicName, int offset){
        this.server = server;
        this.topicName = topicName;
        this.msgID = offset;
    }

    public kafkaProducer(String server, String topicName){
        this.server = server;
        this.topicName = topicName;
        this.msgID = 0;
    }

    public void init(){
        Properties prop = new Properties();
        prop.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        prop.put(ProducerConfig.ACKS_CONFIG, "1");
        prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer.class);
        prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer.class);
        prop.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, "io.confluent.monitoring.clients.interceptor.MonitoringProducerInterceptor");

        producerClient = new KafkaProducer<String, String>(prop);
    }

    public void close(){
        if(producerClient != null){
            producerClient.close();
        }
    }

    public void send(String record){
        ProducerRecord<String, String> producerRecord =
                new ProducerRecord<String, String>(topicName, Integer.toString(msgID), record);
        Callback sendComplete = new Callback() {
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                if(e != null){
                    e.printStackTrace();
                }
                System.out.println("Offset: " + recordMetadata.offset());
            }
        };
        producerClient.send(producerRecord, sendComplete);
        this.msgID++;
    }

    public static void main(String[] args) {
        Config config = new Config();
        Scanner sc = new Scanner(System.in);
        kafkaProducer producer = new kafkaProducer(
                config.getKafka_server(),
                config.getKafka_topic()
        );
        producer.init();
        while (true){
            String input = sc.nextLine();
            if(input == "0"){
                break;
            }
            System.out.println(input);
            producer.send(input);
        }
    }
}




