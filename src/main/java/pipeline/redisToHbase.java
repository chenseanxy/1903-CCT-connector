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
                props.getProperty("redis-server"),
                Integer.parseInt(props.getProperty("redis-port")),
                props.getProperty("redis-key"),
                props.getProperty("redis-channel")
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
