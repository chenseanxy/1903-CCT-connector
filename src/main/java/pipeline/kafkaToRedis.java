package pipeline;

import kafka.kafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.util.Properties;

public class kafkaToRedis {
    private static kafkaConsumer consumer;

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

        while(true){
            records = consumer.poll();
            for(ConsumerRecord<String, String> record : records){
                System.out.println(record.toString());
            }
        }
    }

    public static void cleanup(){
        consumer.close();
    }
}
