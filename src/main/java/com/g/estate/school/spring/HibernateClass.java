package com.g.estate.school.spring;

public class HibernateClass {
    /*
    Hibernate 关系型数据库框架
    Hibernate分为三级缓存，一级缓存为 session キャッシュ：
    默认是开启的，再一次事务中，第一次发起请求回去数据库查询，将查询的结果放在一级缓存中，这个查询结果是数据库对象POJO的形式保存的。
    而且使用了懒加载技术，session.load方法 本身就是需要的时候才会去数据库查询。
    如果，当在这个事务内第二次sql的时候session会在缓存中查找结果，而不需要通过数据库查询。
    优点： 是可以减少数据库的查询次数，
    缺点，在批量操作中容易产生内存溢出问题。
     */
    /*
    二级缓存：sessionFactory的缓存的缓存，默认不会开启。
    开启二级缓存需要配置ehcache.xml， アノテーションのやり方は@Cache(usage=CacheconcurrencyStrategy.READ_ONLY)
    表示开启二级缓存，二级缓存是生命周期通常是整个应用，不同session的相同查询只进行一次，如果两次相同的情况下。。
     */
    /*
    三级缓存：查询缓存hibernate.cache.use_query_cache=true开启，查询需要声明，一级二级查询都是对单独的查询对象进行缓存，如果要对结果集缓存，
    就需要开启三级缓存，也就是查询缓存。
    用于两条query的HQL是一样的，存的是一个list，但其中的一个成员发生改变的时候
    会导致整个list的改变，效率低。
    只适用于不能改变的list

     */
    public static void main(String[] args) {

    }
}
