# 使用官方的 Maven 镜像，它带有 Maven 和 JDK 1.8
FROM maven:3.6.3-jdk-8 AS builder

# 设置工作目录
WORKDIR /app

# 将项目的 pom.xml 文件复制到容器中
COPY pom.xml .

# 下载项目依赖
RUN mvn dependency:go-offline -B

# 将项目的源代码复制到容器中
COPY src ./src

# 使用 Maven 构建项目
RUN mvn clean package -DskipTests

# 使用 OpenJDK 1.8 作为基础镜像
FROM openjdk:8-jdk-alpine

# 创建目录并设置工作目录
WORKDIR /app

# 从构建阶段复制生成的 JAR 文件
COPY --from=builder /app/target/*.jar app.jar

# 运行 Spring Boot 应用
ENTRYPOINT ["java", "-jar", "app.jar"]

# 暴露应用运行的端口（根据你的 Spring Boot 应用端口设置）
EXPOSE 8080