package org.example.kindmindsenglish.exception;

import lombok.Getter;

/**
 * 业务异常，用于在 Service 层抛出，由全局异常处理器捕获。
 * 携带错误码和消息，确保前端收到的 JSON 结构与 API 契约一致。
 *
 * @author ClayBuddha250
 * @since 2026-06-14
 */
@Getter
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }
}
