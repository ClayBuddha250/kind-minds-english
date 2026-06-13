package org.example.kindmindsenglish.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

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
@Data                       // Lombok：自动生成 getter/setter/toString/equals/hashCode
@Entity                     // 告诉 JPA：这是一个实体类，要和数据库表映射
@Table(name = "user")       // 指定映射的数据库表名是 user
public class User {

    /**
     * 主键 ID，由数据库自动生成。
     * 对应 DDL：id BIGINT NOT NULL AUTO_INCREMENT
     */
    @Id                                                     // 标记这是主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // 对应 AUTO_INCREMENT，数据库自动递增
    private Long id;

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

    /**
     * 用户邮箱，登录时的唯一凭证。
     * unique = true 只是给 JPA 建表用的提示，
     * 真正的唯一保证是数据库里的 UNIQUE KEY uk_email。
     * 对应 DDL：email VARCHAR(255) NOT NULL UNIQUE
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * 用户角色：0 = 普通用户，1 = 管理员。
     * 对应 DDL：role TINYINT NOT NULL DEFAULT 0
     */
    @Column(nullable = false)
    private Integer role;

    /**
     * 账户状态：0 = 无效用户，1 = 有效用户。
     * 对应 DDL：status TINYINT NOT NULL DEFAULT 1
     */
    @Column(nullable = false)
    private Integer status;

    /**
     * 账户创建时间，一旦写入就不再修改。
     * updatable = false：JPA 更新操作不会修改这个字段。
     * 对应 DDL：created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 账户最后更新时间，每次更新记录时自动刷新。
     * 对应 DDL：updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
     */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 在第一次保存（插入）到数据库之前，自动调用这个方法。
     * 作用：给创建时间和更新时间赋初始值（当前时间）。
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * 在每次更新数据之前，自动调用这个方法。
     * 作用：刷新更新时间为当前时间。
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
