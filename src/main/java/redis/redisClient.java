package redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

public class redisClient {
    public String server;
    public int port;
    public String key;
    private Jedis jedis;
    private Pipeline pipeline;

    public redisClient(String server, int port, String key) {
        this.server = server;
        this.port = port;
        this.key = key;
    }

    public void init(){
        jedis = new Jedis(server, port);
        pipeline = jedis.pipelined();
    }

    public void send(String data){
        pipeline.sadd(key, data);
    }
}
