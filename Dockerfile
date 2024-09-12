# 使用 Maven 官方镜像构建应用
FROM maven:3.8.6-openjdk-8 AS builder

# 设置工作目录
WORKDIR /app

# 将 Maven 项目源代码拷贝到容器中
COPY . .

# 使用 Maven 构建项目
RUN mvn clean package -DskipTests

# 使用官方 Java 运行时镜像作为运行阶段
FROM openjdk:8-jdk-alpine

# 设置工作目录
WORKDIR /app

# 从构建阶段拷贝 JAR 文件
COPY --from=builder "/app/target/reggie-takeout.jar" app.jar

# 暴露应用端口
EXPOSE 8080

# 运行 JAR 文件
ENTRYPOINT ["java", "-jar", "app.jar"]
