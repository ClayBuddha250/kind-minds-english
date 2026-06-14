# API 契约文档 (单词管理模块)

## 1 创建单词

### 基本信息
- **路径**: POST /api/v1/admin/words
- **描述**: 管理员创建新单词，支持完整单词信息录入
- **认证**: 需要认证（请求头携带 `Authorization: Bearer <accessToken>`，且用户角色必须为管理员）

### Request
```json
{
    "word": "string, 必填, 单词原型",
    "translation": "string, 选填, 中文释义",
    "phonetic": "string, 选填, 音标",
    "definition": "string, 选填, 英文释义",
    "pos": "string, 选填, 词性",
    "collins": "Integer, 选填, 柯林斯星级(1-5)",
    "oxford": "Integer, 选填, 牛津3000标记(0-1)",
    "tag": "string, 选填, 考试标签",
    "bnc": "Integer, 选填, BNC词频排名",
    "frq": "Integer, 选填, 当代语料库词频排名",
    "exchange": "string, 选填, 时态/复数变换(JSON)",
    "detail": "string, 选填, 扩展信息(JSON)",
    "audio": "string, 选填, 读音音频URL"
}
```

### Response 200 (成功)
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "id": 1,
        "word": "apple",
        "translation": "苹果",
        "phonetic": "/ˈæp.əl/",
        "definition": "a round fruit with red or green skin and white flesh",
        "pos": "n.",
        "collins": 5,
        "oxford": 1,
        "tag": "CET-4,IELTS",
        "bnc": 100,
        "frq": 150,
        "exchange": "{\"plural\":\"apples\"}",
        "detail": "{\"example\":\"I eat an apple every day.\"}",
        "audio": "https://example.com/audio/apple.mp3",
        "createdAt": "2026-06-14T10:30:00",
        "updatedAt": "2026-06-14T10:30:00"
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

### Response 401 (认证失败)
```json
{
    "code": 401,
    "message": "未认证或 token 无效",
    "data": null
}
```

### Response 403 (无权限)
```json
{
    "code": 403,
    "message": "未认证或 token 无效",
    "data": null
}
```

### Response 20001 (单词重复创建)
```json
{
    "code": 20001,
    "message": "单词重复创建",
    "data": null
}
```

### 错误码说明
| 错误码   | 说明                                   |
|-------|--------------------------------------|
| 200   | 成功                                   |
| 400   | 参数校验失败（word字段为空）                     |
| 401   | 未认证或 token 无效（未携带 token 或 token 已过期） |
| 403   | 无权限访问（用户非管理员角色）                      |
| 20001 | 单词重复创建（单词已存在于数据库中）                   |

### 请求头说明
| 头部            | 必填 | 说明                                  |
|---------------|----|-------------------------------------|
| Authorization | 是  | Bearer 令牌，格式：`Bearer <accessToken>` |
| Content-Type  | 是  | `application/json`                  |

### 字段校验规则
| 字段          | 规则                |
|-------------|-------------------|
| word        | 必填，不能为空字符串，数据库唯一  |
| translation | 选填，支持多释义用逗号分隔     |
| phonetic    | 选填，支持英美音标注音       |
| definition  | 选填，英文释义文本         |
| pos         | 选填，如 n., v., adj. |
| collins     | 选填，1-5 整数         |
| oxford      | 选填，0 或 1          |
| tag         | 选填，多个标签用逗号分隔      |
| bnc         | 选填，正整数            |
| frq         | 选填，正整数            |
| exchange    | 选填，JSON 格式字符串     |
| detail      | 选填，JSON 格式字符串     |
| audio       | 选填，有效 URL         |

### 响应字段说明
| 字段          | 类型      | 说明                    |
|-------------|---------|-----------------------|
| id          | Long    | 单词唯一标识，自增             |
| word        | String  | 单词原型                  |
| translation | String  | 中文释义                  |
| phonetic    | String  | 音标                    |
| definition  | String  | 英文释义                  |
| pos         | String  | 词性                    |
| collins     | Integer | 柯林斯星级（1-5）            |
| oxford      | Integer | 牛津3000核心词标记（0=否, 1=是） |
| tag         | String  | 考试标签                  |
| bnc         | Integer | 英国国家语料库词频排名           |
| frq         | Integer | 当代语料库词频排名             |
| exchange    | String  | 时态/复数等变换（JSON格式）      |
| detail      | String  | 扩展信息（JSON格式）          |
| audio       | String  | 读音音频URL               |
| createdAt   | String  | 创建时间，格式：ISO 8601 日期时间 |
| updatedAt   | String  | 更新时间，格式：ISO 8601 日期时间 |
