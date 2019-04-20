package pipeline;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Config {
    private int json_listen_port;
    private String kafka_server;
    private String kafka_topic;

    public Config(){
        json_listen_port = 5600;
        kafka_server = "kfk-cp-kafka-headless:9092";
        kafka_topic = "test-topic";
    }

    private static Gson gson = new Gson();
    public static Config fromJson(String config_location){
        FileReader config;
        try {
             config = new FileReader(config_location);
        }
        catch (FileNotFoundException e){
            System.out.println("Config file not found, using default values.");
            return new Config();
        }
        JsonReader jsonReader = new JsonReader(config);
        return gson.fromJson(jsonReader, Config.class);
    }

    public int getJson_listen_port() {
        return json_listen_port;
    }

    public String getKafka_server() {
        return kafka_server;
    }

    public String getKafka_topic() {
        return kafka_topic;
    }
}
