package com.laravelshao.common.core.handler;

import com.dangdang.ddframe.job.api.ElasticJob;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.laravelshao.common.core.annotations.ElasticSimpleJob;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * elastic simple job 处理器
 *
 * @author qinghua.shao
 * @date 2019/10/4
 * @since 1.0.0
 */
public class ElasticSimpleJobHandler implements InitializingBean, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    private CoordinatorRegistryCenter zookeeperRegistryCenter;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() {

        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(ElasticSimpleJob.class);
        if (CollectionUtils.isEmpty(beansWithAnnotation)) {
            return;
        }

        for (Map.Entry<String, Object> entry : beansWithAnnotation.entrySet()) {
            Object instance = entry.getValue();
            // 判断是否实现SimpleJob接口 没有则不是定时任务 忽略
            Class<?>[] interfaces = instance.getClass().getInterfaces();
            if (interfaces.length == 0) {
                return;
            }

            for (Class<?> superInterface : interfaces) {
                if (superInterface != SimpleJob.class) {
                    continue;
                }

                ElasticSimpleJob elasticSimpleJob = instance.getClass().getAnnotation(ElasticSimpleJob.class);
                String jobName = elasticSimpleJob.jobName();
                String cron = elasticSimpleJob.cron();
                int shardingTotalCount = elasticSimpleJob.shardingTotalCount();
                boolean overwrite = elasticSimpleJob.overwrite();

                // JOB核心配置
                JobCoreConfiguration jobCoreConfig = JobCoreConfiguration.newBuilder(jobName, cron, shardingTotalCount).build();
                // JOB类型配置
                JobTypeConfiguration simpleJobConfig = new SimpleJobConfiguration(jobCoreConfig, instance.getClass().getCanonicalName());
                // JOB根配置
                LiteJobConfiguration liteJobConfig = LiteJobConfiguration.newBuilder(simpleJobConfig).overwrite(overwrite).build();

                // 初始化JOB任务
                //new JobScheduler(zookeeperRegistryCenter, liteJobConfig).init(); // spring整合存在npe，需使用SpringJobScheduler
                new SpringJobScheduler((ElasticJob) instance, zookeeperRegistryCenter, liteJobConfig).init();
            }
        }
    }
}
