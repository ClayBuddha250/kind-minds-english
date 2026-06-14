# API 契约文档 (用户信息模块)

## 1 用户信息查询

### 基本信息
- **路径**: GET /api/v1/user/me
- **描述**: 获取当前登录用户的个人信息（ID、用户名、邮箱、角色、注册时间等）
- **认证**: 需要认证（请求头携带 `Authorization: Bearer <accessToken>`）

### Request
无需请求体。

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
        "createdAt": "2026-06-14T01:24:06",
        "updatedAt": "2026-06-14T01:24:06"
    }
}
```

### Response 401 (认证失败)
```json
{
    "code": 401,
    "message": "未认证或 token 无效",
    "data": null
}
```

### Response 10005 (用户不存在)
```json
{
    "code": 10005,
    "message": "用户不存在",
    "data": null
}
```

### 错误码说明
| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 401 | 未认证或 token 无效（未携带 token 或 token 已过期） |
| 10005 | 用户不存在（用户被删除或 ID 无效） |

### 请求头说明
| 头部 | 必填 | 说明 |
|------|------|------|
| Authorization | 是 | Bearer 令牌，格式：`Bearer <accessToken>` |

### 响应字段说明
| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 用户唯一标识，自增 |
| username | String | 用户显示名 |
| email | String | 登录邮箱 |
| role | Integer | 角色：0=普通用户, 1=管理员 |
| createdAt | String | 账户创建时间，格式：ISO 8601 日期时间 |
| updatedAt | String | 账户最近更新时间，格式：ISO 8601 日期时间 |
