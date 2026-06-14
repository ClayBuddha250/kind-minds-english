package org.example.kindmindsenglish.dto.request.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * <p>
 * 管理员创建单词请求体。前端提交单词表单时传入此对象。
 * 使用 JSR-303 注解进行基础参数校验。
 * </p>
 *
 * @author ClayBuddha250
 * @since 2026-06-14
 */
@Data
public class WordCreateRequest {
    /**
     * 单词原型（英文），不能为空。
     */
    @NotBlank(message = "单词不能为空")
    private String word;

    /**
     * 中文释义，支持多个释义用逗号分隔。
     */
    private String translation;

    /**
     * 音标（英/美）。
     */
    private String phonetic;

    /**
     * 英文释义。
     */
    private String definition;

    /**
     * 词性（如 n., v., adj.）。
     */
    private String pos;

    /**
     * 柯林斯星级（1-5星）。
     */
    private Integer collins;

    /**
     * 牛津3000核心词标记（0-否,1-是）。
     */
    private Integer oxford;

    /**
     * 考试标签（如 CET-4, CET-6, IELTS），多个标签用逗号分隔。
     */
    private String tag;

    /**
     * 英国国家语料库词频排名。
     */
    private Integer bnc;

    /**
     * 当代语料库词频排名。
     */
    private Integer frq;

    /**
     * 时态/复数等变换（JSON格式）。
     */
    private String exchange;

    /**
     * 扩展信息（JSON格式）。
     */
    private String detail;

    /**
     * 读音音频URL。
     */
    private String audio;
}