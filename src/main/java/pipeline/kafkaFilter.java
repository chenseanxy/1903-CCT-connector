package pipeline;

import kafka.kafkaStream;

import java.util.Properties;

public class kafkaFilter {
    private static kafkaStream stream;

    public static void run(Properties props) {
        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> kafkaFilter.cleanup())
        );

        stream = new kafkaStream(
                props.getProperty("kafka-server"),
                props.getProperty("kafka-topic"),
                props.getProperty("kafka-topic-filtered"),
                "ccproject-kafkafilter-stream",
                (k, v) -> jsonValid(v)
        );

        System.out.println("Initializing stream");
        stream.init();
        System.out.println("Starting stream");
        stream.run();
    }

    public static void cleanup() {
        stream.stop();
    }

    public static Boolean jsonValid(String json) {
        try {
            Record r = Record.fromJson(json);
            Boolean valid = r.isValid();
            if (valid) {
                return true;
            } else {
                System.out.println("[INVALID]" + json);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[ERROR]" + json);
            return false;
        }
    }
}
