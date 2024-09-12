# 使用 OpenJDK 1.8 作为基础镜像
FROM openjdk:8-jdk

# 安装 Maven 3.8.8
ARG MAVEN_VERSION=3.8.8
ARG USER_HOME_DIR="/root"
ARG BASE_URL=https://apache.osuosl.org/maven/maven-3/${MAVEN_VERSION}/binaries
RUN apt-get update && apt-get install -y wget \
    && wget -q ${BASE_URL}/apache-maven-${MAVEN_VERSION}-bin.tar.gz -P /tmp \
    && tar -xzf /tmp/apache-maven-${MAVEN_VERSION}-bin.tar.gz -C /opt \
    && ln -s /opt/apache-maven-${MAVEN_VERSION} /opt/maven \
    && rm /tmp/apache-maven-${MAVEN_VERSION}-bin.tar.gz

ENV MAVEN_HOME=/opt/maven
ENV PATH=$MAVEN_HOME/bin:$PATH

# 设置项目目录
WORKDIR /app

# 复制项目文件到工作目录
COPY . .

# 构建项目
RUN mvn clean install

# 暴露应用程序的端口
EXPOSE 8080

# 运行生成的 JAR 文件
CMD ["java", "-jar", "target/*.jar"]
