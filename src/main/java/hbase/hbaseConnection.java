package hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;

import java.io.IOException;

public class hbaseConnection {
    private String server;
    private Connection connection;
    private Configuration config;

    public hbaseConnection(String server) {
        this.server = server;
    }

    public void init() {
        config = new Configuration();
        config.set("hbase.zookeeper.quorum", server);
        try{
            connection = ConnectionFactory.createConnection(config);
        } catch (IOException e) {
            System.out.println("HBase connection init failed");
            e.printStackTrace();
        }
    }

    public Connection getConnection(){
        if(connection == null || connection.isClosed()){
            this.init();
        }
        return connection;
    }

    public Table getTableByName(String tableName){
        if(connection.isClosed()){
            try {
                connection = ConnectionFactory.createConnection(config);
            } catch (IOException e) {
                System.out.println("HBase connection failed");
                e.printStackTrace();
            }
        }

        try {
            return connection.getTable(TableName.valueOf(tableName));
        } catch (IOException e) {
            System.out.println("HBase connection failed");
            e.printStackTrace();
        }
        return null;
    }

}
