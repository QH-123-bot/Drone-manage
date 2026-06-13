/**
 * 无人机管理系统 — Apache Shiro 安全配置类（ShiroConfig.java）
 * <p>作用：安全横切配置，定义认证、授权与 URL 访问控制规则，保护除登录页外的业务资源。</p>
 * <p>功能：注册 {@code UserRealm}、{@code SecurityManager}、{@code ShiroFilterFactoryBean}；
 * 配置登录页 URL、匿名访问路径（/login、静态资源）、登出及全局需认证（{@code authc}）的过滤器链。</p>
 */
package com.drone.management.config;

import com.drone.management.constant.AuthPaths;
import com.drone.management.shiro.UserRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <b>基础设施层 / 安全横切（非业务三层，但拦截所有 HTTP）</b><br>
 * 注册 Shiro 的 Realm、SecurityManager、过滤器链；与 {@link com.drone.management.controller.LoginController} 路径配合。<br>
 * 对应依赖：{@code pom.xml} 中 shiro-spring。
 */
@Configuration // 本类中的 @Bean 方法结果注册为 Spring 容器单例
public class ShiroConfig {

    /** 提供账号密码校验与角色信息（内存演示账号）。 */
    @Bean
    public UserRealm userRealm() {
        return new UserRealm();
    }

    /** Shiro 核心：管理 Realm、会话等；供 Filter 使用。 */
    @Bean
    public SecurityManager securityManager(UserRealm userRealm) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(userRealm); // 注入自定义 Realm
        return manager;
    }

    /**
     * 声明 URL 与过滤器对应关系：/login 匿名，/** 需认证。<br>
     * {@code loginUrl} 必须与 LoginController 的 GET 路径一致（{@link AuthPaths#LOGIN}）。
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager);
        factoryBean.setLoginUrl(AuthPaths.LOGIN); // 未登录访问受保护 URL 时重定向到登录页
        factoryBean.setSuccessUrl("/drones"); // 登录成功默认落地页（部分场景使用）
        factoryBean.setUnauthorizedUrl(AuthPaths.LOGIN); // 无权限时跳转（本演示未细粒度鉴权）

        Map<String, String> chain = new LinkedHashMap<>(); // 顺序敏感：先匹配的规则先生效
        chain.put(AuthPaths.LOGIN, "anon"); // 登录页与登录 POST 同路径，均匿名可访问
        chain.put("/logout", "logout"); // Shiro 内置登出过滤器
        chain.put("/css/**", "anon"); // 静态资源（若本地化）
        chain.put("/js/**", "anon");
        chain.put("/favicon.ico", "anon");
        chain.put("/**", "authc"); // 其余全部需要已认证用户
        factoryBean.setFilterChainDefinitionMap(chain);
        return factoryBean;
    }
}
