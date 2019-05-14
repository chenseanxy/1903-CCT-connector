package kafka;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Predicate;

import java.util.Properties;

public class kafkaStream {
    private String server;
    private String topic;
    private String filteredTopic;
    private String appID;
    private KStream<String, String> sourceStream;
    private KStream<String, String> destStream;
    private Predicate<String, String> predicate;
    private KafkaStreams streams;

    public void init() {
        // Stream Pipeline
        StreamsBuilder builder = new StreamsBuilder();
        sourceStream = builder.stream(topic);
        destStream = sourceStream.filter(predicate);
        destStream.to(filteredTopic);

        // Props
        Properties props = new Properties();
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, appID);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        streams = new KafkaStreams(builder.build(), props);

        kafkaTopic kTopicFiltered = new kafkaTopic(server, filteredTopic);
        kTopicFiltered.makeTopic();

        streams.cleanUp();
    }

    public void run() {
        streams.start();
    }

    public void stop() {
        streams.close();
    }

    public kafkaStream(String server, String topic, String filteredTopic, String appID,
            Predicate<String, String> predicate) {
        this.server = server;
        this.topic = topic;
        this.filteredTopic = filteredTopic;
        this.appID = appID;
        this.predicate = predicate;
    }
}
