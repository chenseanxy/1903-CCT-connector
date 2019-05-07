package pipeline;

import redis.clients.jedis.JedisPubSub;
import redis.redisSubscriber;

import java.util.Properties;

public class redisToHbase {
    private static redisSubscriber subscriber;

    public static void run(Properties props){
        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> redisToHbase.cleanup())
        );

        subscriber = new redisSubscriber(
                props.getProperty("publisher-server"),
                Integer.parseInt(props.getProperty("publisher-port")),
                props.getProperty("publisher-key"),
                props.getProperty("publisher-channel")
        );
        subscriber.init();

        JedisPubSub listener = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                System.out.println("[REDIS][S]Received: "+message);
            }
        };

        subscriber.subscribe(listener);
    }

    public static void cleanup(){
        subscriber.unsubscribe();
    }
}
