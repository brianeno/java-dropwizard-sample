FROM openjdk:21-slim

MAINTAINER bke

WORKDIR /home/root/chargesession

EXPOSE 8000 8001

COPY target/JavaDropWizardSample-1.0-SNAPSHOT.jar app.jar
COPY src/main/resources/application.yml config.yml

ENTRYPOINT ["sh", "-c", "java -jar app.jar db migrate config.yml && java -jar app.jar server config.yml"]