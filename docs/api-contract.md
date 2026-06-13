# API 契约文档 (用户认证模块)

## 1 用户注册

### 基本信息
- **路径**: POST /api/v1/auth/register
- **描述**: 注册新用户，密码使用 BCrypt 加密存储
- **认证**: 无需认证

### Request
```json
{
    "username": "string, 必填, 最大50字符",
    "email": "string, 必填, 邮箱格式, 唯一",
    "password": "string, 必填, 6-30位"
}
```

### Response 200 (成功)
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "id": 1,
        "username": "测试用户",
        "email": "test@example.com",
        "role": 0,
        "status": 1
    }
}
```

### Response 400 (参数校验失败)
```json
{
    "code": 400,
    "message": "参数校验失败",
    "data": null
}
```

### Response 10001 (邮箱已注册)
```json
{
    "code": 10001,
    "message": "邮箱已注册",
    "data": null
}
```

### 错误码说明
| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 参数校验失败（用户名/邮箱/密码格式不正确） |
| 10001 | 邮箱已被其他用户注册 |

### 字段校验规则
| 字段 | 规则 |
|------|------|
| username | 必填，不能为空字符串 |
| email | 必填，符合邮箱格式，数据库唯一 |
| password | 必填，长度 6-30 位 |

### 响应字段说明
| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 用户唯一标识，自增 |
| username | String | 用户显示名 |
| email | String | 登录邮箱 |
| role | Integer | 角色：0=普通用户, 1=管理员 |
| status | Integer | 状态：0=无效, 1=有效 |
