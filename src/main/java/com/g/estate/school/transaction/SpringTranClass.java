package com.g.estate.school.transaction;

/**
 * 什么是事物 这里通常指的是 数据库的事务，
 *
 *
 *
 */
public class SpringTranClass {

    /*
    1. BeanPostProcessor
    processor 顾名思义，处理器
    BeanPostProcessor后置处理器,作为spring的顶级核心接口
    作用是在Bean对象在实例化和依赖注入完毕后,在显示调用初始化方法的前后添加我们自己的逻辑
    该接口的两个接口方法
    default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    为了理解spring 事务的原理，我们从注解处理器为入口
    BeanPostProcessor 的实现InfrastructureAdvisorAutoProxyCreator
    InfrastructureAdvisorAutoProxyCreator 父类 AbstractAdvisorAutoProxyCreator 的父类AbstractAutoProxyCreator
    对BeanPostProcessor进行了实现
    -----------------------------------------------------------------------------------------
    public Object postProcessAfterInitialization(@Nullable Object bean, String beanName) {
        if (bean != null) {
            Object cacheKey = this.getCacheKey(bean.getClass(), beanName);
            if (this.earlyProxyReferences.remove(cacheKey) != bean) {
                return this.wrapIfNecessary(bean, beanName, cacheKey);
            }
        }

        return bean;
    }
----------------------------------------------------------------------------------------
    ↑可以看出，除去相应的check后，核心代码是在wrapIfNecessary这
    protected Object wrapIfNecessary(Object bean, String beanName, Object cacheKey) {
        // targetSourcedBeans 有的话，则直接返回
        if (StringUtils.hasLength(beanName) && this.targetSourcedBeans.contains(beanName)) {
            return bean;
        // advisedBeans 中有cacheKey的话，并且返回值为false的时候，也直接返回
        } else if (Boolean.FALSE.equals(this.advisedBeans.get(cacheKey))) {
            return bean;
        // 提前把一些InfrastructureClass的实现类排除在，因为他们是属于spring的自己的东西。不需要去做这些处理
        } else if (!this.isInfrastructureClass(bean.getClass()) && !this.shouldSkip(bean.getClass(), beanName)) {
            // 找到这个类的增强方法，如果有的话，则往advisedBeans插入k/v cacheKey 和 true
            Object[] specificInterceptors = this.getAdvicesAndAdvisorsForBean(bean.getClass(), beanName, (TargetSource)null);
            if (specificInterceptors != DO_NOT_PROXY) {
                this.advisedBeans.put(cacheKey, Boolean.TRUE);
                // ※　获取代理对象
                Object proxy = this.createProxy(bean.getClass(), beanName, specificInterceptors, new SingletonTargetSource(bean));
                this.proxyTypes.put(cacheKey, proxy.getClass());
                return proxy;
            } else {
                this.advisedBeans.put(cacheKey, Boolean.FALSE);
                return bean;
            }
        } else {
            this.advisedBeans.put(cacheKey, Boolean.FALSE);
            return bean;
        }
    }

    protected boolean isInfrastructureClass(Class<?> beanClass) {
        // 通过类继承角度去判断，beanClass是否为了Advice Pointcut Advisor AopInfrastructureBean的实现类
        boolean retVal = Advice.class.isAssignableFrom(beanClass) || Pointcut.class.isAssignableFrom(beanClass) || Advisor.class.isAssignableFrom(beanClass) || AopInfrastructureBean.class.isAssignableFrom(beanClass);
        if (retVal && this.logger.isTraceEnabled()) {
            this.logger.trace("Did not attempt to auto-proxy infrastructure class [" + beanClass.getName() + "]");
        }

        return retVal;
    }

    @Nullable
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, @Nullable TargetSource targetSource) {
        List<Advisor> advisors = this.findEligibleAdvisors(beanClass, beanName);
        return advisors.isEmpty() ? DO_NOT_PROXY : advisors.toArray();
    }



     */

    public static void main(String[] args) {

    }


}
