package com.g.estate.school.spring;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanFactory;

import java.util.List;

public class StartupClass {
    /*

        Spring ioc 中最重要的类：
        AbstractAutowireCapableBeanFactory 这个类负责 bean创建，后置处理器BeanPostProcessor

        doCreteBean 方法开始负责单个bean创建。
        initializeBean
        会调用applyBeanPostProcessorsAfterInitialization和applyBeanPostProcessorsBeforeInitialization方法
	    BeanPostProcessors to apply.
//      private final List<BeanPostProcessor> beanPostProcessors = new AbstractBeanFactory.BeanPostProcessorCacheAwareList();



     */
}
