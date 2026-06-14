package org.example.kindmindsenglish.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * <p>
 * 用户认证刷新请求体。前端判断需要刷新令牌时传入此对象。
 * 使用 JSR-303 注解进行基础参数校验。
 * </p>
 *
 * @author ClayBuddha250
 * @since 2026-06-14
 */
@Data
public class RefreshRequest {
    @NotBlank(message = "刷新令牌不能为空")
    private String refreshToken;
}
