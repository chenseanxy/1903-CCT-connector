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
        options.addOption("v", "debug", true, "Set debug flag");

        // json
        options.addOption("jp", "json-port", true, "Specify listening port for json server");

        // kafka
        options.addOption("ks", "kafka-server", true, "Specify kafka server");
        options.addOption("kt", "kafka-topic", true, "Specify kafka topic to use");
        options.addOption("ktf", "kafka-topic-filtered", true, "Specify the filtered topic name");

        // redis
        options.addOption("rs","redis-server", true, "Specify redis server");
        options.addOption("rp", "redis-port", true, "Port to connect to redis, int only");
        options.addOption("rk", "redis-key", true, "Secret for redis connection");
        options.addOption("rc","redis-channel", true, "Redis channel for publisher and subscriber");

        // hbase
        options.addOption("ht","hbase-table",true,"HBase table used for data storage");
        options.addOption("hf","hbase-family",true,"HBase family used for table");
        options.addOption("hs","hbase-server",true,"HBase Master for DB connection");


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
        if (cmd.hasOption("v") && cmd.getOptionValue("v").equals("true")) {
            debug = true;
        } else {
            debug = false;
        }
        System.out.println("Debug: " + debug.toString());

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

        // kafka-filter
        else if (mode.equals("kafka-filter")) {
            if (cmd.hasOption("ks") && cmd.hasOption("kt") && cmd.hasOption("ktf")) {

                props.put("kafka-server", cmd.getOptionValue("ks"));
                props.put("kafka-topic", cmd.getOptionValue("kt"));
                props.put("kafka-topic-filtered", cmd.getOptionValue("ktf"));

                System.out.println(props.toString());

                kafkaFilter.run(props);
            } else {
                System.out.println("Arguments not complete:");
                System.out.println(cmd.toString());
            }
        }

        // kafka-redis
        else if (mode.equals("kafka-redis")){
            if(cmd.hasOption("ks") && cmd.hasOption("ktf")
                    && cmd.hasOption("rs") && cmd.hasOption("rp")
                    && cmd.hasOption("rk") && cmd.hasOption("rc")){

                props.put("kafka-topic", cmd.getOptionValue("ktf")); //Consume from *filtered* topic
                props.put("kafka-server", cmd.getOptionValue("ks"));
                props.put("redis-server", cmd.getOptionValue("rs"));
                props.put("redis-port", cmd.getOptionValue("rp"));
                props.put("redis-key", cmd.getOptionValue("rk"));
                props.put("redis-channel", cmd.getOptionValue("rc"));

                System.out.println("Running kafka-redis");
                System.out.println(props.toString());
                kafkaToRedis.run(props);
            } else {
                System.out.println("Arguments not complete:");
                System.out.println(cmd.toString());
            }
        }

        // redis-hbase
        else if (mode.equals("redis-hbase")){
            if(cmd.hasOption("rs") && cmd.hasOption("rp") && cmd.hasOption("rk") && cmd.hasOption("rc")
                    && cmd.hasOption("ht") && cmd.hasOption("hf") && cmd.hasOption("hs")){
                props.put("redis-server", cmd.getOptionValue("rs"));
                props.put("redis-port", cmd.getOptionValue("rp"));
                props.put("redis-key", cmd.getOptionValue("rk"));
                props.put("redis-channel", cmd.getOptionValue("rc"));
                props.put("hbase-server", cmd.getOptionValue("hs"));
                props.put("hbase-family", cmd.getOptionValue("hf"));
                props.put("hbase-table", cmd.getOptionValue("ht"));

                System.out.println("Running redis-hbase");
                System.out.println(props.toString());
                redisToHbase.run(props);
            } else {
                System.out.println("Arguments not complete:");
                System.out.println(cmd.toString());
            }
        }


        // no mode match
        else {
            System.out.println("No matching mode for: '" + mode + "'");
            pause();
        }
        System.out.println("End of program");
        pause();
    }

    public static void pause() {
        if (debug == false) {
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
