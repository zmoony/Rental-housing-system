# 租房管理系统技术文档

## 系统概述

租房管理系统是一个基于现代化技术栈的Web应用，提供房屋管理、租户管理、合同管理、水电费计算等核心功能。

## 技术架构

### 前端技术栈
- **Vue 3**: 渐进式JavaScript框架
- **Element Plus**: Vue 3 UI组件库
- **TypeScript**: 类型安全的JavaScript超集
- **Vite**: 快速的前端构建工具
- **Pinia**: Vue 3状态管理库
- **Vue Router**: Vue官方路由管理器
- **Axios**: HTTP客户端库

### 后端技术栈
- **Spring Boot 3.2**: Java企业级应用框架
- **Spring Security**: 安全框架
- **MyBatis Plus**: 数据访问层框架
- **PostgreSQL**: 关系型数据库
- **JWT**: JSON Web Token认证
- **Maven**: 项目构建工具

### 部署技术
- **Docker**: 容器化部署
- **Docker Compose**: 多容器编排
- **Nginx**: 反向代理服务器

## 功能模块

### 1. 用户管理
- 用户注册/登录
- 角色权限管理（管理员、房东、租户）
- 个人信息管理

### 2. 房屋管理
- 房屋信息录入
- 房屋状态管理
- 房屋设施管理
- 房屋图片管理

### 3. 房间管理
- 房间信息管理
- 房间类型分类
- 租金设置
- 房间状态跟踪

### 4. 租户管理
- 租户信息录入
- 租户状态管理
- 紧急联系人管理
- 入住/退房记录

### 5. 合同管理
- 合同创建
- 合同续签
- 合同终止
- 合同状态跟踪
- 自动到期检查

### 6. 水电费管理
- 水电表读数记录
- 自动费用计算
- 月度账单生成
- 费用标准配置
- 逾期提醒

### 7. 财务管理
- 租金收取记录
- 费用统计报表
- 收入分析
- 账单管理

## 数据库设计

### 核心表结构

#### 用户表 (users)
- 存储系统用户信息
- 支持多种角色类型
- 包含认证和状态管理

#### 房屋表 (houses)
- 房屋基本信息
- 房东关联
- 设施和图片管理

#### 房间表 (rooms)
- 房间详细信息
- 租金和押金设置
- 状态管理

#### 合同表 (contracts)
- 合同基本信息
- 租期管理
- 状态跟踪

#### 水电费记录表 (utility_records)
- 水电表读数
- 用量计算
- 费用计算

#### 租金记录表 (rent_records)
- 租金收取记录
- 缴费状态跟踪
- 月度账单管理

## 核心业务逻辑

### 水电费计算逻辑

1. **读数记录**: 定期记录水电表读数
2. **用量计算**: 当前读数 - 上次读数 = 用量
3. **费用计算**: 用量 × 单价 = 费用
4. **自动生成**: 每月自动生成账单

### 合同管理逻辑

1. **合同创建**: 验证房间和租户状态
2. **状态更新**: 自动更新房间和租户状态
3. **到期检查**: 定时检查合同到期
4. **续签/终止**: 支持合同续签和终止

### 定时任务

- **合同到期检查**: 每天凌晨1点执行
- **月度账单生成**: 每月1号执行
- **逾期检查**: 每天上午9点执行

## API接口设计

### 认证接口
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/logout` - 用户登出
- `GET /api/auth/user-info` - 获取用户信息

### 合同管理接口
- `POST /api/contracts` - 创建合同
- `GET /api/contracts/{id}` - 获取合同详情
- `PUT /api/contracts/{id}/renew` - 续签合同
- `PUT /api/contracts/{id}/terminate` - 终止合同

### 水电费管理接口
- `POST /api/utilities/record` - 记录水电费读数
- `GET /api/utilities/room/{roomId}` - 获取房间水电费记录
- `POST /api/utilities/generate-monthly-bills` - 生成月度账单

## 部署指南

### 环境要求
- Docker 20.10+
- Docker Compose 2.0+
- 至少4GB内存
- 10GB可用磁盘空间

### 快速部署

1. **克隆项目**
```bash
git clone <repository-url>
cd zufang
```

2. **启动服务**
```bash
docker-compose up -d
```

3. **访问系统**
- 前端: http://localhost
- 后端API: http://localhost/api
- 数据库: localhost:5432

### 配置说明

#### 数据库配置
- 数据库名: zufang_db
- 用户名: zufang_user
- 密码: zufang_password

#### 应用配置
- 后端端口: 8080
- 前端端口: 3000
- Nginx端口: 80

## 开发指南

### 本地开发环境搭建

1. **后端开发**
```bash
cd backend
mvn spring-boot:run
```

2. **前端开发**
```bash
cd frontend
npm install
npm run dev
```

3. **数据库启动**
```bash
docker run -d -p 5432:5432 -e POSTGRES_DB=zufang_db -e POSTGRES_USER=zufang_user -e POSTGRES_PASSWORD=zufang_password postgres:13
```

### 代码规范

- 后端使用Java 17语法规范
- 前端使用TypeScript严格模式
- 遵循RESTful API设计原则
- 使用Git Flow工作流

## 安全考虑

1. **认证授权**: JWT Token + Spring Security
2. **数据验证**: 前后端双重验证
3. **SQL注入防护**: MyBatis Plus参数化查询
4. **XSS防护**: Vue 3内置防护
5. **CSRF防护**: Spring Security默认配置

## 性能优化

1. **数据库优化**: 索引优化、查询优化
2. **缓存策略**: Redis缓存热点数据
3. **前端优化**: 代码分割、懒加载
4. **CDN加速**: 静态资源CDN分发

## 监控和日志

1. **应用监控**: Spring Boot Actuator
2. **日志管理**: Logback + ELK Stack
3. **性能监控**: Micrometer + Prometheus
4. **错误追踪**: Sentry集成

## 扩展功能

### 计划中的功能
1. **移动端支持**: 响应式设计优化
2. **消息通知**: 邮件/短信通知
3. **报表系统**: 数据可视化
4. **支付集成**: 在线支付功能
5. **多租户支持**: SaaS模式

### 技术升级计划
1. **微服务架构**: Spring Cloud
2. **消息队列**: RabbitMQ/Kafka
3. **缓存升级**: Redis Cluster
4. **数据库优化**: 读写分离
5. **容器编排**: Kubernetes

## 常见问题

### Q: 如何重置管理员密码？
A: 直接操作数据库，更新users表中admin用户的password字段。

### Q: 如何备份数据？
A: 使用PostgreSQL的pg_dump命令进行数据备份。

### Q: 如何扩展系统功能？
A: 按照现有的代码结构，添加新的Controller、Service、Entity等组件。

## 联系方式

如有技术问题或建议，请联系开发团队。
