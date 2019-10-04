package com.laravelshao.common.spring.config;

import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.laravelshao.common.spring.config.properties.ElasticJobZookeeperProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * elastic-job zookeeper 自动装配
 *
 * @author qinghua.shao
 * @date 2019/10/4
 * @since 1.0.0
 */
@Configuration
@ConditionalOnProperty("elasticjob.zookeeper.server-list")
@EnableConfigurationProperties(ElasticJobZookeeperProperties.class)
public class ElasticJobZookeeperAutoConfiguration {

    @Autowired
    private ElasticJobZookeeperProperties elasticJobZookeeperProperties;

    /**
     * zookeeper 注册中心
     *
     * @return
     */
    @Bean(initMethod = "init")
    public CoordinatorRegistryCenter zookeeperRegistryCenter() {

        String serverList = elasticJobZookeeperProperties.getServerList();
        String namespace = elasticJobZookeeperProperties.getNamespace();
        if (StringUtils.isEmpty(serverList) || StringUtils.isEmpty(namespace)) {
            throw new RuntimeException("[elastic-job zookeeper config error]");
        }

        ZookeeperConfiguration zkConfig = new ZookeeperConfiguration(serverList, namespace);
        // 可利用Bean注解的initMethod方法进行初始化
        //ZookeeperRegistryCenter zkRegistryCenter = new ZookeeperRegistryCenter(zkConfig);
        //zkRegistryCenter.init(); // 注册中心必须初始化
        return new ZookeeperRegistryCenter(zkConfig);
    }
}
