package org.example.kindmindsenglish.controller.admin;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.kindmindsenglish.dto.request.admin.WordCreateRequest;
import org.example.kindmindsenglish.dto.response.admin.WordResponse;
import org.example.kindmindsenglish.service.admin.AdminWordService;
import org.example.kindmindsenglish.util.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 处理添加单词、修改单词、删除单词相关请求。
 * </p>
 *
 * @author ClayBuddha250
 * @since 2026-06-14
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/admin/words")
public class AdminWordController {
    private final AdminWordService adminWordService;

    public AdminWordController(AdminWordService adminWordService) {
        this.adminWordService = adminWordService;
    }

    /**
     * 创建单词。
     *
     * @param wordCreateRequest 创建单词请求体，包含单词原型及其他可选信息
     * @return 创建成功的单词信息
     */
    @PostMapping
    public Result<WordResponse> create(@Valid @RequestBody WordCreateRequest wordCreateRequest) {
        WordResponse wordResponse = adminWordService.createNewWord(wordCreateRequest);
        log.debug("创建单词: {}", wordResponse.getWord());
        return Result.success(wordResponse);
    }
}
