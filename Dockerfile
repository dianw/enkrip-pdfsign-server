FROM openjdk:8-jdk-alpine
VOLUME /pdfsign
ARG JAR_FILE=*.jar
ADD target/${JAR_FILE} /pdfsign/app.jar
ENV JAVA_OPTS=""
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /pdfsign/app.jar