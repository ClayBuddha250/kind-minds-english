# 数据库设计文档

## 一、整体说明
- 数据库名：`kind_minds_english`
- 字符集：utf8mb4
- 引擎：InnoDB
- 设计原则：所有表均含 id、created_at、updated_at

### 建库语句
```sql
CREATE DATABASE `kind_minds_english`
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
```

### 数据库关系图
```
user 1 ─── * user_word_progress * ─── 1 word
user 1 ─── * user_favorites * ─── 1 word
user 1 ─── * study_record
```

## 二、用户认证模块

### 1. user 用户表
#### 表功能
存储平台所有用户的基本认证信息与角色状态。

#### 字段说明
| 字段名           | 类型           | 约束                                                              | 说明               |
|---------------|--------------|-----------------------------------------------------------------|------------------|
| id            | BIGINT       | PRIMARY KEY, NOT NULL, AUTO_INCREMENT                           | 唯一用户标识           |
| username      | VARCHAR(50)  | NOT NULL                                                        | 用户显示名（非登录凭证）     |
| password_hash | VARCHAR(255) | NOT NULL                                                        | 加密密码（BCrypt）     |
| email         | VARCHAR(255) | NOT NULL, UNIQUE                                                | 用户邮箱（登录凭证）       |
| role          | TINYINT      | NOT NULL, DEFAULT 0                                             | 角色：0-普通用户,1-管理员  |
| status        | TINYINT      | NOT NULL, DEFAULT 1                                             | 状态：0-无效用户,1-有效用户 |
| created_at    | DATETIME     | NOT NULL, DEFAULT CURRENT_TIMESTAMP                             | 创建时间             |
| updated_at    | DATETIME     | NOT NULL, DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 更新时间             |

#### DDL
```sql
CREATE TABLE `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '唯一用户标识',
    `username` VARCHAR(50) NOT NULL COMMENT '用户显示名',
    `password_hash` VARCHAR(255) NOT NULL COMMENT '加密密码',
    `email` VARCHAR(255) NOT NULL COMMENT '登录邮箱',
    `role` TINYINT NOT NULL DEFAULT 0 COMMENT '角色：0-普通用户,1-管理员',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-无效,1-有效',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
```

## 三、单词库模块

### 1. word 单词表
#### 表功能
存储平台所有单词数据，管理员可增删改查，用户学习时从中获取。

#### 字段说明
| 字段名         | 类型           | 约束                                                    | 说明                         |
|-------------|--------------|-------------------------------------------------------|----------------------------|
| id          | BIGINT       | PRIMARY KEY, NOT NULL, AUTO_INCREMENT                 | 主键                         |
| word        | VARCHAR(255) | NOT NULL, UNIQUE                                      | 单词（英文）                     |
| translation | TEXT         | NULL                                                  | 中文释义（支持多个释义，用逗号分隔）         |
| phonetic    | VARCHAR(255) | NULL                                                  | 音标（英/美）                    |
| definition  | TEXT         | NULL                                                  | 英文释义                       |
| pos         | VARCHAR(100) | NULL                                                  | 词性（如n., v., adj.）          |
| collins     | TINYINT      | NULL                                                  | 柯林斯星级（1-5星）                |
| oxford      | TINYINT      | NULL                                                  | 牛津3000核心词标记（0-否,1-是）       |
| tag         | VARCHAR(255) | NULL                                                  | 考试标签（如CET-4, CET-6, IELTS） |
| bnc         | INT          | NULL                                                  | 英国国家语料库词频排名                |
| frq         | INT          | NULL                                                  | 当代语料库词频排名                  |
| exchange    | TEXT         | NULL                                                  | 时态/复数等变换（JSON格式）           |
| detail      | TEXT         | NULL                                                  | 扩展信息（JSON格式）               |
| audio       | VARCHAR(255) | NULL                                                  | 读音音频URL                    |
| created_at  | DATETIME     | DEFAULT CURRENT_TIMESTAMP                             | 创建时间                       |
| updated_at  | DATETIME     | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 更新时间                       |

#### DDL
```sql
CREATE TABLE `word` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `word` VARCHAR(255) NOT NULL COMMENT '单词',
    `translation` TEXT COMMENT '中文释义',
    `phonetic` VARCHAR(255) COMMENT '音标',
    `definition` TEXT COMMENT '英文释义',
    `pos` VARCHAR(100) COMMENT '词性',
    `collins` TINYINT COMMENT '柯林斯星级',
    `oxford` TINYINT COMMENT '牛津3000核心词',
    `tag` VARCHAR(255) COMMENT '考试标签',
    `bnc` INT COMMENT '英国国家语料库词频',
    `frq` INT COMMENT '当代语料库词频',
    `exchange` TEXT COMMENT '时态复数等变换',
    `detail` TEXT COMMENT 'json扩展信息',
    `audio` VARCHAR(255) COMMENT '读音音频url',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_word` (`word`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='单词表';
```
