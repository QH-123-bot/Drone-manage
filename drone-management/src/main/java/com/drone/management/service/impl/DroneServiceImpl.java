/**
 * 无人机管理系统 — 无人机业务服务实现类（DroneServiceImpl.java）
 * <p>作用：业务层核心实现，封装无人机增删改查的业务规则与事务边界，是唯一调用 {@code DroneMapper} 的业务入口。</p>
 * <p>功能：委托 Mapper 完成持久化；保存/更新时自动填充时间戳；条件查询前规范化并校验查询参数；
 * 对写操作使用 {@code @Transactional} 保证事务一致性。</p>
 */
package com.drone.management.service.impl;

import com.drone.management.dto.DroneQueryCriteria;
import com.drone.management.entity.Drone;
import com.drone.management.mapper.DroneMapper;
import com.drone.management.service.DroneService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * <b>业务层实现（Service 实现类）</b><br>
 * 标注 {@link Service} 由 Spring 容器管理；实现接口 {@link DroneService}。<br>
 * 依赖数据层：{@link DroneMapper}（仅此一处访问数据库逻辑入口）。<br>
 * 对应表现层：{@link com.drone.management.controller.DroneController} 注入的是接口，运行时绑定到本类。
 */
@Service // 声明为 Spring 业务 Bean，供 Controller 构造器注入
public class DroneServiceImpl implements DroneService {

    /** 数据访问对象：执行具体 SQL（定义在 DroneMapper.xml）。 */
    private final DroneMapper droneMapper;

    /** 构造器注入 Mapper（推荐写法，便于测试替换实现）。 */
    public DroneServiceImpl(DroneMapper droneMapper) {
        this.droneMapper = droneMapper;
    }

    @Override
    public List<Drone> listAll() {
        // [业务层 → 数据层] 无额外规则时直接委托 Mapper；对应 XML：findAll
        return droneMapper.findAll();
    }

    @Override
    public Drone getById(Long id) {
        // [业务层 → 数据层] 按主键查；对应页面：detail.html、编辑表单回显
        return droneMapper.findById(id);
    }

    @Override
    public List<Drone> findByCriteria(DroneQueryCriteria raw) {
        // 入参为空或用户未填任何条件：不访问数据库，返回空列表（与表现层 flash 提示配合）
        if (raw == null || !raw.hasAnyNonBlankCriteria()) {
            return Collections.emptyList();
        }
        // 规范化字符串（trim、空串变 null），避免 XML 里拼接无意义条件
        DroneQueryCriteria q = normalizeCriteria(raw);
        if (!hasAnyCondition(q)) {
            return Collections.emptyList();
        }
        // [业务层 → 数据层] 动态条件查询；对应 XML：findByCriteria；对应页面：list 查询结果表
        return droneMapper.findByCriteria(q);
    }

    /** 判断规范化后是否仍存在任一非空条件（双保险）。 */
    private static boolean hasAnyCondition(DroneQueryCriteria q) {
        return q.getId() != null
                || StringUtils.hasText(q.getName())
                || StringUtils.hasText(q.getModel())
                || StringUtils.hasText(q.getSerialNumber())
                || q.getMaxFlightTime() != null
                || q.getMaxSpeed() != null
                || StringUtils.hasText(q.getStatus());
    }

    /** 复制一份 DTO 并对字符串字段 trim，空白串置 null，供 MyBatis 动态 SQL 判断。 */
    private static DroneQueryCriteria normalizeCriteria(DroneQueryCriteria c) {
        DroneQueryCriteria q = new DroneQueryCriteria();
        q.setId(c.getId());
        q.setName(trimToNull(c.getName()));
        q.setModel(trimToNull(c.getModel()));
        q.setSerialNumber(trimToNull(c.getSerialNumber()));
        q.setMaxFlightTime(c.getMaxFlightTime());
        q.setMaxSpeed(c.getMaxSpeed());
        q.setStatus(trimToNull(c.getStatus()));
        return q;
    }

    /** null 保持 null；仅空白字符则返回 null。 */
    private static String trimToNull(String s) {
        if (s == null) {
            return null;
        }
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // 发生异常则回滚数据库事务
    public void save(Drone drone) {
        long now = System.currentTimeMillis(); // 统一用毫秒时间戳存库
        if (drone.getCreatedAt() == null) {
            drone.setCreatedAt(now); // 新建时补创建时间
        }
        drone.setUpdatedAt(now); // 新建同时写更新时间
        droneMapper.insert(drone); // [业务层 → 数据层] 对应 XML：insert
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Drone drone) {
        drone.setUpdatedAt(System.currentTimeMillis()); // 每次更新刷新更新时间
        droneMapper.update(drone); // [业务层 → 数据层] 对应 XML：update
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        droneMapper.deleteById(id); // [业务层 → 数据层] 对应 XML：deleteById
    }
}
