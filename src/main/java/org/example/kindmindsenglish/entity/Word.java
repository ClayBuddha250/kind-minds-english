package org.example.kindmindsenglish.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 单词实体，映射到数据库的 {@code word} 表。
 * 存储平台所有单词的释义、音标、词频、考试标签等信息。
 * </p>
 *
 * <p>
 * 字段命名策略：Java 驼峰命名 ↔ 数据库下划线命名，通过 {@link Column#name()} 显式映射。
 * 时间字段由 JPA 生命周期回调 {@link #onCreate()} 和 {@link #onUpdate()} 自动维护。
 * </p>
 *
 * @author ClayBuddha250
 * @since 2026-06-14
 */
@Data                       // Lombok：自动生成 getter/setter/toString/equals/hashCode
@Entity                     // 告诉 JPA：这是一个实体类，要和数据库表映射
@Table(name = "word")       // 指定映射的数据库表名是 word
public class Word {

    /**
     * 主键 ID，由数据库自动生成。
     * 对应 DDL：id BIGINT NOT NULL AUTO_INCREMENT
     */
    @Id                                                     // 标记这是主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // 对应 AUTO_INCREMENT，数据库自动递增
    private Long id;

    /**
     * 单词原型（英文）。
     * 对应 DDL：word VARCHAR(255) NOT NULL
     */
    @Column(nullable = false)  // 非空
    private String word;

    /**
     * 中文释义，支持多个释义用逗号分隔。
     * 对应 DDL：translation TEXT
     */
    @Column(columnDefinition = "TEXT")
    private String translation;

    /**
     * 音标（英/美）。
     * 对应 DDL：phonetic VARCHAR(255)
     */
    @Column
    private String phonetic;

    /**
     * 英文释义。
     * 对应 DDL：definition TEXT
     */
    @Column(columnDefinition = "TEXT")
    private String definition;

    /**
     * 词性（如 n., v., adj.）。
     * 对应 DDL：pos VARCHAR(100)
     */
    @Column(length = 100)
    private String pos;

    /**
     * 柯林斯星级（1-5星）。
     * 对应 DDL：collins TINYINT
     */
    @Column
    private Integer collins;

    /**
     * 牛津3000核心词标记（0-否,1-是）。
     * 对应 DDL：oxford TINYINT
     */
    @Column
    private Integer oxford;

    /**
     * 考试标签（如 CET-4, CET-6, IELTS），多个标签用逗号分隔。
     * 对应 DDL：tag VARCHAR(255)
     */
    @Column
    private String tag;

    /**
     * 英国国家语料库词频排名。
     * 对应 DDL：bnc INT
     */
    @Column
    private Integer bnc;

    /**
     * 当代语料库词频排名。
     * 对应 DDL：frq INT
     */
    @Column
    private Integer frq;

    /**
     * 时态/复数等变换（JSON格式）。
     * 对应 DDL：exchange TEXT
     */
    @Column(columnDefinition = "TEXT")
    private String exchange;

    /**
     * 扩展信息（JSON格式）。
     * 对应 DDL：detail TEXT
     */
    @Column(columnDefinition = "TEXT")
    private String detail;

    /**
     * 读音音频URL。
     * 对应 DDL：audio VARCHAR(255)
     */
    @Column
    private String audio;

    /**
     * 单词创建时间，一旦写入就不再修改。
     * updatable = false：JPA 更新操作不会修改这个字段。
     * 对应 DDL：created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 单词最后更新时间，每次更新记录时自动刷新。
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