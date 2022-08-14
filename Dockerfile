FROM bitnami/tomcat:9.0.64-debian-11-r10

ADD target/violin-book.war /opt/bitnami/tomcat/webapps

ENV ALLOW_EMPTY_PASSWORD=yes
