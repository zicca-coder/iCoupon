package com.zicca.icoupon.framework.config;

import com.zicca.icoupon.framework.web.GlobalExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zicca
 */
@Configuration
public class WebAutoConfiguration {


    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

}
