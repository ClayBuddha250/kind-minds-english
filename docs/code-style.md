# 代码注释格式契约文档

## 一、概述

本文档定义了 KindMinds English 项目中 Java 代码的注释规范，旨在提高代码可读性、维护性和效率。

## 二、注释风格原则

| 原则     | 说明                                |
|--------|-----------------------------------|
| **清晰** | 注释应清晰表达代码意图，避免歧义                  |
| **简洁** | 注释应简洁明了，不重复代码本身已表达的内容             |
| **一致** | 注释格式应在整个项目中保持一致                   |
| **及时** | 代码变更时应同步更新相关注释                    |
| **有用** | 注释应提供代码本身无法表达的信息（设计意图、业务规则、约束条件等） |

## 三、类注释规范

### 3.1 格式要求

```java
/**
 * <p>
 * 类的简要描述，说明类的职责和核心功能。
 * </p>
 *
 * <p>
 * 可选：类的设计说明、字段命名策略、关键实现细节等。
 * </p>
 *
 * @author 作者名
 * @since 创建日期（格式：YYYY-MM-DD）
 * @see 相关类（可选，Service类建议添加）
 */
public class ClassName {
    // ...
}
```

### 3.2 示例

**Controller 类**（参考：`AuthController.java`）：
```java
/**
 * <p>
 * 认证控制器，处理注册、登录、令牌刷新等认证相关请求。
 * </p>
 *
 * @author ClayBuddha250
 * @since 2026-06-14
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    // ...
}
```

**Service 类**（参考：`UserService.java`）：
```java
/**
 * <p>
 * 用户业务逻辑层，负责处理与用户相关的所有操作。
 * 包括注册、登录验证、用户信息更新等。
 * </p>
 *
 * @author ClayBuddha250
 * @since 2026-06-14
 * @see UserRepository
 * @see PasswordEncoder
 */
@Service
public class UserService {
    // ...
}
```

**Entity 类**（参考：`User.java`）：
```java
/**
 * <p>
 * 用户实体，映射到数据库的 {@code user} 表。
 * 存储平台所有用户的认证信息、角色和状态。
 * </p>
 *
 * <p>
 * 字段命名策略：Java 驼峰命名 ↔ 数据库下划线命名，通过 {@link Column#name()} 显式映射。
 * 时间字段由 JPA 生命周期回调 {@link #onCreate()} 和 {@link #onUpdate()} 自动维护。
 * </p>
 *
 * @author ClayBuddha250
 * @since 2026-06-13
 */
@Entity
@Table(name = "user")
public class User {
    // ...
}
```

## 四、方法注释规范

### 4.1 格式要求

```java
/**
 * 方法的简要描述，说明方法的功能和作用。
 *
 * @param 参数名 参数说明（必填参数需注明“不能为空”等约束）
 * @return 返回值说明（说明返回的数据结构和含义）
 * @throws 异常类 异常触发条件（如有）
 */
public ReturnType methodName(ParamType param) throws ExceptionType {
    // ...
}
```

### 4.2 示例

**Controller 方法**（参考：`AuthController.register()`）：
```java
/**
 * 用户注册。
 *
 * @param request 注册请求体，包含用户名、邮箱、密码
 * @return 注册成功的用户信息
 */
@PostMapping("/register")
public Result<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
    // ...
}
```

**Service 方法**（参考：`UserService.register()`）：
```java
/**
 * 注册新用户。
 * 校验邮箱是否已存在，将明文密码加密后保存。
 *
 * @param username    用户显示名，不能为空
 * @param email       登录邮箱，不能为空且唯一
 * @param rawPassword 明文密码，不能为空（至少6位由 Controller 校验）
 * @return 保存成功的用户响应体（含自增ID）
 * @throws BusinessException 邮箱已被注册时抛出
 */
public UserResponse register(String username, String email, String rawPassword) {
    // ...
}
```

## 五、字段注释规范

### 5.1 格式要求

| 字段类型 | 注释要求 |
|----------|----------|
| DTO 字段 | 字段含义 + 约束说明（如必填、格式要求等） |
| Entity 字段 | 字段含义 + 对应 DDL 说明 + JPA 注解说明 |

### 5.2 示例

**DTO 字段**（参考：`RegisterRequest.java`）：
```java
/**
 * 用户显示名，不能为空。
 */
@NotBlank(message = "用户名不能为空")
private String username;

/**
 * 登录邮箱，不能为空，必须符合邮箱格式。
 */
@NotBlank(message = "邮箱不能为空")
@Email(message = "邮箱格式不正确")
private String email;
```

**Entity 字段**（参考：`User.java`）：
```java
/**
 * 用户显示名（非登录凭证）。
 * 对应 DDL：username VARCHAR(50) NOT NULL
 */
@Column(nullable = false, length = 50)  // 非空，最大50字符
private String username;

/**
 * 加密后的密码（BCrypt）。
 * 注意：表里列名是 password_hash，Java 字段名是 passwordHash，
 * 所以必须用 @Column(name=...) 来指定映射。
 * 对应 DDL：password_hash VARCHAR(255) NOT NULL
 */
@Column(name = "password_hash", nullable = false)
private String passwordHash;
```

## 六、注解注释规范

### 6.1 格式要求

对于重要的注解（如 JPA 注解、Spring 注解），可在注解后添加简短注释说明其作用。

### 6.2 示例

```java
@Data                       // Lombok：自动生成 getter/setter/toString/equals/hashCode
@Entity                     // 告诉 JPA：这是一个实体类，要和数据库表映射
@Table(name = "user")       // 指定映射的数据库表名是 user
public class User {
    
    @Id                                                     // 标记这是主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // 对应 AUTO_INCREMENT，数据库自动递增
    private Long id;
    
    @Column(nullable = false, unique = true)  // 非空且唯一
    private String email;
}
```

## 七、代码块注释规范

### 7.1 格式要求

对于复杂的业务逻辑或算法，可在代码块前添加注释说明其实现思路。

### 7.2 示例

```java
// 检查邮箱唯一性
if (userRepository.existsByEmail(email)) {
    log.warn("注册失败：邮箱已被注册，email={}", email);
    throw new BusinessException(10001, "邮箱已被注册");
}

// 组装用户对象
User user = new User();
user.setUsername(username);
user.setEmail(email);
user.setPasswordHash(passwordEncoder.encode(rawPassword));
user.setRole(0);   // 0 = 普通用户
user.setStatus(1); // 1 = 有效用户
```

## 八、TODO/FIXME 注释规范

### 8.1 格式要求

| 类型 | 格式 | 说明 |
|------|------|------|
| TODO | `// TODO: 待完成事项` | 标记需要后续完成的任务 |
| FIXME | `// FIXME: 问题描述` | 标记需要修复的问题 |

### 8.2 示例

```java
// TODO: 后续需要添加密码强度校验
// FIXME: 此处可能存在并发问题，需要添加分布式锁
```

## 九、注释格式检查清单

### 9.1 类注释检查
- [ ] 包含类的职责描述
- [ ] 包含 `@author` 标签
- [ ] 包含 `@since` 标签
- [ ] Service 类包含 `@see` 标签（引用相关依赖）

### 9.2 方法注释检查
- [ ] 包含方法功能描述
- [ ] 所有参数都有 `@param` 说明
- [ ] 有返回值的方法包含 `@return` 说明
- [ ] 抛出异常的方法包含 `@throws` 说明

### 9.3 字段注释检查
- [ ] DTO 字段包含约束说明
- [ ] Entity 字段包含 DDL 对应说明
- [ ] Entity 字段包含 JPA 注解说明

### 9.4 通用检查
- [ ] 注释语言统一（中文）
- [ ] 标点符号统一（中文标点）
- [ ] 避免无意义的注释（如 `// 获取用户`）
- [ ] 注释与代码保持同步
