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
    APP_PREFIX=eureka \
    APP_JAVA_PARAMS="-Xmx1g"

RUN apk add --update \
	    ca-certificates \
        curl \
	    openssl  && \
	    update-ca-certificates && \
	    rm -rf /var/cache/apk/*

RUN cd /tmp/ && \
    curl -LO "http://download.oracle.com/otn-pub/java/jce/8/jce_policy-8.zip" -H 'Cookie: oraclelicense=accept-securebackup-cookie' && \
    unzip jce_policy-8.zip && \
    rm jce_policy-8.zip && \
    yes |cp -v /tmp/UnlimitedJCEPolicyJDK8/*.jar ${JAVA_HOME}/lib/security/

#
# Start the application
#

WORKDIR ${APP_DIR}

CMD APP_NAME=$(ls ${APP_PREFIX}*.jar) && java ${APP_JAVA_PARAMS} -jar ${APP_NAME}

ADD ${APP_PREFIX}*.jar ${APP_DIR}/