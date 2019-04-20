package kafka;

import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;

import java.util.Properties;

public class kafkaStream {
    private String server;
    private String appID;
    private KStream<String, String> stream;

    public kafkaStream(String server, String appID){
        this.server = server;
        this.appID = appID;
    }

    public void init(){
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, appID);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, server);

    }
}
