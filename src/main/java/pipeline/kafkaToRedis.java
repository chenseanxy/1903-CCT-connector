package pipeline;

import kafka.kafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import redis.redisPublisher;

import java.util.Properties;

public class kafkaToRedis {
    private static kafkaConsumer consumer;
    private static redisPublisher publisher;

    public static void run(Properties props){

        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> kafkaToRedis.cleanup())
        );

        consumer = new kafkaConsumer(
                props.getProperty("kafka-server"),
                props.getProperty("kafka-groupid"),
                props.getProperty("kafka-topic")
        );
        consumer.init();

        ConsumerRecords<String, String> records;

        publisher = new redisPublisher(
                props.getProperty("publisher-server"),
                Integer.parseInt(props.getProperty("publisher-port")),
                props.getProperty("publisher-key"),
                props.getProperty("publisher-channel")
        );
        publisher.init();

        while(true){
            records = consumer.poll();
            for(ConsumerRecord<String, String> record : records){
                System.out.println("[KFK][C]Received: "+record.value());
                publisher.send(record.value());
            }
        }
    }

    public static void cleanup(){
        consumer.close();
    }
}
