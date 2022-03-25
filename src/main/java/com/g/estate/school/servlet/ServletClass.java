package com.g.estate.school.servlet;

import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;
import java.io.IOException;

/**
 * 什么是servlet，
 * servlet 狭义上就是servlet接口，广义上是实现了servlet这个接口的实现类。
 * servlet的生命周期
 * servlet有init service destroy等主要方法，
 * 一个Servlet
 *      从类加载，到实例化，
 *      init方法初始化，
 *      service方法提供服务，
 *      到销毁阶段destroy方法，可以分为4个阶段。
 * servlet的实现是离不开web服务器的。比如说tomcat，他就是servlet的服务器。
 * tomcat默认会监听8080端口的请求，当接收到一个http请求后，tomcat会开启一个线程，并将http请求封装成一个ServletRequest，
 * 并根据url映射将servlet请给委托给被映射的servlet处理service方法去处理。
 * 实现类HttpServlet，对service进行了重写， 获取ServletRequest，根据http请求的类型（delete，post，put，get，HEAD， OPTIONS， trace，CONNECT），
 * 调用相应的doGet，doPost，doPut等方法，进行服务处理，并将响应信息封装进入到httpResponse交由web服务器对请求进行相应。
 *
 * 而 SpringMVC 框架是在Servlet的基础上实现的。
 * DispatcherServlet 继承了httpServlet，将所有请求映射到DispatcherServlet中，
 * 在service方法内，对url进行解析，通过HandlerMapping将请求映射到Controller的方法上，
 * 所以Controller是单例且线程不安全的，但是数据通过方法直接传进的，并不出现线程问题。
 * 方法参数不存在线程不安全问题，只有成员变量才会有这个问题。
 * 关于MVC的相应问题，如果是前端模板，SPA的话，遵循restFul的话，我们只需要将数据以josn的方式返回就可以。
 * 如果是后端模板，我们需要将数据即model和试图view结合交给ViewResoler生成html返回给前端。
 *
 */
public class ServletClass {
    public static void main(String[] args) {
        /**
         * Servlet
         */
        Servlet servlet = new Servlet() {
            public void init(ServletConfig servletConfig) throws ServletException {

            }

            public ServletConfig getServletConfig() {
                return null;
            }

            public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {

            }

            public String getServletInfo() {
                return null;
            }

            public void destroy() {

            }
        };

        DispatcherServlet servlet1 = new DispatcherServlet();
    }
}
