package com.zicca.icoupon.agent.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

/**
 * 店铺工具类
 * @author zicca
 */
@Slf4j
@Component
public class ShopTool {

    @Tool(description = "根据店铺名称查询店铺ID")
    public Long getShopIdByName(@ToolParam String shopName) {
        log.info("[智能体服务] | 根据店铺名称查询店铺ID，shopName: {}", shopName);
        return 1810714735922956666L;
    }


}
