FROM maven:3.6.1-jdk-11 AS build  
COPY src /usr/src/app/src  
COPY pom.xml /usr/src/app  
RUN mvn -f /usr/src/app/pom.xml clean package

FROM openjdk:11-jre-slim  
COPY --from=build /usr/src/app/target/ccproject-1.0-SNAPSHOT.jar /ccproject.jar

ENV mode "default"
ENV json_port "5600"
ENV kafka_server "kfk-cp-kafka-headless:9092"
ENV kafka_topic "main"
ENV debug "false"

ENTRYPOINT ["java", "-jar", "/ccproject.jar"] 
CMD --mode ${mode} --json-port ${json_port} --kafka-server ${kafka_server} --kafka-topic ${kafka_topic} --debug ${debug}
# ENTRYPOINT [ "/bin/bash" ]