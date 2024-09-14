FROM openjdk:22-jdk-slim

WORKDIR /app

#copy jar into container
COPY target/RestTransactionAPI-0.0.1-SNAPSHOT.jar transaction.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "transaction.jar"]
