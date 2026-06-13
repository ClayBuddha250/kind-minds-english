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

## 二、用户认证模块
### 1. user 用户表
#### 表功能
存储平台所有用户的基本认证信息与角色状态。

#### 字段说明
id              - BIGINT        - PRIMARY KEY, NOT NULL, AUTO_INCREMENT         - 唯一用户标识
username        - VARCHAR(50)   - NOT NULL                                      - 用户显示名（非登录凭证）
password_hash   - VARCHAR(255)  - NOT NULL                                      - 加密密码
email           - VARCHAR(255)  - NOT NULL, UNIQUE                              - 用户邮箱
role            - TINYINT       - NOT NULL, DEFAULT 0                           - 角色：0-普通用户,1-管理员
status          - TINYINT       - NOT NULL, DEFAULT 1                           - 状态：0-无效用户,1-有效用户
created_at      - DATETIME      - NOT NULL, DEFAULT CURRENT_TIMESTAMP           - 创建时间
updated_at      - DATETIME      - NOT NULL, DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP - 更新时间

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


