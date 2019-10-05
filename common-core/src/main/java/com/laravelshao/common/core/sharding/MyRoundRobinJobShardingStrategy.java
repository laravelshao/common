package com.laravelshao.common.core.sharding;

import com.dangdang.ddframe.job.lite.api.strategy.JobInstance;
import com.dangdang.ddframe.job.lite.api.strategy.JobShardingStrategy;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 自定义轮询 elastic job 分片策略
 *
 * @author qinghua.shao
 * @date 2019/10/5
 * @since 1.0.0
 */
public class MyRoundRobinJobShardingStrategy implements JobShardingStrategy {

    @Override
    public Map<JobInstance, List<Integer>> sharding(List<JobInstance> jobInstances, String jobName, int shardingTotalCount) {

        Map<JobInstance, List<Integer>> resultMap = new HashMap<>(16);
        ArrayDeque<Integer> queue = new ArrayDeque<>(shardingTotalCount);
        for (int i = 0; i < shardingTotalCount; i++) {
            queue.add(i);
        }

        while (queue.size() > 0) {
            for (JobInstance jobInstance : jobInstances) {
                if (queue.size() > 0) {
                    Integer shardingItem = queue.pop();
                    List<Integer> shardingList = resultMap.get(jobInstance);
                    if (CollectionUtils.isEmpty(shardingList)) {
                        shardingList = new ArrayList<>();
                        shardingList.add(shardingItem);
                        resultMap.put(jobInstance, shardingList);
                    } else {
                        shardingList.add(shardingItem);
                    }
                }
            }
        }

        return resultMap;
    }
}
