package com.zicca.icoupon.agent.config;

import com.alibaba.cloud.ai.memory.redis.RedisChatMemoryRepository;
import com.zicca.icoupon.agent.tools.CouponTool;
import com.zicca.icoupon.agent.tools.ShopTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 智能体配置
 *
 * @author zicca
 */
@Configuration
public class AiConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.timeout}")
    private int redisTimeout;

    @Bean
    public RedisChatMemoryRepository redisChatMemoryRepository() {
        return RedisChatMemoryRepository.builder()
                .host(redisHost)
                .port(redisPort)
                .timeout(redisTimeout)
                .build();
    }

    @Bean
    public ChatMemory chatMemory(RedisChatMemoryRepository redisChatMemoryRepository) {
        return MessageWindowChatMemory.builder()
                .maxMessages(100)
                .chatMemoryRepository(redisChatMemoryRepository)
                .build();
    }

    @Bean
    public ChatClient chatClient(DeepSeekChatModel chatModel, ChatMemory chatMemory, CouponTool couponTool, ShopTool shopTool) {
        return ChatClient.builder(chatModel)
                .defaultSystem("""
                        ##角色
                            您是“爱券”公司的客户聊天支持代理。请以友好、乐于助人且愉快的方式来回复。
                            可以适当增加emoji表情来拉近与客户的距离。
                            您正在通过在线聊天系统与客户互动。
                        ##要求
                            1.在涉及增删改（除了查询）function-call前，必须等待用户回复“确认”后再调用tool。
                            2.请讲中文。
                            3.请严格返回 JSON 格式参数，不能包含多余文本、换行、注释、符号。
                        """)
                .defaultAdvisors(
                        PromptChatMemoryAdvisor.builder(chatMemory).build() // RedisChatMemory
                )
                .defaultTools(couponTool, shopTool)
                .build();
    }

}
