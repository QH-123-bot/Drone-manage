/**
 * 无人机管理系统 — 登录控制器（LoginController.java）
 * <p>作用：表现层中负责用户认证相关页面与表单提交，是访问受保护业务页（如无人机列表）前的入口。</p>
 * <p>功能：展示登录页（GET {@code /login}）；接收用户名密码并调用 Shiro {@code Subject.login} 完成认证；
 * 登录成功跳转无人机列表，失败则 flash 错误信息并重定向回登录页。</p>
 */
package com.drone.management.controller;

import com.drone.management.constant.AuthPaths;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * <b>表现层：登录页 GET 与登录提交 POST</b><br>
 * 路径常量 {@link AuthPaths#LOGIN} 与 {@code ShiroConfig} 中 loginUrl、anon 规则一致。<br>
 * 认证逻辑委托 Shiro {@link Subject#login}；对应模板 {@code templates/login.html}。
 */
@Controller
public class LoginController {

    /**
     * 展示登录表单；{@code produces} 限定为 HTML，避免与 REST 歧义。<br>
     * {@code error} 查询参数：Shiro 登录失败重定向时可能携带，用于提示用户。
     */
    @GetMapping(value = AuthPaths.LOGIN, produces = MediaType.TEXT_HTML_VALUE)
    public String loginPage(Model model,
                            @RequestParam(value = "error", required = false) String errorFlag) {
        if (errorFlag != null) {
            model.addAttribute("error", "登录失败，请检查用户名和密码"); // 供 login.html 中 th:if 显示
        }
        return "login"; // → templates/login.html
    }

    /**
     * 处理用户名密码表单；参数非必填防止空提交导致 Spring 400，在方法内校验。<br>
     * [表现层 → Shiro] 成功后重定向到无人机列表（受保护资源）。
     */
    @PostMapping(AuthPaths.LOGIN)
    public String doLogin(@RequestParam(value = "username", required = false) String username,
                          @RequestParam(value = "password", required = false) String password,
                          RedirectAttributes ra) {
        String u = username != null ? username.trim() : ""; // 去首尾空白
        String p = password != null ? password : "";
        if (u.isEmpty() || p.isEmpty()) {
            ra.addFlashAttribute("error", "请输入用户名和密码");
            return "redirect:" + AuthPaths.LOGIN;
        }

        Subject subject = SecurityUtils.getSubject(); // 当前线程绑定的 Shiro 用户会话
        try {
            subject.login(new UsernamePasswordToken(u, p)); // 交给 Realm 校验，见 UserRealm
            return "redirect:/drones"; // 登录成功进入业务首页
        } catch (AuthenticationException ex) {
            ra.addFlashAttribute("error", "用户名或密码错误");
            return "redirect:" + AuthPaths.LOGIN;
        }
    }
}
