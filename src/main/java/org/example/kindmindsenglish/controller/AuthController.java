package org.example.kindmindsenglish.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.kindmindsenglish.dto.request.RegisterRequest;
import org.example.kindmindsenglish.dto.response.UserResponse;
import org.example.kindmindsenglish.entity.User;
import org.example.kindmindsenglish.service.UserService;
import org.example.kindmindsenglish.util.Result;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 认证控制器，处理注册、登录、令牌刷新等认证相关请求。
 * </p>
 *
 * @author ClayBuddha250
 * @since 2026-06-14
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户注册。
     *
     * @param request 注册请求体，包含用户名、邮箱、密码
     * @return 注册成功的用户信息
     */
    @PostMapping("/register")
    public Result<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.debug("收到注册请求：username={}, email={}", request.getUsername(), request.getEmail());
        User user = userService.register(
                request.getUsername(),
                request.getEmail(),
                request.getPassword()
        );

        // 组装响应体
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setStatus(user.getStatus());

        log.debug("注册响应：id={}", user.getId());
        return Result.success(response);
    }
}
