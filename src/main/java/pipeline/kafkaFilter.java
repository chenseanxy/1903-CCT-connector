package pipeline;


import kafka.kafkaStream;

import java.util.Properties;

public class kafkaFilter {
    private static kafkaStream stream;

    public static void run(Properties props){
        Runtime.getRuntime().addShutdownHook(
                new Thread(stream::stop)
        );

        stream = new kafkaStream(
                props.getProperty("kafka-server"),
                props.getProperty("kafka-topic"),
                props.getProperty("kafka-topic-filtered"),
                "geofilter-stream" + props.getProperty("kafka-appid"),
                "geofilter-stream-client" + props.getProperty("kafka-clientid"),
                (k, v) -> Record.jsonValid(v)
        );
        stream.init();
        stream.run();
    }

}
