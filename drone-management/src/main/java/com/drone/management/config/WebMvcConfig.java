/**
 * 无人机管理系统 — Spring MVC 配置类（WebMvcConfig.java）
 * <p>作用：基础设施配置，扩展 Spring MVC 行为，将自定义拦截器挂入请求处理链。</p>
 * <p>功能：注册 {@link com.drone.management.interceptor.RequestLogInterceptor}，对所有路径（{@code /**}）在 Controller 执行前记录 HTTP 请求日志。</p>
 */
package com.drone.management.config;

import com.drone.management.interceptor.RequestLogInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <b>表现层配置：注册 Spring MVC 拦截器</b><br>
 * 将 {@link RequestLogInterceptor} 挂到所有路径，在 Controller 方法执行前后可插逻辑（此处仅 preHandle 打日志）。
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /** 请求日志拦截器 Bean，见 interceptor 包。 */
    private final RequestLogInterceptor requestLogInterceptor;

    public WebMvcConfig(RequestLogInterceptor requestLogInterceptor) {
        this.requestLogInterceptor = requestLogInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestLogInterceptor).addPathPatterns("/**"); // 匹配所有请求，含 /drones、/login
    }
}
