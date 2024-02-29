#引入JDK配置
FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=spring-boot-auth.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]