
# 构建阶段
FROM ubuntu:22.04
FROM gradle:8.4-jdk17 AS builder
WORKDIR /app

# 先复制构建文件以利用缓存
COPY build.gradle .
COPY settings.gradle .
COPY gradle gradle
RUN gradle dependencies --no-daemon

# 复制源代码
COPY src src

# 执行构建
RUN gradle clean build -x test --no-daemon

# 运行阶段
FROM openjdk:17-jdk-slim
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar mental-cove.jar

# Flyway 和健康检查
HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENV JAVA_OPTS="-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/app/dump -XX:+PrintFlagsFinal -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:MinRAMPercentage=20.0"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Dspring.profiles.active=prod -jar mental-cove.jar"]
