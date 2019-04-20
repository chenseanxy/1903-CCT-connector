import pipeline.Config;
import pipeline.jsonToKafka;

public class main {
    public static void main(String[] args) {
        Config config = new Config();
        jsonToKafka.jsonToKafka(config);
    }
}
