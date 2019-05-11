package com.laravelshao.common.spring.config;

import com.laravelshao.common.core.handler.StrategyHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 策略处理器自动装配
 *
 * @author shaoqinghua
 * @date 2019/5/11
 * @description
 */
@Configuration
@ConditionalOnClass(StrategyHandler.class)
public class StrategyHandlerConfiguration {

    @Bean
    @ConditionalOnMissingBean(StrategyHandler.class)
    public StrategyHandler strategyHandler() {
        return new StrategyHandler();
    }

}
