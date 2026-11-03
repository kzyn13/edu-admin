package com.gaoshuang.controller;

import com.gaoshuang.pojo.Emp;
import com.gaoshuang.pojo.EmpQueryParam;
import com.gaoshuang.pojo.PageResult;
import com.gaoshuang.pojo.Result;
import com.gaoshuang.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 员工管理
 */
@Slf4j
@RestController
@RequestMapping("/emps")
public class EmpController {

    @Autowired
    private EmpService empService;

    /** 条件分页查询：GET /emps?page=1&pageSize=10&name=张&gender=1 */
    @GetMapping
    public Result page(EmpQueryParam empQueryParam) {
        log.info("分页查询：{}", empQueryParam);
        PageResult<Emp> pageResult = empService.page(empQueryParam);
        return Result.success(pageResult);
    }

    /** 根据 id 查询员工：GET /emps/1 */
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id) {
        log.info("根据ID查询员工信息：{}", id);
        Emp emp = empService.getInfo(id);
        return Result.success(emp);
    }

    /** 查询班主任列表（前端下拉框用）：GET /emps/list */
    @GetMapping("/list")
    public Result listMasters() {
        log.info("查询班主任列表");
        List<Map<String, Object>> masters = empService.listMasters();
        return Result.success(masters);
    }

    /** 新增员工：POST /emps */
    @PostMapping
    public Result save(@RequestBody Emp emp) {
        log.info("新增员工信息：{}", emp);
        empService.save(emp);
        return Result.success();
    }

    /** 修改员工：PUT /emps */
    @PutMapping
    public Result update(@RequestBody Emp emp) {
        log.info("修改员工信息：{}", emp);
        empService.update(emp);
        return Result.success();
    }

    /** 批量删除：DELETE /emps?ids=1,2,3 */
    @DeleteMapping
    public Result delete(@RequestParam List<Integer> ids) {
        log.info("删除员工信息：{}", ids);
        empService.delete(ids);
        return Result.success();
    }
}
