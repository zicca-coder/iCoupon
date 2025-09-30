package com.zicca.icoupon.agent.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * 智能体控制层
 *
 * @author zicca
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/agent")
@Tag(name = "智能体", description = "智能体接口管理")
public class AiController {

    private final ChatClient chatClient;

    @PostMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chat(@RequestParam(value = "message") String message) {
        return chatClient.prompt().user(message).stream().content();
    }


}
