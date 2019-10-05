package com.laravelshao.common.core.annotations;

import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import com.dangdang.ddframe.job.lite.api.strategy.JobShardingStrategy;
import com.dangdang.ddframe.job.lite.api.strategy.impl.AverageAllocationJobShardingStrategy;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * elastic simple job 注解
 *
 * @author qinghua.shao
 * @date 2019/10/4
 * @since 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface ElasticSimpleJob {

    /**
     * 任务名称
     */
    String jobName() default "";

    /**
     * cron 表达式
     */
    String cron() default "";

    /**
     * 总分片数
     */
    int shardingTotalCount() default 1;

    /**
     * 是否覆盖配置
     */
    boolean overwrite() default false;

    /**
     * 任务分片策略
     */
    Class<? extends JobShardingStrategy> jobShardingStrategy() default AverageAllocationJobShardingStrategy.class;

    /**
     * 是否开启任务事件追踪
     */
    boolean isJobEventTrace() default false;

    /**
     * 作业监听器
     */
    Class<? extends ElasticJobListener>[] jobListeners() default {};
}
