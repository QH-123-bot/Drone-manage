/**
 * 无人机管理系统 — 无人机业务服务接口（DroneService.java）
 * <p>作用：业务层对外 API，表现层 Controller 只依赖本接口，与数据层及实现类解耦。</p>
 * <p>功能：声明无人机的列表查询、按 ID 查询、条件查询、新增、修改、删除等业务操作；
 * 由 {@link com.drone.management.service.impl.DroneServiceImpl} 实现。</p>
 */
package com.drone.management.service;

import com.drone.management.dto.DroneQueryCriteria;
import com.drone.management.entity.Drone;

import java.util.List;

/**
 * <b>业务层接口（Service API）</b><br>
 * 表现层 {@link com.drone.management.controller.DroneController} 只依赖本接口，不依赖 Mapper。<br>
 * 实现类：{@link com.drone.management.service.impl.DroneServiceImpl}（事务与规则在此集中）。
 */
public interface DroneService {

    /** 查询全部无人机；实现类转调 {@code DroneMapper.findAll}。 */
    List<Drone> listAll();

    /** 按 ID 查一条；实现类转调 {@code DroneMapper.findById}。 */
    Drone getById(Long id);

    /**
     * 按组合条件查询列表（已填条件 AND）；无有效条件时实现类返回空列表。<br>
     * 对应 Mapper：{@code findByCriteria}；对应页面：条件查询结果表。
     */
    List<Drone> findByCriteria(DroneQueryCriteria criteria);

    /** 新增；实现类补时间戳并 {@code insert}。 */
    void save(Drone drone);

    /** 修改；实现类更新 {@code updatedAt} 并 {@code update}。 */
    void update(Drone drone);

    /** 删除；实现类 {@code deleteById}。 */
    void delete(Long id);
}
