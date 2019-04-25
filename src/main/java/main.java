import org.apache.commons.cli.*;

import pipeline.jsonToKafka;

import java.util.Properties;

public class main {
    public static void main(String[] args) {
        Options options = new Options();
        options.addRequiredOption("m","mode",true,"Specify the mode of operation");
        options.addOption("jp","json-port", true, "Specify listening port for json server");
        options.addOption("ks","kafka-server",true,"Specify kafka server");
        options.addOption("kt","kafka-topic",true, "Specify kafka topic to use");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        try{
            cmd = parser.parse(options, args);
        }
        catch (ParseException e){
            System.out.println(e.getMessage());
            System.exit(1);
            return;
        }

        Properties props = new Properties();

        if(cmd.getOptionValue("m") == "json-kafka"){
            if(cmd.hasOption("jp") && cmd.hasOption("ks") && cmd.hasOption("kt")
                    && cmd.getOptionValue("jp") != "default"
                    && cmd.getOptionValue("ks") != "default"
                    && cmd.getOptionValue("kt") != "default"
            ){
                props.put("json-port", cmd.getOptionValue("jp"));
                props.put("kafka-server", cmd.getOptionValue("ks"));
                props.put("kafka-topic", cmd.getOptionValue("kt"));

            }
            else{
                System.out.println("Arguments are not complete for mode json-kafka:");
                System.out.println("json-port, kafka-server, kafka-topic must be all present");
                System.exit(1);
                return;
            }
        }
    }
}
