FROM bitnami/tomcat:9.0.64-debian-11-r10
COPY ROOT.war /opt/bitnami/tomcat/webapps
ENV ALLOW_EMPTY_PASSWORD=yes

