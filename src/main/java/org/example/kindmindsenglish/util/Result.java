package org.example.kindmindsenglish.util;

import lombok.Data;

/**
 * <p>
 * 统一 API 返回体。所有 Controller 的返回值都封装为此对象，
 * 确保前端收到的数据结构始终一致。
 * </p>
 *
 * @param <T> data 字段的具体类型
 * @author ClayBuddha250
 * @since 2026-06-14
 */
@Data
public class Result<T> {

    private int code;       // 状态码：200=成功，其他=异常
    private String message; // 提示信息
    private T data;         // 返回数据

    // 成功，带数据
    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.code = 200;
        r.message = "success";
        r.data = data;
        return r;
    }

    // 失败，带错误码和消息
    public static <T> Result<T> error(int code, String message) {
        Result<T> r = new Result<>();
        r.code = code;
        r.message = message;
        return r;
    }
}
