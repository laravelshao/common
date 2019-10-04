package com.laravelshao.common.spring.config;

import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.laravelshao.common.core.handler.ElasticSimpleJobHandler;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * elastic simple job 处理器自动装配
 *
 * @author qinghua.shao
 * @date 2019/10/4
 * @since 1.0.0
 */
@Configuration
@ConditionalOnBean(CoordinatorRegistryCenter.class)
@AutoConfigureAfter(ElasticJobZookeeperAutoConfiguration.class)
public class ElasticSimpleJobHandlerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ElasticSimpleJobHandler.class)
    public ElasticSimpleJobHandler elasticSimpleJobHandler() {
        return new ElasticSimpleJobHandler();
    }
}
