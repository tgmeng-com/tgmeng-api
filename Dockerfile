# 1. 选择基础镜像（使用 OpenJDK 运行环境）
FROM openjdk:21-jdk-slim

# 2. 设置工作目录
WORKDIR /app

# 3. 复制项目的 JAR 文件到容器中
COPY target/tgmeng-api-0.0.1-SNAPSHOT.jar /app/my-app.jar

# 4. 暴露应用端口（Spring Boot 默认 8080，可根据你的项目修改）
EXPOSE 4399

# 5. 运行 Java 应用
CMD java ${JAVA_OPTS} -jar my-app.jar