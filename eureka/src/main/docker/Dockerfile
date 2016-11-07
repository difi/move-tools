FROM openjdk:8-jre-alpine
MAINTAINER DIFI <espen.korra@difi.no>

LABEL package="no.difi" artifact="eureka" version="1.0" description="Direktoratet for forvaltning og IKT (Difi)"

#
# Open ports for incoming connections
#

EXPOSE 8761

#
# Install application
#

ENV APP_DIR=/var/lib/difi \
    APP_JAVA_PARAMS="-Xmx1g"

RUN apk add --update \
        curl \
        tini \
	rm -rf /var/cache/apk/*

#
# Start the application
#

WORKDIR ${APP_DIR}


ENTRYPOINT ["/sbin/tini", "-g", "--", "java", "-jar", "app.jar"]

ADD *.jar ${APP_DIR}/app.jar