package com.zicca.icoupon.agent;

import org.junit.jupiter.api.Test;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 测试类
 *
 * @author zicca
 */
@SpringBootTest
public class ChatModelTest {

    @Test
    public void testOpenAi(@Autowired OpenAiChatModel chatModel) {
        String content = chatModel.call("你好你是谁？");
        System.out.println(content);
    }

    @Test
    public void testDeepSeek(@Autowired DeepSeekChatModel chatModel) {
        String content = chatModel.call("你好你是谁？");
        System.out.println(content);
    }


}
