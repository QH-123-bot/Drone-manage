/**
 * 无人机管理系统 — 站点首页控制器（IndexController.java）
 * <p>作用：处理根路径 {@code /} 的访问，将用户引导至系统主业务页面。</p>
 * <p>功能：将浏览器访问首页的请求 302 重定向到 {@code /drones} 无人机列表；未登录用户由 Shiro 拦截并跳转登录页。</p>
 */
package com.drone.management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * <b>表现层：站点根路径重定向</b><br>
 * 无业务逻辑；将用户从 {@code /} 引导至无人机列表 {@code /drones}（未登录则由 Shiro 拦截去登录页）。
 */
@Controller
public class IndexController {

    @GetMapping("/") // 浏览器访问 http://host:port/
    public String home() {
        return "redirect:/drones"; // HTTP 302 到无人机模块，对应 DroneController 列表
    }
}
