# Spring Boot 后端 Dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

# 复制 Maven 配置文件
COPY pom.xml .
COPY src ./src

# 安装 Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# 构建应用
RUN mvn clean package -DskipTests

# 运行应用
EXPOSE 8080
CMD ["java", "-jar", "target/zufang-backend-1.0.0.jar"]
