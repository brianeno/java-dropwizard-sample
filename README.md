# Java Dropwizard Sample Application


# Database

docker-compose up -d

java -jar target/JavaDropWizardSample-1.0-SNAPSHOT.jar db status src/main/resources/application.yml

java -jar target/JavaDropWizardSample-1.0-SNAPSHOT.jar db migrate src/main/resources/application.yml


# Application

java -jar target/JavaDropWizardSample-1.0-SNAPSHOT.jar server src/main/resources/application.yml
