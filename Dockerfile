# 1. 选择基础镜像（使用 OpenJDK 运行环境）
FROM eclipse-temurin:21-jre

# 2. 设置工作目录
WORKDIR /app

# 3. 复制项目的 JAR 文件到容器中
#COPY target/tgmeng-api-v1.0.1.jar /app/my-app.jar。下面这个JAR_FILE是在githubaction里的deploy.yml里面构建镜像的时候传递进来的
ARG JAR_FILE
COPY ${JAR_FILE} /app/my-app.jar

# 4. 暴露应用端口（Spring Boot 默认 8080，可根据你的项目修改）
EXPOSE 4399
# 运行 Java 应用
ENV JAVA_OPTS="-XX:+UseContainerSupport -Djava.security.egd=file:/dev/./urandom"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar my-app.jar"]