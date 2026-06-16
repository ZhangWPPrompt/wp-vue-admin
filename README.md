# wp-vue-admin

基于 Vue3 + Element Plus + Spring Boot 的前后端分离后台管理系统脚手架。

## 技术栈

| 层级 | 技术 |
|------|------|
| 前端 | Vue 3, Element Plus, Vite, Pinia, Vue Router, Axios |
| 后端 | Spring Boot 3, MyBatis Plus, Spring Security |
| 认证 | JWT 无状态认证, BCrypt 密码加密 |
| 权限 | RBAC (用户-角色-菜单) |
| 数据库 | H2 内存数据库 (开发模式) |

## 功能模块

- 登录 / 退出
- 首页仪表盘
- 用户管理 (CRUD + 角色分配)
- 角色管理 (CRUD + 菜单分配)
- 菜单管理 (树形 CRUD)
- 字典管理 (字典+字典项)
- 操作日志查询

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.8+
- Node.js 18+

### 启动后端

```bash
cd backend
mvn package -DskipTests
java -jar target/admin-server-1.0.0.jar
```

后端默认运行在 `http://localhost:3001`

### 启动前端

```bash
cd frontend
npm install
npm run dev
```

前端默认运行在 `http://localhost:5173`，已配置 `/api` 反向代理到后端。

### 一键启动

```bash
./start.sh
```

### 登录

- 用户名: `admin`
- 密码: `admin123`

## 项目结构

```
├── backend/
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/admin/
│       │   ├── config/          # Security, CORS, MyBatisPlus 配置
│       │   ├── controller/      # REST API 控制器
│       │   ├── dto/             # 请求/响应 DTO
│       │   ├── entity/          # 数据库实体
│       │   ├── mapper/          # MyBatis Plus Mapper
│       │   ├── security/        # JWT + Spring Security 集成
│       │   ├── service/         # 业务接口 + 实现
│       │   └── util/            # 工具类
│       └── resources/
│           ├── application.yml  # 应用配置
│           ├── schema.sql       # 建表 DDL
│           └── data.sql         # 初始数据
│
├── frontend/
│   ├── vite.config.js           # Vite 配置 (代理 + allowedHosts)
│   └── src/
│       ├── api/                 # axios + 各模块 API
│       ├── router/              # 路由 + 登录守卫
│       ├── store/               # Pinia 状态管理
│       ├── utils/               # 工具函数
│       └── views/               # 页面组件
│           ├── dashboard/       # 首页
│           ├── layout/          # 主布局
│           ├── login/           # 登录
│           ├── system/          # 用户/角色/菜单管理
│           ├── tool/dict/       # 字典管理
│           └── log/operation/   # 操作日志
│
└── start.sh                     # 一键启动脚本
```

## API 接口

| 接口 | 方法 | 说明 |
|------|------|------|
| /api/auth/login | POST | 登录 |
| /api/auth/userinfo | GET | 获取用户信息和菜单 |
| /api/auth/logout | POST | 退出 |
| /api/users | GET/POST | 用户列表/新增 |
| /api/users/{id} | GET/PUT/DELETE | 用户详情/编辑/删除 |
| /api/users/{id}/roles | GET/PUT | 用户角色查询/分配 |
| /api/roles | GET/POST | 角色列表/新增 |
| /api/roles/{id} | GET/PUT/DELETE | 角色详情/编辑/删除 |
| /api/roles/{id}/menus | GET/PUT | 角色菜单查询/分配 |
| /api/menus/tree | GET | 菜单树 |
| /api/menus/{id} | GET/POST/PUT/DELETE | 菜单 CRUD |
| /api/dicts | GET/POST | 字典列表/新增 |
| /api/dicts/{id} | GET/PUT/DELETE | 字典 CRUD |
| /api/dicts/code/{code} | GET | 按编码查字典项 |
| /api/logs | GET | 操作日志列表 |
