package org.example.kindmindsenglish.dto.response.admin;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 创建单词响应体。创建成功后返回给前端。
 * </p>
 *
 * @author ClayBuddha250
 * @since 2026-06-14
 */
@Data
public class WordResponse {
    private long id;
    private String word;
    private String translation;
    private String phonetic;
    private String definition;
    private String pos;
    private Integer collins;
    private Integer oxford;
    private String tag;
    private Integer bnc;
    private Integer frq;
    private String exchange;
    private String detail;
    private String audio;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
