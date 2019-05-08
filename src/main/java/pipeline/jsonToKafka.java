package pipeline;

import kafka.kafkaProducer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class jsonToKafka {
    private static ServerSocket serverSocket;
    private static kafkaProducer producer;

    public static void run(Properties props) {

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                jsonToKafka.cleanup();
            }
        });

        producer = new kafkaProducer(props.getProperty("kafka-server"), props.getProperty("kafka-topic"));
        producer.init();

        InputStreamReader isr;
        BufferedReader br;
        Socket clientSocket;
        String data;
        int jsonPort = Integer.parseInt(props.getProperty("json-port"));

        try {
            serverSocket = new ServerSocket(jsonPort);
            while (true) {
                try {
                    clientSocket = serverSocket.accept();
                    isr = new InputStreamReader(clientSocket.getInputStream());
                    br = new BufferedReader(isr);

                    data = br.readLine();

                    if (data != null) {
                        System.out.println(data);
                        String finalData = data;
                        new Thread(() -> producer.send(finalData)).start();
                    }

                    clientSocket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void cleanup() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        producer.close();
    }

}
