package com.laravelshao.common.spring.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author qinghua.shao
 * @date 2019/10/4
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "elasticjob.zookeeper")
@Getter
@Setter
public class ElasticJobZookeeperProperties {

    /**
     * zookeeper服务列表
     */
    private String serverList;
    /**
     * elastic-job zookeeper 命名空间
     */
    private String namespace;

}
