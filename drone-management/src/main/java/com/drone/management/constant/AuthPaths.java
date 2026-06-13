/**
 * 无人机管理系统 — 认证相关路径常量类（AuthPaths.java）
 * <p>作用：集中定义登录等安全相关 URL 路径，避免 Shiro 配置、Controller 与模板中出现不一致的魔法字符串。</p>
 * <p>功能：提供 {@code LOGIN = "/login"} 常量，供 {@code ShiroConfig}、{@code LoginController} 及登录页表单 action 统一引用。</p>
 */
package com.drone.management.constant;

/**
 * <b>常量层：跨模块共享的路径，避免魔法字符串不一致</b><br>
 * 使用者：{@link com.drone.management.config.ShiroConfig}、{@link com.drone.management.controller.LoginController}。<br>
 * 与模板关系：登录表单 {@code th:action="@{/login}"} 须与此处 {@link #LOGIN} 相同。
 */
public final class AuthPaths {

    /** 登录页 URL 路径片段，与 Spring MVC 映射、Shiro loginUrl 一致。 */
    public static final String LOGIN = "/login";

    /** 工具类禁止实例化。 */
    private AuthPaths() {
    }
}
