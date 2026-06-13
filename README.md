# 好心人英文 - 后端服务 (KindMinds English API)

## 一、项目介绍
基于 Spring Boot 的 RESTful API 服务，为英语学习平台提供后端支持。

## 二、实现功能

### 用户认证模块
| 功能 | API | 状态 | 说明 |
|------|-----|------|------|
| 用户注册 | POST /api/v1/auth/register | ✅ 已完成 | 支持用户名、邮箱、密码注册，密码BCrypt加密存储 |

### 功能清单
- [x] 用户注册
- [ ] 用户登录
- [ ] 密码重置
- [ ] 用户信息查询
- [ ] 用户信息更新

## 三、功能架构图

```
┌─────────────────────────────────────────────────────────────┐
│                      前端应用                                │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼ HTTP/JSON
┌─────────────────────────────────────────────────────────────┐
│                    Controller 层                             │
│  ┌─────────────────┐                                        │
│  │  AuthController │  /api/v1/auth/register                 │
│  └─────────────────┘                                        │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                    Service 层                               │
│  ┌─────────────────┐                                        │
│  │   UserService   │  register() 密码加密、邮箱唯一性校验        │
│  └─────────────────┘                                        │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                  Repository 层                               │
│  ┌─────────────────┐                                        │
│  │  UserRepository │  existsByEmail(), save()               │
│  └─────────────────┘                                        │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                    数据库                                    │
│                   MySQL 8.0                                 │
│                    user 表                                   │
└─────────────────────────────────────────────────────────────┘
```

## 四、技术选型

| 类别 | 技术 | 版本 |
|------|------|------|
| 语言 | Java | 21 |
| 框架 | Spring Boot | 4.1.0 |
| 数据库 | MySQL | 8.0 |
| ORM | Spring Data JPA (Hibernate) | 7.4.1 |
| 连接池 | HikariCP | 7.0.2 |
| 密码加密 | BCrypt | - |
| 参数校验 | Jakarta Validation | - |
| 构建工具 | Maven | 3.9.x |

## 五、本地开发环境搭建

### 1. 环境要求
- Java 21+
- MySQL 8.0+
- Maven 3.9+

### 2. 数据库准备
```sql
CREATE DATABASE `kind_minds_english`
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- 创建 user 表，详见 docs/database-schema.md
```

### 3. 配置修改
修改 `src/main/resources/application-dev.yml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/kind_minds_english
    username: root
    password: 你的密码
```

### 4. 启动项目
```bash
# 设置 JAVA_HOME（如果系统默认不是 Java 21）
# Windows PowerShell:
$env:JAVA_HOME = "D:\javajdk\21"

# 启动
mvn spring-boot:run
```

### 5. 测试 API
```bash
# 注册用户
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"测试用户","email":"test@example.com","password":"123456"}'
```

## 六、API 设计原则

### 1. 统一响应格式
所有 API 返回统一的 JSON 结构：
```json
{
    "code": 200,
    "message": "success",
    "data": { 
      "任意条数据": "任意条数据"
    }
}
```

### 2. 错误码规范
| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 参数校验失败 |
| 10001 | 邮箱已注册 |

### 3. RESTful 规范
- 使用 HTTP 方法表达操作意图：POST(创建)、GET(查询)、PUT(更新)、DELETE(删除)
- URL 使用名词，版本号前缀：`/api/v1/...`
- 请求/响应使用 JSON 格式

## 七、文档索引

| 文档 | 说明 |
|------|------|
| [docs/api-contract.md](docs/api-contract.md) | API 契约文档 |
| [docs/database-schema.md](docs/database-schema.md) | 数据库设计文档 |
