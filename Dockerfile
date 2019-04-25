FROM maven:3.6.1-jdk-11 AS build  
COPY src /usr/src/app/src  
COPY pom.xml /usr/src/app  
RUN mvn -f /usr/src/app/pom.xml clean package

FROM openjdk:11  
COPY --from=build /usr/src/app/target/ccproject-deploy.jar /usr/app/ccproject-deploy.jar

ENV mode "default"
ENV json_port "default"
ENV kafka_server "default"
ENV kafka_topic "default"

ENTRYPOINT ["java","-jar","/usr/app/ccproject-deploy.jar", \
            "--mode", ${mode}, \
            "--json-port", ${json-port}, \
            "--kafka-server", ${kafka-server}, \
            "--kafka-topic", ${kafka-topic}]