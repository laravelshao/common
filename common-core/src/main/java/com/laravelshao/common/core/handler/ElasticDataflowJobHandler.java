package com.laravelshao.common.core.handler;

import com.dangdang.ddframe.job.api.ElasticJob;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.laravelshao.common.core.annotations.ElasticDataflowJob;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.Map;

/**
 * elastic dataflow job 处理器
 *
 * @author qinghua.shao
 * @date 2019/10/4
 * @since 1.0.0
 */
public class ElasticDataflowJobHandler implements InitializingBean, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    private CoordinatorRegistryCenter zookeeperRegistryCenter;

    @Autowired
    private DataSource dataSource;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() {

        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(ElasticDataflowJob.class);
        if (CollectionUtils.isEmpty(beansWithAnnotation)) {
            return;
        }

        for (Map.Entry<String, Object> entry : beansWithAnnotation.entrySet()) {
            Object instance = entry.getValue();
            // 判断是否实现DataflowJob接口 没有则不是定时任务 忽略
            Class<?>[] interfaces = instance.getClass().getInterfaces();
            if (interfaces.length == 0) {
                return;
            }

            for (Class<?> superInterface : interfaces) {
                if (superInterface != DataflowJob.class) {
                    continue;
                }

                ElasticDataflowJob elasticDataflowJob = instance.getClass().getAnnotation(ElasticDataflowJob.class);
                String jobName = elasticDataflowJob.jobName();
                String cron = elasticDataflowJob.cron();
                int shardingTotalCount = elasticDataflowJob.shardingTotalCount();
                boolean overwrite = elasticDataflowJob.overwrite();
                boolean streamingProcess = elasticDataflowJob.streamingProcess();
                Class<?> jobShardingStrategyClass = elasticDataflowJob.jobShardingStrategy();
                boolean isJobEventTrace = elasticDataflowJob.isJobEventTrace();

                // JOB核心配置
                JobCoreConfiguration jobCoreConfig = JobCoreConfiguration.newBuilder(jobName, cron, shardingTotalCount).build();
                // JOB类型配置
                JobTypeConfiguration dataflowJobConfig = new DataflowJobConfiguration(
                        jobCoreConfig, instance.getClass().getCanonicalName(), streamingProcess);
                // JOB根配置(设置是否覆盖配置、任务分片策略)
                LiteJobConfiguration liteJobConfig = LiteJobConfiguration.newBuilder(dataflowJobConfig)
                        .overwrite(overwrite).jobShardingStrategyClass(jobShardingStrategyClass.getCanonicalName()).build();

                // 初始化JOB任务
                //new JobScheduler(zookeeperRegistryCenter, liteJobConfig).init(); // spring整合存在npe，需使用SpringJobScheduler
                if (isJobEventTrace) {
                    JobEventConfiguration jobEventConfig = new JobEventRdbConfiguration(dataSource);
                    new SpringJobScheduler((ElasticJob) instance, zookeeperRegistryCenter, liteJobConfig, jobEventConfig).init();
                } else {
                    new SpringJobScheduler((ElasticJob) instance, zookeeperRegistryCenter, liteJobConfig).init();
                }
            }
        }
    }
}

