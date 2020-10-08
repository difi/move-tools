FROM openjdk:8-jdk-alpine

MAINTAINER Johannes Molland <johannes.molland@digdir.no>
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

ARG jarPath
COPY ${jarPath} app.jar

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar ${0} ${@}"]

HEALTHCHECK --interval=30s --timeout=2s --retries=3 CMD wget -qO- "http://localhost:8081/health" || exit 1