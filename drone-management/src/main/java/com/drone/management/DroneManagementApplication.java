/**
 * 无人机管理系统 — 应用启动入口类（DroneManagementApplication.java）
 * <p>作用：作为整个 Spring Boot 应用的 JVM 入口，负责启动内嵌 Web 容器并扫描注册全部组件（Controller、Service、Mapper、配置类等）。</p>
 * <p>功能：在启动前创建 SQLite 数据库文件所在目录；通过 {@code @SpringBootApplication} 启用自动配置与组件扫描；
 * 通过 {@code @MapperScan} 将 MyBatis 的 Mapper 接口注册为 Spring Bean。</p>
 */
package com.drone.management;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * <b>应用启动入口（不属于三层业务，负责组装整个 Spring Boot 应用）</b><br>
 * {@code @SpringBootApplication} 内含 {@code @ComponentScan}，会扫描本包及子包下 Bean。<br>
 * {@code @MapperScan} 将 {@code com.drone.management.mapper} 注册为 MyBatis Mapper 接口。
 */
@SpringBootApplication // 启用自动配置、组件扫描；等价于 @Configuration + @EnableAutoConfiguration + @ComponentScan
@MapperScan("com.drone.management.mapper") // 为 DroneMapper 等接口生成 MyBatis 代理实现并注册为 Bean
public class DroneManagementApplication {

    /**
     * JVM 入口：先确保 SQLite 文件目录存在（与 application-sqlite.yml 中 jdbc:sqlite 路径配合），再启动 Spring。
     */
    public static void main(String[] args) throws IOException {
        Path dataDir = Paths.get(System.getProperty("user.dir"), "data"); // 工作目录下 data 文件夹
        Files.createDirectories(dataDir); // 不存在则创建，避免 JDBC 建库失败
        SpringApplication.run(DroneManagementApplication.class, args); // 启动内嵌 Tomcat 等全部组件
    }
}
