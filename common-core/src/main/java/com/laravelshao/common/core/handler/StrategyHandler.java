package com.laravelshao.common.core.handler;

import com.laravelshao.common.core.annotations.Strategy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.StringUtils.join;

/**
 * 策略注解处理器
 *
 * @author shaoqinghua
 * @date 2019/5/11
 * @description
 * @see Strategy
 * @see InitializingBean
 * @see ApplicationContextAware
 */
public class StrategyHandler implements InitializingBean, ApplicationContextAware {

    private ApplicationContext applicationContext;

    /**
     * 策略实现容器
     */
    private static final Map<String, Object> STRATEGY_BEAN_MAP = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() {

        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(Strategy.class);
        for (Map.Entry<String, Object> entry : beansWithAnnotation.entrySet()) {

            Object object = entry.getValue();
            Class<?> clazz = object.getClass();
            String simpleName = clazz.getSimpleName().toLowerCase();
            Strategy strategy = clazz.getAnnotation(Strategy.class);
            Class<?> superClass = strategy.superClass();
            String strategyCode = strategy.strategyCode();
            String alias = superClass.getSimpleName() + strategyCode;

            STRATEGY_BEAN_MAP.put(simpleName, object);
            STRATEGY_BEAN_MAP.put(alias, object);
        }
    }

    /**
     * 根据bean name获取bean
     *
     * @param beanName
     * @return
     */
    public static Object getBeanByName(String beanName) {
        Object bean = requireNonNull(STRATEGY_BEAN_MAP.get(beanName), "未查询到指定名称的Bean [" + beanName + "]");
        return bean;
    }

    public static <T> T getBean(Class<T> clazz, String... parameters) {
        requireNonNull(clazz, "请使用有效的Class[" + clazz + "]");
        return (T) getBeanByName(join(clazz.getSimpleName(), join(parameters)));
    }

}
