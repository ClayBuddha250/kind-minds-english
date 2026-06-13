package org.example.kindmindsenglish.service;

import org.example.kindmindsenglish.entity.User;
import org.example.kindmindsenglish.exception.BusinessException;
import org.example.kindmindsenglish.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * UserService 单元测试。
 * 不启动 Spring 容器，只测试注册业务逻辑。
 *
 * @author ClayBuddha250
 * @since 2026-06-14
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock  // 这是一个假的 UserRepository，不需要真实数据库
    private UserRepository userRepository;

    @Mock // 这是一个假的 PasswordEncoder，可以自由控制它返回什么
    private PasswordEncoder passwordEncoder;

    @InjectMocks // 创建一个真实的 UserService，并把上面两个假的 @Mock 注入进去
    private UserService userService;

    private final String username = "测试用户";
    private final String email = "test@example.com";
    private final String rawPassword = "123456";
    private final String encodedPassword = "$2a$10$encodedpasswordhash";

    @Test
    void shouldRegisterSuccessfully() {
        // 假设邮箱在数据库中不存在
        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);
        // 假设邮箱不存在
        when(userRepository.existsByEmail(email)).thenReturn(false);
        // 假设保存用户时，模拟数据库自增 ID
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L); // 模拟数据库自增
            return user;
        });

        // when
        User result = userService.register(username, email, rawPassword);

        // then
        // 断言：返回的 User 对象不为空
        assertNotNull(result);
        // 断言：指定字段被正确设置为指定值
        assertEquals(1L, result.getId());
        assertEquals(username, result.getUsername());
        assertEquals(email, result.getEmail());
        assertEquals(encodedPassword, result.getPasswordHash());
        assertEquals(0, result.getRole());
        assertEquals(1, result.getStatus());

        // 验证（verify）：existsByEmail 被调用过一次，参数是 email
        verify(userRepository).existsByEmail(email);
        // 验证：save 方法被调用过一次，保存了任意 User 对象
        verify(userRepository).save(any(User.class));
        // 验证：encode 方法被调用过一次，参数是原始密码
        verify(passwordEncoder).encode(rawPassword);
    }

    @Test
    void shouldThrowExceptionWhenEmailExists() {
        // given: 邮箱已存在
        when(userRepository.existsByEmail(email)).thenReturn(true);

        // when & then
        // 断言：调用 register 方法会抛出 BusinessException
        BusinessException exception = assertThrows(
                BusinessException.class, () -> userService.register(username, email, rawPassword)
        );

        // 断言：异常的错误码是 10001
        assertEquals(10001, exception.getCode());
        // 断言：异常消息包含 "邮箱已被注册"
        assertTrue(exception.getMessage().contains("邮箱已被注册"));

        // 验证 save 从未被调用
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldEncodePasswordBeforeSaving() {
        // given
        // 邮箱不存在
        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);
        when(userRepository.existsByEmail(email)).thenReturn(false);
        // 保存时直接返回传入的对象（无 ID 也无所谓，这个测试只关心密码）
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        User result = userService.register(username, email, rawPassword);

        // then
        // 断言：存储的密码不等于原始密码
        assertNotEquals(rawPassword, result.getPasswordHash());
        // 断言：存储的密码等于加密后的密码
        assertEquals(encodedPassword, result.getPasswordHash());

        // 验证 encode 被正确调用,参数是原始密码
        verify(passwordEncoder).encode(rawPassword);
    }
}
