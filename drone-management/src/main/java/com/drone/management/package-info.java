/**
 * 无人机管理系统 — 根包说明文件（package-info.java）
 * <p>作用：描述项目整体包结构与三层架构划分，供开发者与 JavaDoc 工具阅读，不参与运行时逻辑。</p>
 * <p>功能：说明表现层（controller + templates）、业务层（service）、数据访问层（mapper + XML）各包职责及横切模块（config、shiro、interceptor）的关系。</p>
 * <p>无人机管理系统根包。
 * <p><b>三层架构在本项目中的划分与职责总结</b></p>
 * <ul>
 *   <li><b>表现层（Web / Controller）</b>：{@code com.drone.management.controller}、
 *       {@code templates/*.html}。接收浏览器 HTTP 请求，调用业务层，把结果放入 {@link org.springframework.ui.Model}
 *       或重定向，由 Thymeleaf 渲染页面。对应用户能直接访问的 URL 与表单。</li>
 *   <li><b>业务层（Service）</b>：{@code com.drone.management.service} 与 {@code service.impl}。
 *       封装业务规则（如查询前校验、保存时补时间戳、事务边界），只依赖 Mapper 接口，不直接写 SQL。
 *       与表现层通过接口解耦；与数据层通过 {@code DroneMapper} 交互。</li>
 *   <li><b>数据访问层（Mapper / MyBatis）</b>：{@code com.drone.management.mapper} 与
 *       {@code resources/mapper/DroneMapper.xml}。定义持久化操作与 SQL，把表行映射为实体
 *       {@link com.drone.management.entity.Drone}。与业务层一一对应：接口方法名与 XML 中 {@code id} 一致。</li>
 * </ul>
 * <p>此外：{@code entity} 为与表结构对应的领域模型；{@code dto} 为查询等专用传参对象；
 * {@code config}、{@code shiro}、{@code interceptor} 为横切能力（安全、MVC、日志），贯穿请求链路。</p>
 */
package com.drone.management;
