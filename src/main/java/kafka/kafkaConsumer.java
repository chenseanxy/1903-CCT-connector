package kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class kafkaConsumer {
    private String server;
    private String groupID;
    private String topicName;
    private KafkaConsumer<String, String> consumerClient;

    public kafkaConsumer(String server, String groupID, String topicName) {
        this.server = server;
        this.groupID = groupID;
        this.topicName = topicName;
    }

    public void init() {
        Properties prop = new Properties();
        prop.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        prop.put(ConsumerConfig.GROUP_ID_CONFIG, groupID);
        prop.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        prop.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        prop.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                org.apache.kafka.common.serialization.StringDeserializer.class);
        prop.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                org.apache.kafka.common.serialization.StringDeserializer.class);
        prop.put(ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG,
                io.confluent.monitoring.clients.interceptor.MonitoringConsumerInterceptor.class);

        consumerClient = new KafkaConsumer<String, String>(prop);
        consumerClient.subscribe(Arrays.asList(topicName));
    }

    public ConsumerRecords<String, String> poll() {
        return consumerClient.poll(Duration.ofSeconds(100));
    }

    public void close(){
        consumerClient.close();
    }

}
