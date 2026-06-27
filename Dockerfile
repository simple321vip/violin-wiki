# ============================================================
# Dockerfile — violin-wiki 生产环境（多阶段 + K8s 优化）
# ============================================================
# 构建：
#   docker build -t registry.violin-work.online/violin-wiki:2.1 .
# ============================================================

# -------- Stage 1: 构建 --------
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /build

COPY pom.xml .
RUN mvn -B -ntp dependency:go-offline

COPY src ./src
RUN mvn -B -ntp clean package -DskipTests \
 && cp target/violin-wiki-*.jar /build/app.jar

# -------- Stage 2: 运行时 --------
FROM eclipse-temurin:17-jre-jammy

ENV TZ=Asia/Shanghai \
    LANG=C.UTF-8 \
    LC_ALL=C.UTF-8 \
    JAVA_TOOL_OPTIONS=""

RUN apt-get update \
 && apt-get install -y --no-install-recommends curl tini tzdata ca-certificates \
 && ln -sf /usr/share/zoneinfo/${TZ} /etc/localtime \
 && rm -rf /var/lib/apt/lists/*

RUN groupadd -r app && useradd -r -g app -d /app -s /sbin/nologin app

WORKDIR /app
COPY --from=builder /build/app.jar /app/app.jar

ENV JVM_HEAP_MIN="256m" \
    JVM_HEAP_MAX="768m" \
    JVM_METASPACE="256m" \
    JVM_GC_LOG_DIR="/app/logs" \
    SPRING_PROFILES_ACTIVE="prod"

VOLUME ["/app/logs", "/app/docsify", "/app/nginx-static", "/tmp"]

USER app
EXPOSE 8081

HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
    CMD curl -fsS http://127.0.0.1:8081/wiki/actuator/health/liveness || exit 1

ENTRYPOINT ["/usr/bin/tini", "--", "sh", "-c", "\
    exec java \
        -XX:+UseContainerSupport \
        -XX:MaxRAMPercentage=75.0 \
        -XX:InitialRAMPercentage=50.0 \
        -XX:+ExitOnOutOfMemoryError \
        -XX:+HeapDumpOnOutOfMemoryError \
        -XX:HeapDumpPath=/app/logs/heapdump.hprof \
        -Xms${JVM_HEAP_MIN} \
        -Xmx${JVM_HEAP_MAX} \
        -XX:MaxMetaspaceSize=${JVM_METASPACE} \
        -Duser.timezone=${TZ} \
        -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} \
        -Dfile.encoding=UTF-8 \
        -jar /app/app.jar \
"]