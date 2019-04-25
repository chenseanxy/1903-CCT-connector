package pipeline;

import kafka.kafkaProducer;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;


public class jsonToKafka {

    public static void jsonToKafka(Properties props){

        kafkaProducer producer = new kafkaProducer(
                props.getProperty("kafka-server"), props.getProperty("kafka-topic"));
        producer.init();


        InputStreamReader isr;
        BufferedReader br;
        Socket clientSocket;
        String data;
        int jsonPort = Integer.parseInt(props.getProperty("json-port"));

        try {
            ServerSocket serverSocket=new ServerSocket(jsonPort);
            while(true){
                try{
                    clientSocket=serverSocket.accept();
                    isr=new InputStreamReader(clientSocket.getInputStream());
                    br=new BufferedReader(isr);

                    data = br.readLine();
                    System.out.println(data);

                    producer.send(data);
                    
                    clientSocket.close();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        producer.close();
    }

}
