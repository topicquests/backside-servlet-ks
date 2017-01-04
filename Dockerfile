FROM openjdk:8-jdk-alpine

RUN apk add --no-cache curl tar bash

ARG MAVEN_VERSION=3.3.9
ARG USER_HOME_DIR="/root"

RUN mkdir -p /usr/share/maven /usr/share/maven/ref \
  && curl -fsSL http://apache.osuosl.org/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz \
    | tar -xzC /usr/share/maven --strip-components=1 \
  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

WORKDIR /app

ADD . .
RUN mvn clean install -DskipTests
RUN sed -i.bak 's/localhost/es/' config/provider-config.xml

VOLUME /app/data/backside
EXPOSE 8080

ENTRYPOINT ["./wait-for-it.sh", "-t", "0", "es:9200", "--"]
CMD ["./run.sh"]
