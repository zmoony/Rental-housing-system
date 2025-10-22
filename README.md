# 租房管理系统

## 项目简介
基于 Vue3 + Element Plus + Spring Boot + PostgreSQL 的现代化租房管理系统，提供房屋管理、租户管理、合同管理、水电费计算等核心功能。

## 技术栈
- **前端**: Vue 3 + Element Plus + TypeScript + Vite + Pinia
- **后端**: Spring Boot + Spring Security + MyBatis Plus + JWT
- **数据库**: PostgreSQL
- **部署**: Docker + Nginx

## 功能模块
1. 用户管理（管理员、房东、租户）
2. 房屋管理（房源信息、房间管理）
3. 租户管理（租户信息、入住记录）
4. 合同管理（合同创建、续签、终止）
5. 水电费管理（费用记录、自动计算、账单生成）
6. 财务管理（租金收取、费用统计）
7. 系统管理（权限管理、日志记录）

## 项目结构
```
zufang/
├── frontend/                 # Vue3 前端项目
│   ├── src/
│   │   ├── components/       # 公共组件
│   │   ├── views/           # 页面组件
│   │   ├── router/          # 路由配置
│   │   ├── store/           # Pinia 状态管理
│   │   ├── api/             # API 接口
│   │   ├── utils/           # 工具函数
│   │   └── types/           # TypeScript 类型定义
│   ├── package.json
│   └── vite.config.ts
├── backend/                 # Spring Boot 后端项目
│   ├── src/main/java/
│   │   └── com/zufang/
│   │       ├── controller/  # 控制器
│   │       ├── service/     # 业务逻辑
│   │       ├── mapper/      # 数据访问层
│   │       ├── entity/      # 实体类
│   │       ├── dto/         # 数据传输对象
│   │       ├── config/      # 配置类
│   │       └── utils/       # 工具类
│   ├── src/main/resources/
│   │   ├── application.yml  # 配置文件
│   │   └── mapper/          # MyBatis XML
│   └── pom.xml
├── database/               # 数据库相关
│   ├── init.sql           # 初始化脚本
│   └── migrations/        # 数据库迁移脚本
├── docker/                # Docker 配置
│   ├── docker-compose.yml
│   └── Dockerfile
└── docs/                  # 文档
    ├── api.md             # API 文档
    └── database.md        # 数据库设计文档
```

## 快速开始

### 环境要求
- Docker 20.10+
- Docker Compose 2.0+
- 至少4GB内存
- 10GB可用磁盘空间

### 一键启动 (推荐)
```bash
# Linux/Mac
chmod +x start.sh
./start.sh

# Windows
start.bat
```

### 手动启动
```bash
# 1. 停止现有容器
docker-compose down

# 2. 构建并启动所有服务
docker-compose up --build -d

# 3. 查看服务状态
docker-compose ps
```

### 访问系统
- 前端界面: http://localhost
- 后端API: http://localhost/api
- 数据库: localhost:5432

### 默认账号
- 用户名: `admin`
- 密码: `admin123`

### 本地开发
1. 启动 PostgreSQL 数据库
2. 运行数据库初始化脚本: `database/init.sql`
3. 启动后端服务: `cd backend && mvn spring-boot:run`
4. 启动前端服务: `cd frontend && npm run dev`

## 许可证
MIT License
