FROM amazoncorretto:21-alpine-jdk
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar ./app.jar
EXPOSE 8080