FROM bitnami/8.5-debian-11

ADD target/violin-book.war /opt/bitnami/tomcat/webapps

ENV ALLOW_EMPTY_PASSWORD=yes
