FROM bellsoft/liberica-openjdk-alpine:17.0.6-10
ARG JAR_FILE=target/daily-survey-bot-0.1-SNAPSHOT.jar
WORKDIR /opt/app
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]