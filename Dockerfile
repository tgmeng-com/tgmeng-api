# 1. 选择基础镜像（使用 OpenJDK 运行环境）
FROM openjdk:21-jdk-slim

# 2. 设置工作目录
WORKDIR /app

# 3. 复制项目的 JAR 文件到容器中
COPY target/tgmeng-api-v1.0.0.jar /app/my-app.jar

# 4. 暴露应用端口（Spring Boot 默认 8080，可根据你的项目修改）
EXPOSE 4399
# 运行 Java 应用
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+ExitOnOutOfMemoryError -XX:+AlwaysPreTouch -Djava.security.egd=file:/dev/./urandom"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar my-app.jar"]