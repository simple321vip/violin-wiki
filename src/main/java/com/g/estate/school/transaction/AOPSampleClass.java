package com.g.estate.school.transaction;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@ComponentScan()
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AOPSampleClass {


    /*
        AOP Aspect Oriented Programming 面向切面编程

          专业术语：どこからコピーされた
            ・通知（Advice） 通知（Advice）: AOP 框架中的增强处理。通知描述了切面何时执行以及如何执行增强处理。
            ・连接点（join point）: 连接点表示应用执行过程中能够插入切面的一个点，这个点可以是方法的调用、异常的抛出。
              在 Spring AOP 中，连接点总是方法的调用。
            ・切点（PointCut）: 可以插入增强处理的连接点。
            ・切面（Aspect）: 切面是通知和切点的结合。
            ・引入（Introduction）：引入允许我们向现有的类添加新的方法或者属性。
            ・织入（Weaving）: 将增强处理添加到目标对象中，并创建一个被增强的对象，这个过程就是织入。
            可以看初步理解：
            |￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣|
            | |￣￣￣￣￣|               |￣￣￣￣￣|   |
            | |  advice |  join point   | pointCut|   |
            | |_________|   aspect      |_________|   |
            |_________________________________________|
     */


    /*
    从网上找了一个小例子 来测试一下
    注明来源： https://www.cnblogs.com/joy99/p/10941543.html
     */

    interface IBuy {
        String buy();
    }

    interface Sleep {
        String sleep(double time);
    }

    // 可以看到，内部类是可以被扫描到的，但是必须是静态的。最好给定名称，要不getBean的时候，需要把父类的信息加上,并且首字母不需要小写了
    @Component
    static class Boy implements IBuy {
        @Override
        public String buy() {
            System.out.println("男孩买了一个游戏机");
            return "游戏机";
        }
    }

    @Component("girl")
    static class Girl implements IBuy {
        @Override
        public String buy() {
            System.out.println("女孩买了一件漂亮的衣服");
            return "衣服";
        }
    }

    @Component()
    static class Dog implements Sleep {
        @Override
        public String sleep(double time) {
            System.out.println(String.format("这只狗睡了%s个小时", time));
            return "小时";
        }
    }

    @Aspect
    @Component
    static class BuyAspectJ {
        @Before("execution(* com.g.estate.school.transaction.AOPSampleClass.IBuy.buy(..))")
        public void Before(){
            System.out.println("before.........");
        }
        @After("execution(* com.g.estate.school.transaction.AOPSampleClass.IBuy.buy(..))")
        public void After(){
            System.out.println("after.........");
        }
        @AfterReturning("execution(* com.g.estate.school.transaction.AOPSampleClass.IBuy.buy(..))")
        public void AfterReturning(){
            System.out.println("afterReturning.........");
        }
        @AfterThrowing("execution(* com.g.estate.school.transaction.AOPSampleClass.IBuy.buy(..))")
        public void AfterThrowing(){
            System.out.println("afterThrowing.........");
        }
        @Around("execution(* com.g.estate.school.transaction.AOPSampleClass.IBuy.buy(..))")
        public void Around(ProceedingJoinPoint pj){
        // 必须有ProceedingJoinPoint pj 参数，不写或者后面没有调用process方法，就会堵塞
            try {
                System.out.println("around..............begin");
//                pj.proceed();
                System.out.println("around..............end");
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        // ----------------------------------------------------Dog advice
//        @Pointcut("execution(* com.g.estate.school.transaction.AOPSampleClass.Sleep.sleep(..))")
//        public void point(){
//            System.out.println("i am oooooooooooooooo");
//        }
//
//        @Before("point()")
//        public void before() {
//            System.out.println("Before ...");
//        }
//
//        @After("point()")
//        public void after() {
//            System.out.println("After ...");
//        }

//        @After("ccccc()")
//        public void addddfter() {
//            System.out.println("After ...");
//        }

    }

    // Spring AOP 中的 5种通知 @Before @After @AfterReturning @AfterThrowing @Around

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AOPSampleClass.class);
//        System.out.println(context.getBeanDefinitionNames().length);
//        for (int i = 0; i < context.getBeanDefinitionNames().length; i++) {
//            System.out.println(context.getBeanDefinitionNames()[i]);
//        }
//        9
//        org.springframework.context.annotation.internalConfigurationAnnotationProcessor
//        org.springframework.context.annotation.internalAutowiredAnnotationProcessor
//        org.springframework.context.annotation.internalCommonAnnotationProcessor
//        org.springframework.context.annotation.internalPersistenceAnnotationProcessor
//        org.springframework.context.event.internalEventListenerProcessor
//        org.springframework.context.event.internalEventListenerFactory
//        AOPClass
//        AOPClass.Boy
        // girl

        Boy boy = context.getBean("AOPSampleClass.Boy", Boy.class);
//        Girl girl = context.getBean("girl", Girl.class);
        boy.buy();
//        System.out.println("");
//        System.out.println("--^^---^^^-^-^-^-^-^-^-^-^-^-^");
//        System.out.println("");
//        girl.buy();
//        System.out.println("");
//        System.out.println("--^^---^^^-^-^-^-^-^-^-^-^-^-^");
//        System.out.println("");

//        Dog dog = context.getBean("AOPSampleClass.Dog", Dog.class);
//        dog.sleep(1);

    }
}
