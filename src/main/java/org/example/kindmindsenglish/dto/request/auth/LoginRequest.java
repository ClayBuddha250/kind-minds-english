package org.example.kindmindsenglish.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * <p>
 * 用户登录请求体。前端提交登录表单时传入此对象。
 * 使用 JSR-303 注解进行基础参数校验。
 * </p>
 *
 * @author ClayBuddha250
 * @since 2026-06-14
 */
@Data
public class LoginRequest {

    /**
     * 登录邮箱，不能为空，必须符合邮箱格式。
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Size(max=254, message = "邮箱字符不能超过254位")
    private String email;

    /**
     * 明文密码，不能为空，长度至少6位。
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, message = "密码长度至少6位")
    private String password;
}
