FROM maven:3.6.1-jdk-11 AS build  
COPY src /usr/src/app/src  
COPY pom.xml /usr/src/app  
RUN mvn -f /usr/src/app/pom.xml clean package

FROM openjdk:11  
COPY --from=build /usr/src/app/target/ccproject-1.0-SNAPSHOT.jar /usr/app/ccproject-1.0-SNAPSHOT.jar  
EXPOSE 5600  
ENTRYPOINT ["java","-jar","/usr/app/ccproject-1.0-SNAPSHOT.jar"]  