/**
 * 无人机管理系统 — HTTP 请求日志拦截器（RequestLogInterceptor.java）
 * <p>作用：表现层横切，在 Controller 方法执行前拦截请求，用于可观测性与调试。</p>
 * <p>功能：在 {@code preHandle} 中记录请求方法、URI、查询串、表单参数及客户端 IP 到 SLF4J 日志，然后放行继续处理。</p>
 */
package com.drone.management.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;

/**
 * <b>表现层横切：请求日志（在 Controller 之前执行）</b><br>
 * 实现 {@link HandlerInterceptor}，由 {@link com.drone.management.config.WebMvcConfig} 注册。<br>
 * 与业务三层无直接数据依赖，仅观察 HTTP 入参。
 */
@Component // 注册为 Spring Bean 供 WebMvcConfig 注入
public class RequestLogInterceptor implements HandlerInterceptor {

    /** SLF4J 日志记录器，输出到控制台/logback 配置。 */
    private static final Logger log = LoggerFactory.getLogger(RequestLogInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String method = request.getMethod(); // GET / POST 等
        String uri = request.getRequestURI(); // 含 context-path 之后的路径
        String query = request.getQueryString(); // URL ? 后查询串，可能为 null
        String remote = request.getRemoteAddr(); // 客户端 IP（经代理时可能为上一跳）
        String params = request.getParameterMap().entrySet().stream() // 表单与 query 参数合并视图
                .map(e -> e.getKey() + "=" + String.join(",", e.getValue()))
                .collect(Collectors.joining("&"));
        log.info("[HTTP] {} {} | query={} | params={} | remote={}", method, uri,
                query != null ? query : "-",
                params.isEmpty() ? "-" : params,
                remote);
        return true; // 返回 true 表示继续过滤器链与后续 Controller
    }
}
