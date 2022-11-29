FROM harbor.bielai.top/library/alpine_glibc_jre8
WORKDIR /app

ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/${TZ} /etc/localtime && \
    echo ${TZ} > /etc/timezone

ENV JAVA_HOME=/usr/local/java
ENV PATH="$PATH:$JAVA_HOME/bin"

COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT $JAVA_HOME/bin/java -jar /app/app.jar