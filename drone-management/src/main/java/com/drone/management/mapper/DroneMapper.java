/**
 * 无人机管理系统 — 无人机数据访问接口（DroneMapper.java）
 * <p>作用：数据访问层（DAO）接口，声明对 {@code drone} 表的持久化操作，由 MyBatis 在运行时生成实现。</p>
 * <p>功能：定义按主键查询、全表列表、动态条件查询、插入、更新、删除等方法；具体 SQL 写在 {@code DroneMapper.xml} 中。</p>
 */
package com.drone.management.mapper;

import com.drone.management.dto.DroneQueryCriteria;
import com.drone.management.entity.Drone;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <b>数据访问层（DAO / Mapper 接口）</b><br>
 * 只声明方法签名，SQL 写在同名的 XML：{@code src/main/resources/mapper/DroneMapper.xml}。<br>
 * 由 MyBatis 在运行时生成实现类；业务层 {@link com.drone.management.service.impl.DroneServiceImpl} 注入本接口调用。<br>
 * 对应实体：{@link Drone}；条件查询参数：{@link DroneQueryCriteria}（XML 中参数名 {@code q}）。
 */
public interface DroneMapper {

    /** 按主键查一行；对应 XML {@code findById}；结果给业务层/表现层详情页。 */
    Drone findById(@Param("id") Long id);

    /**
     * 动态条件查询多行（AND）；对应 XML {@code findByCriteria}。<br>
     * 入参 {@code q}：与 {@code DroneQueryCriteria} 属性一一对应到 WHERE 子句。
     */
    List<Drone> findByCriteria(@Param("q") DroneQueryCriteria criteria);

    /** 全表查询；对应 XML {@code findAll}；用于列表页默认展示。 */
    List<Drone> findAll();

    /** 插入一行；对应 XML {@code insert}；{@code useGeneratedKeys} 回填实体主键。 */
    int insert(Drone drone);

    /** 按主键更新；对应 XML {@code update}。 */
    int update(Drone drone);

    /** 按主键删除；对应 XML {@code deleteById}。 */
    int deleteById(@Param("id") Long id);
}
