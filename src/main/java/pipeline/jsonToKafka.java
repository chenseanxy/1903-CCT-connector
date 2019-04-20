package pipeline;

import kafka.kafkaProducer;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class jsonToKafka {

    public static void jsonToKafka(Config config){

        kafkaProducer producer = new kafkaProducer(
                config.getKafka_server(), config.getKafka_topic());
        producer.init();


        InputStreamReader isr;
        BufferedReader br;
        Socket clientSocket;
        String data;

        try {
            ServerSocket serverSocket=new ServerSocket(config.getJson_listen_port());
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
