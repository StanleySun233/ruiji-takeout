#!/bin/bash

# 设置要部署的项目目录和Git仓库地址
PROJECT_DIR="/root/code/ruiji-takeout" # 替换为您的项目路径
REPO_URL="https://github.com/StanleySun233/ruiji-takeout.git" # 替换为您的Git仓库地址
BRANCH="main" # 替换为您的分支名称

# 导航到项目目录
cd "$PROJECT_DIR" || exit

# 检查项目目录是否为空
if [ ! -d "$PROJECT_DIR/.git" ]; then
  echo "项目目录为空，开始克隆仓库..."
  git clone "$REPO_URL" "$PROJECT_DIR"
else
  echo "项目目录已存在，拉取最新代码..."
  git pull origin "$BRANCH"
fi

# 构建项目
echo "使用Maven构建项目..."
mvn clean install

# 检查端口是否被占用
PORT=8080

# 使用 lsof 查找占用端口的进程 ID (PID)
PID=$(lsof -ti:$PORT)

# 检查端口是否被占用
if [ -z "$PID" ]; then
  echo "端口 $PORT 未被占用。"
else
  echo "端口 $PORT 被占用，进程 ID: $PID"

  # 终止占用端口的进程
  kill -9 $PID
  echo "已终止占用端口 $PORT 的进程（PID: $PID）。"
fi

# 运行项目
echo "运行Java程序..."
java -jar target/*.jar # 假设目标目录下的jar文件是要运行的程序

echo "部署完成！"
