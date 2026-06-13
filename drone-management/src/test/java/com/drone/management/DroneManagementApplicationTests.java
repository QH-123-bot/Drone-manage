/**
 * 无人机管理系统 — 应用上下文集成测试（DroneManagementApplicationTests.java）
 * <p>作用：测试模块中的冒烟测试，验证 Spring Boot 应用能否在 sqlite 配置下正常启动并加载全部 Bean。</p>
 * <p>功能：使用 {@code @SpringBootTest} 启动完整应用上下文，{@code contextLoads()} 断言上下文加载成功（无业务断言）。</p>
 */
package com.drone.management;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("sqlite")
public class DroneManagementApplicationTests {

    @Test
    public void contextLoads() {
    }
}
