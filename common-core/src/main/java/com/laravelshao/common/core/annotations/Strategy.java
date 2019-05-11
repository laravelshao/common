package com.laravelshao.common.core.annotations;

import com.laravelshao.common.core.handler.StrategyHandler;

import java.lang.annotation.*;

/**
 * 策略注解
 *
 * @author shaoqinghua
 * @date 2019/5/11
 * @description
 * @see StrategyHandler
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Strategy {

    /**
     * 父类
     *
     * @return
     */
    Class<?> superClass();

    /**
     * 策略代码
     *
     * @return
     */
    String strategyCode() default "";

    /**
     * 描述信息
     *
     * @return
     */
    String description() default "";

}
