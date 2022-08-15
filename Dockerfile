FROM ccr.ccs.tencentyun.com/violin/tomcat:8.5

ADD target/violin-book.war /opt/bitnami/tomcat/webapps

ENV ALLOW_EMPTY_PASSWORD=yes
