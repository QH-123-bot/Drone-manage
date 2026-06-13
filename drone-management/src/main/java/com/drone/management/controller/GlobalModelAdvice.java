/**
 * 无人机管理系统 — 全局模型增强类（GlobalModelAdvice.java）
 * <p>作用：表现层横切组件，为所有 Controller 返回的视图 Model 自动注入公共属性。</p>
 * <p>功能：从 Shiro 当前登录主体读取用户名，以 {@code loginName} 键注入 Model，供各页面导航栏显示当前登录用户。</p>
 */
package com.drone.management.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * <b>表现层横切：全局模型属性</b><br>
 * {@code @ControllerAdvice} 作用于所有 Controller 返回的 Model；此处为导航栏提供当前登录用户名。<br>
 * 对应模板：{@code drone/list.html}、{@code detail.html} 等中的 {@code loginName}。
 */
@ControllerAdvice // 与所有 @Controller 配合，在每次请求处理前可注入公共 Model 属性
public class GlobalModelAdvice {

    /**
     * 每个请求在渲染视图前执行；方法名可任意，返回值以 {@code loginName} 为键放入 Model。<br>
     * [表现层 → Shiro] 从 Subject 取 Principal（在 UserRealm 中设为 username 字符串）。
     */
    @ModelAttribute("loginName") // 等价于 model.addAttribute("loginName", returnValue)
    public String loginName() {
        Subject subject = SecurityUtils.getSubject(); // 当前 Shiro 主体
        if (subject != null && subject.isAuthenticated()) { // 已登录才显示名称
            Object p = subject.getPrincipal(); // UserRealm 返回的 SimpleAuthenticationInfo 主身份
            return p != null ? String.valueOf(p) : null;
        }
        return null; // 未登录，模板中 th:if 不显示
    }
}
