package org.example.kindmindsenglish.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.kindmindsenglish.config.UserDetailsImpl;
import org.example.kindmindsenglish.dto.response.user.MeResponse;
import org.example.kindmindsenglish.service.UserService;
import org.example.kindmindsenglish.util.Result;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户信息查询，修改等用户信息相关请求。
 * </p>
 *
 * @author ClayBuddha250
 * @since 2026-06-14
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 获取当前登录用户的个人信息。
     * 需要携带有效的 accessToken。
     */
    @GetMapping("/me")
    public Result<MeResponse> getCurrentUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        MeResponse response = userService.getCurrentUser(userDetails.getUserId());
        return Result.success(response);
    }
}
