/**
 * 无人机管理系统 — 无人机 Web 控制器（DroneController.java）
 * <p>作用：表现层入口，处理 {@code /drones} 下所有与无人机相关的 HTTP 请求，调用业务层并返回 Thymeleaf 视图或重定向。</p>
 * <p>功能：实现列表展示、条件查询、详情查看、新增/编辑表单、保存提交、删除等页面流程；
 * 将 {@code Drone}、{@code DroneQueryCriteria} 等数据放入 Model 供模板渲染。</p>
 */
package com.drone.management.controller;

import com.drone.management.dto.DroneQueryCriteria;
import com.drone.management.entity.Drone;
import com.drone.management.service.DroneService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

/**
 * <b>表现层（Controller）：无人机业务 URL 入口</b><br>
 * 前缀 {@code /drones} 对应 Shiro 中登录后访问路径；返回视图名对应 {@code templates/drone/*.html}。<br>
 * 依赖业务层：{@link DroneService}；不直接注入 Mapper。
 */
@Controller // 声明为 Spring MVC 控制器，处理 HTTP 并解析视图
@RequestMapping("/drones") // 本类所有映射 URL 均以 /drones 开头
public class DroneController {

    /** 业务服务对象：封装增删改查与条件查询逻辑。 */
    private final DroneService droneService;

    public DroneController(DroneService droneService) {
        this.droneService = droneService; // 构造器注入，对应 Spring 容器中的 DroneServiceImpl
    }

    /**
     * GET /drones — 默认列表页。<br>
     * [表现层] Model 键名与 Thymeleaf 模板 {@code drone/list.html} 中变量一致。
     */
    @GetMapping // 等价于 @GetMapping("")，匹配 GET /drones
    public String list(Model model) {
        model.addAttribute("drones", droneService.listAll()); // 全量列表 → 对应业务层 listAll → Mapper findAll
        model.addAttribute("query", new DroneQueryCriteria()); // 空查询对象，供表单 th:object 绑定
        model.addAttribute("searchApplied", false); // 标记当前非「查询结果」模式，模板用于文案区分
        return "drone/list"; // 视图解析为 templates/drone/list.html
    }

    /**
     * POST /drones/query — 条件查询，结果仍渲染列表页。<br>
     * [表现层 ↔ 业务层] 表单绑定为 {@link DroneQueryCriteria}；结果 List&lt;Drone&gt; 来自 {@code findByCriteria}。
     */
    @PostMapping("/query")
    public String query(@ModelAttribute DroneQueryCriteria criteria, // 将表单字段绑定到 DTO（名称与 input name 对应）
                        Model model,
                        RedirectAttributes ra) { // 重定向时携带一次性 flash 消息
        if (!criteria.hasAnyNonBlankCriteria()) {
            ra.addFlashAttribute("error", "请至少填写一项查询条件"); // 下次 GET /drones 时在模板显示
            return "redirect:/drones"; // PRG 模式：避免刷新重复 POST
        }
        List<Drone> drones = droneService.findByCriteria(criteria); // [表现层 → 业务层] 条件查询
        model.addAttribute("drones", drones); // 查询结果集，模板中 th:each 遍历
        model.addAttribute("query", criteria); // 回显用户输入的查询条件
        model.addAttribute("searchApplied", true); // 模板显示「共找到 N 条」等
        model.addAttribute("matchCount", drones.size()); // 命中条数，仅用于展示
        return "drone/list"; // 同列表模板，数据为筛选结果
    }

    /**
     * GET /drones/detail/{id} — 单机详情。<br>
     * [表现层 → 业务层] getById；[对应模板] drone/detail.html。
     */
    @GetMapping("/detail/{id}")
    public String detailById(@PathVariable("id") Long id, // 从 URL 路径段解析主键
                             Model model,
                             RedirectAttributes ra) {
        Drone drone = droneService.getById(id); // 按主键查实体
        if (drone == null) {
            ra.addFlashAttribute("error", "记录不存在");
            return "redirect:/drones";
        }
        model.addAttribute("drone", drone); // 键 drone 与 detail.html 中 th:object / 字段引用一致
        return "drone/detail";
    }

    /** GET /drones/add — 新增表单页；对应模板 drone/form.html。 */
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("drone", new Drone()); // 空实体供表单绑定，id 为空表示新增
        model.addAttribute("formTitle", "新增无人机"); // 页面标题文案
        return "drone/form";
    }

    /** GET /drones/edit/{id} — 编辑表单页；数据来自业务层 getById。 */
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model, RedirectAttributes ra) {
        Drone drone = droneService.getById(id);
        if (drone == null) {
            ra.addFlashAttribute("error", "记录不存在");
            return "redirect:/drones";
        }
        model.addAttribute("drone", drone); // 带 id 的实体，提交时走更新分支
        model.addAttribute("formTitle", "编辑无人机");
        return "drone/form";
    }

    /**
     * POST /drones/save — 接收表单提交的新增或更新。<br>
     * {@code @Valid} 触发实体 {@link Drone} 上的 JSR-303 校验（如名称非空）。
     */
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("drone") Drone drone, // 与 form 中 th:object 名称一致
                       BindingResult bindingResult, // 校验错误容器
                       Model model,
                       RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formTitle", drone.getId() == null ? "新增无人机" : "编辑无人机"); // 校验失败留在表单页
            return "drone/form";
        }
        if (drone.getId() == null) {
            droneService.save(drone); // [表现层 → 业务层] 新增
            ra.addFlashAttribute("message", "新增成功");
        } else {
            droneService.update(drone); // [表现层 → 业务层] 更新
            ra.addFlashAttribute("message", "更新成功");
        }
        return "redirect:/drones"; // 保存后回到列表，避免重复提交
    }

    /** POST /drones/delete/{id} — 删除一条；对应列表页删除按钮表单。 */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes ra) {
        droneService.delete(id); // [表现层 → 业务层] 删除
        ra.addFlashAttribute("message", "已删除");
        return "redirect:/drones";
    }
}
