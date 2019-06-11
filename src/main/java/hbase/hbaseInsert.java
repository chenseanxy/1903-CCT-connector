package hbase;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import pipeline.Record;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class hbaseInsert {
    private String tableName;
    private String familyName;
    private hbaseConnection hbConnection;

    public hbaseInsert(String tableName, String familyName, hbaseConnection hbConnection) {
        this.tableName = tableName;
        this.familyName = familyName;
        this.hbConnection = hbConnection;
    }

    public boolean insertRecordToHbase(List<Record> records){
        List<Put> puts = new ArrayList<>();
        for(Record record : records){
            Put put = new Put(Bytes.toBytes(record.getRowKey()));
            put.addColumn(Bytes.toBytes(familyName), Bytes.toBytes("address"), Bytes.toBytes(record.getAddress()));
            put.addColumn(Bytes.toBytes(familyName) , Bytes.toBytes("longitude") , Bytes.toBytes(record.getLongitude()+""));
            put.addColumn(Bytes.toBytes(familyName) , Bytes.toBytes("latitude") , Bytes.toBytes(record.getLatitude()+""));
            puts.add(put);
        }

        Table table = hbConnection.getTableByName(tableName);
        try {
            table.put(puts);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
