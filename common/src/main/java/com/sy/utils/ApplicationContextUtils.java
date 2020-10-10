package com.sy.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring容器工具类，工具类实现了Spring容器的感知器对象
 * 在项目中有些地方，例如：执行扩展JpaRepository方法实现以及SpringSecurity配置中数据源的获取
 * 无法通过自动注入完成，此时在可以通过感知器对象完成
 */
@Component
public class ApplicationContextUtils implements ApplicationContextAware {

    /**
     * 用于存放回调方法中得到的感知对象
     */
    private static ApplicationContext ac;

    /**
     * 私有构造，防止外部直接实例化
     */
    private ApplicationContextUtils() {

    }

    /**
     * 当Spring容器创建完毕以后会在该方法中得到容器对象
     *
     * @param applicationContext 创建后容器对象
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ac = applicationContext;
    }

    /**
     * 在该工具方法中可以根据类型来获取Bean对象
     *
     * @param clazz Bean的类型
     * @param <T>   泛型参数
     * @return 根据类型获取到的Bean对象
     */

    public static <T> T getBean(Class<T> clazz) {
        return ac.getBean(clazz);
    }
}
