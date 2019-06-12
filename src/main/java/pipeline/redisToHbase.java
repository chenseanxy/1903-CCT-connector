package pipeline;

import hbase.hbaseConnection;
import hbase.hbaseInsert;
import redis.clients.jedis.JedisPubSub;
import redis.redisSubscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class redisToHbase {
    private static redisSubscriber subscriber;
    private static hbaseConnection hbConnection;
    private static hbaseInsert hbInsert;

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

        hbConnection = new hbaseConnection(
                props.getProperty("hbase-server")
        );
        hbConnection.init();

        hbInsert = new hbaseInsert(
                props.getProperty("hbase-table"),
                props.getProperty("hbase-family"),
                hbConnection
        );

        JedisPubSub listener = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                try{
                    insert(message);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        };

        subscriber.subscribe(listener);
    }

    public static void insert(String message){
        Record r = Record.fromJson(message);
        List<Record> records = new ArrayList<>();
        records.add(r);
        hbInsert.insertRecordToHbase(records);
    }

    public static void cleanup(){
        subscriber.unsubscribe();
    }
}
