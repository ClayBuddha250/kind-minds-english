package org.example.kindmindsenglish.dto.response;

import lombok.Data;

/**
 * <p>
 * 用户响应体。注册成功后返回给前端，不包含密码等敏感字段。
 * </p>
 *
 * @author ClayBuddha250
 * @since 2026-06-14
 */
@Data
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private Integer role;   // 0=普通用户,1=管理员
    private Integer status; // 0=无效,1=有效
}
