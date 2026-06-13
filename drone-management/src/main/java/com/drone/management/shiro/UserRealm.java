/**
 * 无人机管理系统 — Shiro 用户域（UserRealm.java）
 * <p>作用：安全层的认证与授权数据源，由 Shiro 在登录时调用以校验凭据并加载用户角色。</p>
 * <p>功能：{@code doGetAuthenticationInfo} 校验演示账号（admin/operator）用户名密码；
 * {@code doGetAuthorizationInfo} 为登录用户分配 user/admin 角色（本演示未做细粒度 URL 权限）。</p>
 */
package com.drone.management.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * <b>安全层 / Shiro Realm（认证 + 授权数据源）</b><br>
 * 由 {@link com.drone.management.config.ShiroConfig} 注册到 SecurityManager。<br>
 * 被调用链：LoginController → Subject.login → 本类 {@link #doGetAuthenticationInfo}。<br>
 * 与表现层模板无直接文件对应，与内存中的用户名密码硬编码对应（演示用）。
 */
public class UserRealm extends AuthorizingRealm {

    /**
     * 授权：根据已登录主体返回角色/权限（本演示仅角色字符串）。<br>
     * {@code principals}：含登录时放入的 username。
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) principals.getPrimaryPrincipal(); // 与认证时放入的 principal 一致
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRole("user"); // 所有登录用户具备 user 角色
        if ("admin".equals(username)) {
            info.addRole("admin"); // 演示：admin 多一个 admin 角色（本项目中未做 URL 级区分）
        }
        return info;
    }

    /**
     * 认证：校验用户名密码是否与演示账号一致。<br>
     * {@code token}：来自 UsernamePasswordToken，由 LoginController 构造。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken up = (UsernamePasswordToken) token;
        String username = up.getUsername();
        String password = new String(up.getPassword()); // char[] 转 String 与硬编码比较

        if (match("admin", "admin123", username, password)
                || match("operator", "operator123", username, password)) {
            return new SimpleAuthenticationInfo(username, password, getName()); // principal 供页面与授权使用
        }
        throw new IncorrectCredentialsException("用户名或密码错误"); // Shiro 转为登录失败
    }

    /** 工具：同时匹配用户名与明文密码（演示项目，生产应使用哈希与盐）。 */
    private static boolean match(String u, String p, String username, String password) {
        return u.equals(username) && p.equals(password);
    }
}
