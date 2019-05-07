package redis;

import redis.clients.jedis.Jedis;

public class redisPublisher {
    public String server;
    public int port;
    public String key;
    public String channel;
    private Jedis jedis;

    public redisPublisher(String server, int port, String key, String channel) {
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

    public void send(String data){
        System.out.println("[REDIS][P]Sending: " + data);
        jedis.publish(channel, data);
    }

}
