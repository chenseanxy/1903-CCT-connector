package kafka;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;

import java.util.*;

public class kafkaTopic {
    private String server;
    private String topic;

    public kafkaTopic(String server, String topic) {
        this.server = server;
        this.topic = topic;
    }

    public void makeTopic() {
        Properties prop = new Properties();
        prop.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, server);

        AdminClient admin = AdminClient.create(prop);
        ListTopicsResult topicsList = admin.listTopics();
        Set<String> names;
        try {
            names = topicsList.names().get();
        } catch (Exception e) {
            System.out.println("Failed to get topics list");
            return;
        }
        if (!names.contains(topic)) {
            System.out.println("Creating topic " + topic);
            List<NewTopic> newTopics = new ArrayList<>();
            Map<String, String> configs = new HashMap<>();
            int partitions = 5;
            Short replication = 1;
            NewTopic newTopic = new NewTopic(topic, partitions, replication).configs(configs);
            newTopics.add(newTopic);
            admin.createTopics(newTopics);
        }
    }
}
