# 使用官方Java 8镜像作为基础镜像
FROM openjdk:8-jdk-alpine


RUN mkdir /app && \
	apk add --no-cache curl netcat-openbsd

# 设置环境变量，防止在安装过程中出现交互式提示
# ENV JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom"
ENV JAVA_OPTS=""
ENV APP_HOME=/app
ENV APP_PORT=9898
ENV JAR_FILE=web_demo.jar
ENV ACTIVE_PROFILE=dev

# 设置工作目录为 /app
WORKDIR $APP_HOME

# 将本地目录下的所有文件复制到容器的 /app 目录下
COPY ./$JAR_FILE $APP_HOME/$JAR_FILE

# 安装必要的工具，如 curl 和 netcat 用于健康检查
# RUN apk add --no-cache curl netcat-openbsd

# 运行 Maven 命令来构建 Java 应用程序
# RUN mvn -f $APP_HOME/pom.xml clean package

# 声明容器运行时监听的端口
EXPOSE $APP_PORT

# 设置容器启动时执行的命令，这里使用 Java 运行 JAR 包
# ENTRYPOINT ["java", "$JAVA_OPTS", "-jar", "$APP_HOME/$JAR_FILE"]
ENTRYPOINT ["sh", "-c", "java -jar -Dspring.profiles.active=$ACTIVE_PROFILE $APP_HOME/$JAR_FILE"]
#ENTRYPOINT ["java", "-jar", "/app/web_demo.jar"]

# 设置维护者信息
MAINTAINER onlymark <onlymark@example.com>

# 为镜像添加元数据
LABEL version="1.0" \
      description="My Java Web Application" \
      maintainer="onlymark <onlymark@example.com>"

# 定义健康检查命令
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD netcat -zv :$APP_PORT || exit 1