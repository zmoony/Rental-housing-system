#!/bin/bash

# 租房管理系统启动脚本

echo "正在启动租房管理系统..."

# 检查Docker是否运行
if ! docker info > /dev/null 2>&1; then
    echo "错误: Docker未运行，请先启动Docker"
    exit 1
fi

# 检查Docker Compose是否安装
if ! command -v docker-compose &> /dev/null; then
    echo "错误: Docker Compose未安装"
    exit 1
fi

# 停止现有容器
echo "停止现有容器..."
docker-compose down

# 构建并启动服务
echo "构建并启动服务..."
docker-compose up --build -d

# 等待服务启动
echo "等待服务启动..."
sleep 30

# 检查服务状态
echo "检查服务状态..."
docker-compose ps

echo "系统启动完成！"
echo "前端访问地址: http://localhost"
echo "后端API地址: http://localhost/api"
echo "数据库地址: localhost:5432"
echo ""
echo "默认管理员账号:"
echo "用户名: admin"
echo "密码: admin123"
