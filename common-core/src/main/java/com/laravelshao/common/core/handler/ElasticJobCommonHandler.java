package com.laravelshao.common.core.handler;

import com.dangdang.ddframe.job.api.ElasticJob;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;

/**
 * elastic job 公共逻辑处理器
 *
 * @author qinghua.shao
 * @date 2019/10/5
 * @since 1.0.0
 */
public class ElasticJobCommonHandler {

    /**
     * 处理作业监听器
     *
     * @param jobListeners 配置的作业监听器Class数组
     * @return 作业监听器实例数组
     */
    public ElasticJobListener[] handleJobListeners(Class<? extends ElasticJobListener>[] jobListeners)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        ElasticJobListener[] listenerInstances;
        if (jobListeners != null && jobListeners.length > 0) {
            listenerInstances = new ElasticJobListener[jobListeners.length];
            int i = 0;
            for (Class<? extends ElasticJobListener> listener : jobListeners) {
                listenerInstances[i] = listener.getDeclaredConstructor().newInstance();
                i++;
            }
        } else {
            listenerInstances = new ElasticJobListener[0];
        }

        return listenerInstances;
    }

    /**
     * 初始化Spring作业调度器
     *
     * @param instance                作业实例
     * @param zookeeperRegistryCenter zookeeper注册中心
     * @param liteJobConfig           JOB根配置
     * @param isJobEventTrace         是否开启作业事件追踪
     * @param dataSource              数据源
     * @param listenerInstances       作业监听器实例数组
     */
    public void initSpringJobScheduler(Object instance, CoordinatorRegistryCenter zookeeperRegistryCenter,
                                       LiteJobConfiguration liteJobConfig, boolean isJobEventTrace,
                                       DataSource dataSource, ElasticJobListener[] listenerInstances) {

        // 初始化JOB任务
        //new JobScheduler(zookeeperRegistryCenter, liteJobConfig).init(); // spring整合存在npe，需使用SpringJobScheduler
        if (isJobEventTrace) {
            JobEventConfiguration jobEventConfig = new JobEventRdbConfiguration(dataSource);
            new SpringJobScheduler((ElasticJob) instance, zookeeperRegistryCenter, liteJobConfig, jobEventConfig, listenerInstances).init();
        } else {
            new SpringJobScheduler((ElasticJob) instance, zookeeperRegistryCenter, liteJobConfig, listenerInstances).init();
        }
    }
}
