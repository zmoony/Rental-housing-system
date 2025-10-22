@echo off
REM 租房管理系统启动脚本 (Windows)

echo 正在启动租房管理系统...

REM 检查Docker是否运行
docker info >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: Docker未运行，请先启动Docker Desktop
    pause
    exit /b 1
)

REM 检查Docker Compose是否安装
docker-compose --version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: Docker Compose未安装
    pause
    exit /b 1
)

REM 停止现有容器
echo 停止现有容器...
docker-compose down

REM 构建并启动服务
echo 构建并启动服务...
docker-compose up --build -d

REM 等待服务启动
echo 等待服务启动...
timeout /t 30 /nobreak >nul

REM 检查服务状态
echo 检查服务状态...
docker-compose ps

echo.
echo 系统启动完成！
echo 前端访问地址: http://localhost
echo 后端API地址: http://localhost/api
echo 数据库地址: localhost:5432
echo.
echo 默认管理员账号:
echo 用户名: admin
echo 密码: admin123
echo.
pause
