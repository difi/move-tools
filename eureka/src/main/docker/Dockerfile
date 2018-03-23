FROM openjdk:10-slim

MAINTAINER DIFI <idporten@difi.no>

LABEL package="no.difi.move" artifact="eureka" version="1.0" description="Direktoratet for forvaltning og IKT (Difi)"

EXPOSE 8761

WORKDIR /application

ENTRYPOINT exec java $JAVA_OPTS -jar app.jar

ENV JAVA_OPTS="--add-modules java.xml.bind"

HEALTHCHECK CMD curl -s --fail http://localhost:8081/manage/health > /dev/null || exit 1

RUN apt-get -qq update && apt-get install -qq curl && apt-get -qq clean

COPY *.jar /application/app.jar
