import org.apache.commons.cli.*;

import pipeline.*;

import java.util.Properties;

public class main {

    public static String mode;
    public static Boolean debug = true;

    public static void main(String[] args) {
        // Options DEFINITION PHASE
        Options options = new Options();
        options.addRequiredOption("m", "mode", true, "Choose the mode of operation");
        options.addOption("v","debug", true, "Set debug flag");

        // json
        options.addOption("jp", "json-port", true, "Specify listening port for json server");

        // kafka
        options.addOption("ks", "kafka-server", true, "Specify kafka server");
        options.addOption("kt", "kafka-topic", true, "Specify kafka topic to use");
        options.addOption("ktf","kafka-topic-filtered", true, "Specify the filtered topic name");
        options.addOption("kaid","kafka-appid", true, "AppID for Kafka Stream");
        options.addOption("kcid","kafka-clientid",true, "clientID for Kafka Stream and Consumer");


        // redis
        // hadoop

        // Options Parsing phase
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println("Failed to parse input arguments");
            System.out.println(e.getMessage());
            pause();
            System.exit(1);
            return;
        }

        // Options INTERROGATION PHASE
        Properties props = new Properties();

        // mode
        mode = cmd.getOptionValue("m");
        System.out.println("Mode: \"" + mode + "\"");

        // debug
        if(cmd.hasOption("v") && cmd.getOptionValue("v").equals("true")){
            debug = true;
        } else {
            debug = false;
        }
        System.out.println("Debug: "+debug.toString());

        // json-kafka
        if (mode.equals("json-kafka")) {
            if (cmd.hasOption("jp") && cmd.hasOption("ks") && cmd.hasOption("kt")) {
                props.put("json-port", cmd.getOptionValue("jp"));
                props.put("kafka-server", cmd.getOptionValue("ks"));
                props.put("kafka-topic", cmd.getOptionValue("kt"));
                System.out.println(props.toString());
                jsonToKafka.run(props);
            } else {
                System.out.println("Arguments are not complete for mode json-kafka:");
                System.out.println("json-port, kafka-server, kafka-topic must be all present");
                pause();
                System.exit(1);
                return;
            }
        }
        // kafka-json
        else if (mode.equals("kafka-filter")) {
            if(cmd.hasOption("ks") && cmd.hasOption("kt") && cmd.hasOption("ktf") &&
                    cmd.hasOption("kaid") && cmd.hasOption("kcid")){

                props.put("kafka-server", cmd.getOptionValue("ks"));
                props.put("kafka-topic", cmd.getOptionValue("kt"));
                props.put("kafka-topic-filtered", cmd.getOptionValue("ktf"));
                props.put("kafka-appid", cmd.getOptionValue("kaid"));
                props.put("kafka-clientid", cmd.getOptionValue("kcid"));

                System.out.println(props.toString());;
                kafkaFilter.run(props);
            }
        }

        // no mode match
        else {
            System.out.println("No matching mode for: '"+ mode +"'");
            pause();
        }
        System.out.println("End of program");
        pause();
    }

    public static void pause() {
        if(debug==false){
            return;
        }
        System.out.println("DEBUG: Pausing on exit");
        try {
            Thread.sleep(1000 * 60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
