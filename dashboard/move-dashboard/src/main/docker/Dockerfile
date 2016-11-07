FROM openjdk:8-jre-alpine
MAINTAINER DIFI <espen.korra@difi.no>

LABEL package="no.difi" artifact="meldingsutveksling" version="1.0" description="Direktoratet for forvaltning og IKT (Difi)"

ENV APP_DIR=/var/lib/difi


RUN apk add --update \
        curl \
        tini \
	rm -rf /var/cache/apk/*

#
# Start the application
#

WORKDIR ${APP_DIR}

EXPOSE 8080

ENTRYPOINT ["/sbin/tini", "-g", "--", "./run.sh"]

#
# Install application
#

RUN echo -e '#!/bin/sh\njava -jar ${APP_DIR}/app.jar' > ${APP_DIR}/run.sh && \
	chmod a+x ${APP_DIR}/run.sh
ADD *.jar ${APP_DIR}/app.jar
#
# Open ports for incoming connections
#
