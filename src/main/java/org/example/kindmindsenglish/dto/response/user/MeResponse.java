package org.example.kindmindsenglish.dto.response.user;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 用户信息响应体。查询成功后返回给前端，不包含密码等敏感字段。
 * </p>
 *
 * @author ClayBuddha250
 * @since 2026-06-14
 */
@Data
public class MeResponse {
    private Long id;                  // 用户 ID
    private String username;
    private String email;
    private Integer role;   // 0=普通用户,1=管理员
    private LocalDateTime createdAt; // 账户创建时间
    private LocalDateTime updatedAt; // 账户最近更新时间
}
