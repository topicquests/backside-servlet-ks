FROM anapsix/alpine-java:jdk8
WORKDIR /app

ENV ANT_VERSION=1.9.6 \
    ANT_HOME=/opt/ant \
    PATH=${PATH}:/opt/ant/bin

RUN cd && wget -q http://www.us.apache.org/dist/ant/binaries/apache-ant-${ANT_VERSION}-bin.tar.gz && \
    tar xzf apache-ant-${ANT_VERSION}-bin.tar.gz && \
    mv apache-ant-${ANT_VERSION} ${ANT_HOME}${ANT_VERSION} && \
    ln -s ${ANT_HOME}${ANT_VERSION} ${ANT_HOME} && \
    rm -f apache-ant-${ANT_VERSION}-bin.tar.gz

COPY . /app
RUN cd /app && ant compile

VOLUME /app/data/backside
EXPOSE 8080

CMD ["./run.sh"]
