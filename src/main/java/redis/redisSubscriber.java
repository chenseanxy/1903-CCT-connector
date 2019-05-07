package redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class redisSubscriber {
    public String server;
    public int port;
    public String key;
    public String channel;
    private Jedis jedis;
    private JedisPubSub listener;

    public redisSubscriber(String server, int port, String key, String channel) {
        this.server = server;
        this.port = port;
        this.key = key;
        this.channel = channel;
    }

    public void init(){
        jedis = new Jedis(server, port);
        String authResult = jedis.auth(key);
        if(!authResult.equals("OK")){
            // TODO: AUTH ERR Handling
            System.out.println("[REDIS]Auth Error: "+authResult);
        }
    }

    public void subscribe(JedisPubSub listener){
        this.listener = listener;
        System.out.println("[REDIS][S]Subscribing to channel "+ channel);
        jedis.subscribe(listener, channel);
    }

    public void unsubscribe(){
        listener.unsubscribe();
        System.out.println("[REDIS][S]Unsubscribing");
    }

}
