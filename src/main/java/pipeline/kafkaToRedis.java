package pipeline;

import kafka.kafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import redis.redisClient;

import java.util.Properties;

public class kafkaToRedis {
    private static kafkaConsumer consumer;
    private static redisClient redis;

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

        redis = new redisClient(
                props.getProperty("redis-server"),
                Integer.parseInt(props.getProperty("redis-port")),
                props.getProperty("redis-key")
        );
        redis.init();

        while(true){
            records = consumer.poll();
            for(ConsumerRecord<String, String> record : records){
                System.out.println(record.value());
                redis.send(record.value());
            }
        }
    }

    public static void cleanup(){
        consumer.close();
    }
}
