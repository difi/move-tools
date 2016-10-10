FROM openjdk:8-jre-alpine
MAINTAINER DIFI <espen.korra@difi.no>

LABEL package="no.difi" artifact="meldingsutveksling" version="1.0" description="Direktoratet for forvaltning og IKT (Difi)"

ENV APP_DIR=/var/lib/difi


RUN apk add --update \
	    ca-certificates \
        curl \
        tini \
	    openssl  && \
	    update-ca-certificates && \
	    rm -rf /var/cache/apk/*

#
# Install Java unlimited security policy
#

RUN cd /tmp/ && \
    curl -LO "http://download.oracle.com/otn-pub/java/jce/8/jce_policy-8.zip" -H 'Cookie: oraclelicense=accept-securebackup-cookie' && \
    unzip jce_policy-8.zip && \
    rm jce_policy-8.zip && \
    yes |cp -v /tmp/UnlimitedJCEPolicyJDK8/*.jar ${JAVA_HOME}/lib/security/

#
# Start the application
#

WORKDIR ${APP_DIR}

EXPOSE 8080

ENTRYPOINT ["/sbin/tini", "--", "./run.sh"]

#
# Install application
#

RUN echo -e '#!/bin/sh\njava -jar ${APP_DIR}/app.jar' > ${APP_DIR}/run.sh && \
	chmod a+x ${APP_DIR}/run.sh
ADD *.jar ${APP_DIR}/app.jar
#
# Open ports for incoming connections
#
