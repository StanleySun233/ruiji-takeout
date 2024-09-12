#!/bin/bash

# 设置要部署的项目目录和Git仓库地址
echo "请确保/root/code/ruiji-takeout为项目地址"
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

cd "$PROJECT_DIR/ruiji-takeout" || exit

docker build -t ruiji-takeout .
docker run -p 8081:8080 ruiji-takeout