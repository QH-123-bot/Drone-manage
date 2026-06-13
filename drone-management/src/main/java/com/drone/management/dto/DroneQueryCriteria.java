/**
 * 无人机管理系统 — 条件查询数据传输对象（DroneQueryCriteria.java）
 * <p>作用：承载列表页「组合条件查询」表单参数，介于表现层与业务层之间，不直接映射整张表实体。</p>
 * <p>功能：封装 id、名称、型号、序列号、续航、速度、状态等查询字段；
 * 提供 {@code hasAnyNonBlankCriteria()} 判断用户是否至少填写一项条件，避免空查访问数据库。</p>
 */
package com.drone.management.dto;

import java.io.Serializable;

/**
 * <b>数据传输对象（DTO），介于表现层与业务层之间</b><br>
 * 用途：承载列表页「条件查询」表单提交的字段，不直接对应整张表的业务主实体。<br>
 * 对应表现层：{@code templates/drone/list.html} 中 {@code th:object="${query}"} 绑定。<br>
 * 对应业务层：{@link com.drone.management.service.DroneService#findByCriteria} 入参。<br>
 * 对应数据层：{@code mapper/DroneMapper.xml} 中 {@code findByCriteria} 的 {@code q.*} 参数。
 */
public class DroneQueryCriteria implements Serializable {

    /** 序列化版本号。 */
    private static final long serialVersionUID = 1L;

    /** 按主键精确查询时填写；与实体 {@link com.drone.management.entity.Drone#getId} 含义一致。 */
    private Long id;
    /** 按名称完全匹配；与实体 {@code name} 一致。 */
    private String name;
    /** 按型号完全匹配；与实体 {@code model} 一致。 */
    private String model;
    /** 按序列号完全匹配；与实体 {@code serialNumber} 一致。 */
    private String serialNumber;
    /** 按续航（分钟）精确匹配；与实体 {@code maxFlightTime} 一致。 */
    private Integer maxFlightTime;
    /** 按最大速度精确匹配；与实体 {@code maxSpeed} 一致。 */
    private Double maxSpeed;
    /** 按状态精确匹配；与实体 {@code status} 一致。 */
    private String status;

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

    /**
     * 判断用户是否至少填了一项可查条件（避免空查打库）。<br>
     * 表现层：{@link com.drone.management.controller.DroneController#query} 先调用本方法。<br>
     * 业务层：{@link com.drone.management.service.impl.DroneServiceImpl#findByCriteria} 再次校验。
     */
    public boolean hasAnyNonBlankCriteria() {
        // 数值型字段非空即视为有条件
        if (id != null || maxFlightTime != null || maxSpeed != null) {
            return true;
        }
        // 字符串需非空白
        return notBlank(name) || notBlank(model) || notBlank(serialNumber) || notBlank(status);
    }

    /** 内部工具：字符串去掉首尾空白后是否仍有内容。 */
    private static boolean notBlank(String s) {
        return s != null && !s.trim().isEmpty();
    }
}
