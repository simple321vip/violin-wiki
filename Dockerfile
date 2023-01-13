FROM openjdk:11-jre-slim

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN echo 'Asia/Shanghai' >/etc/timezone

ADD target/violin-wiki-*.jar /violin-wiki.jar

ENTRYPOINT ["java","-jar","violin-wiki.jar"]
