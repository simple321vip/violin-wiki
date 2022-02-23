package com.g.estate.school.transaction;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.Unsafe;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class SpringTranClass1 {

    /*
    看了一条Transaction 包的类，了解了以前不知道的东西，包括设计。也在思索如何记一下笔记，毕竟源码的笔记其实不是很好写。
    其实源码的笔记 可以通过 映射来写，把最主要的实现和他的顶级接口连接起来，就像下面这样。
    以下の４つクラスは、Transaction のAOP実現

    ReflectiveMethodInvocation ⇒　Joinpoint
    TransactionInterceptor　⇒　Advice
    TransactionAttributeSourceAdvisor　⇒　Advisor
    TransactionAttributeSourcePointcut　⇒　Pointcut

    Method ⇒　AnnotatedElement　アノテーション
    AnnotationAttributes　⇒　LinkedHashMap
    DefaultTransactionAttribute ⇒　TransactionAttribute


    可以看出
    一个advisor 维护了一个 Advice 和 Pointcut
    Advice 维护了一个 TransactionManager 和 source
    Advice的父类support 代码量很大，可以说是个中心枢纽类
    support 维护了一个 beanFactory
    support 维护了一个transactionManagerBeanName 用来去从beanFactory获取transactionManager或者set
    support 维护了一个transactionManager
    support 维护了一个source（和advice维护的是一个source）
    Advice 虽然维护了invoke方法，但还是调用了invokeWithinTransaction方法，

    主要的解析处理逻辑都在这个invokeWithinTransaction方法里。

    我们可以看到 这种设计方式，将自己和spring aop和spring bean 连接了起来。其实我本来想看spring事务传播的7种方式的是在做的结果。。。到现在还没找到。。

    下面是具体的源码记录把，可有可无的写了些。

    AnnotationTransactionAttributeSource　の中身はSet<TransactionAnnotationParser> annotationParsersを維持している
    TransactionAnnotationParserはトランザクションアノテーションを解析している
    ejbとjtaがいなかった場合はSpringTransactionAnnotationParserを一つだけを持っている。

     */

    public static void main(String[] args) {

//        Unsafe unsafe = sun.misc.Unsafe.getUnsafe();

        SpringTranClass1 c = new SpringTranClass1();

        Class<SpringTranClass1> clazz = SpringTranClass1.class;
        try {
            Method hasAnnotion = clazz.getMethod("hasAnnotion");
            Method hasNotAnnotion = clazz.getMethod("hasNotAnnotion");
//            Annotation annotation = method.getDeclaredAnnotation(Transactional.class);
            AnnotationUtils.findAnnotation(hasAnnotion, Transactional.class);
            AnnotationUtils.findAnnotation(hasNotAnnotion, Transactional.class);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }



    }

    @Transactional
    public void hasAnnotion() {


    }

    @Transactional
    public void hasNotAnnotion() {


    }

}
