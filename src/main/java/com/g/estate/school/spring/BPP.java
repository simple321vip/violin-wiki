package com.g.estate.school.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class BPP implements BeanPostProcessor {

    /*
    BeanPostProcessor spring中很重要的东西，
    spring ioc 和 aop的 连接器 不为过。
    通过阅读源码 我们可以知道


     */

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equals("bPP")) {
            System.out.println("after instance created ! postProcessor before");
        }

//
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equals("bPP")) {
            System.out.println("after instance created ! postProcessor after");
        }
        return bean;
    }
}
