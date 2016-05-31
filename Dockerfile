FROM anapsix/alpine-java:jdk8
WORKDIR /app

ENV GPG_VERSION=2.1.10-r0
RUN apk add --no-cache gnupg=$GPG_VERSION && \
    wget -q https://www.apache.org/dist/ant/KEYS && \
    gpg --import KEYS && rm KEYS

ENV ANT_VERSION=1.9.7 \
    ANT_HOME=/opt/ant \
    PATH=${PATH}:/opt/ant/bin
RUN wget -q http://www.us.apache.org/dist/ant/binaries/apache-ant-${ANT_VERSION}-bin.tar.gz && \
    wget -q http://www.us.apache.org/dist/ant/binaries/apache-ant-${ANT_VERSION}-bin.tar.gz.asc && \
    gpg --verify apache-ant-${ANT_VERSION}-bin.tar.gz.asc && \
    tar xzf apache-ant-${ANT_VERSION}-bin.tar.gz && \
    mv apache-ant-${ANT_VERSION} ${ANT_HOME}${ANT_VERSION} && \
    ln -s ${ANT_HOME}${ANT_VERSION} ${ANT_HOME} && \
    rm -f apache-ant-${ANT_VERSION}-bin.tar.gz.asc && \
    rm -f apache-ant-${ANT_VERSION}-bin.tar.gz

ADD lib lib
ADD . .

RUN sed -i.bak 's/localhost/es/' config/provider-config.xml && \
    cat run.sh | sed -e 's/org\.topicquests\.backside\.servlet\.Main/test\.TestHarness1/' > test.sh && \
    chmod +x test.sh && \
    mkdir -p /app/classes && \
    ant compile

VOLUME /app/data/backside
EXPOSE 8080

ENTRYPOINT ["./wait-for-it.sh", "-t", "0", "es:9200", "--"]
CMD ["./run.sh"]
