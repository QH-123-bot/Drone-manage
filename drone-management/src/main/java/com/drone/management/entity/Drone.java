/**
 * 无人机管理系统 — 无人机领域实体类（Drone.java）
 * <p>作用：作为与数据库表 {@code drone} 一一对应的领域模型，在表现层、业务层、数据层之间传递无人机业务数据。</p>
 * <p>功能：定义无人机主键、名称、型号、序列号、续航、速度、状态、备注及创建/更新时间等字段；
 * 提供 JSR-303 校验注解（如名称非空、长度限制），供表单绑定与 {@code @Valid} 校验使用。</p>
 */
package com.drone.management.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * <b>领域实体 / 模型层（与表 drone 对应，非严格三层中的独立一层，供各层传递）</b><br>
 * 对应数据库表：由 {@code schema-sqlite.sql} / {@code schema-mysql.sql} 定义。<br>
 * 对应 MyBatis：{@code mapper/DroneMapper.xml} 中 {@code resultMap} 将列映射到本类属性。<br>
 * 对应表现层：{@code templates/drone/form.html}、{@code list.html}、{@code detail.html} 通过 Thymeleaf 绑定字段。
 */
public class Drone implements Serializable {

    /** 序列化版本号：保证对象序列化后类结构兼容。 */
    private static final long serialVersionUID = 1L;

    /** 主键；数据库自增；新增时为空，更新/删除时必填。对应列 {@code id}。 */
    private Long id;

    /** 无人机名称；必填；最大 128 字符。对应列 {@code name}。 */
    @NotBlank(message = "名称不能为空")
    @Size(max = 128, message = "名称长度不能超过128")
    private String name;

    /** 型号；可选；最大 128 字符。对应列 {@code model}。 */
    @Size(max = 128)
    private String model;

    /** 序列号；可选；最大 64 字符。对应列 {@code serial_number}（MyBatis 驼峰映射）。 */
    @Size(max = 64)
    private String serialNumber;

    /** 最大续航（分钟）；可选。对应列 {@code max_flight_time}。 */
    private Integer maxFlightTime;

    /** 最大速度（km/h）；可选。对应列 {@code max_speed}。 */
    private Double maxSpeed;

    /** 状态：如 ONLINE / OFFLINE / MAINTENANCE。对应列 {@code status}。 */
    private String status;

    /** 备注；可选；最大 512 字符。对应列 {@code remark}。 */
    @Size(max = 512)
    private String remark;

    /** 创建时间（毫秒时间戳）；插入时由业务层填充。对应列 {@code created_at}。 */
    private Long createdAt;

    /** 最后更新时间（毫秒时间戳）；插入/更新时由业务层维护。对应列 {@code updated_at}。 */
    private Long updatedAt;

    // ---------- 以下为 JavaBean 访问器：供 MyBatis、Thymeleaf、JSON 等通过属性名读写，无额外业务逻辑 ----------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Integer getMaxFlightTime() {
        return maxFlightTime;
    }

    public void setMaxFlightTime(Integer maxFlightTime) {
        this.maxFlightTime = maxFlightTime;
    }

    public Double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
