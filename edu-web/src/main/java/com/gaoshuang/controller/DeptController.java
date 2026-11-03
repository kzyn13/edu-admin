package com.gaoshuang.controller;

import com.gaoshuang.pojo.Dept;
import com.gaoshuang.pojo.Result;
import com.gaoshuang.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 部门管理
 */
@Slf4j
@RestController
public class DeptController {

    @Autowired
    private DeptService deptService;

    /** 查询全部部门 */
    @GetMapping("/depts")
    public Result list() {
        log.info("查询全部部门数据");
        List<Dept> deptList = deptService.findAll();
        return Result.success(deptList);
    }

    /** 根据 id 查询部门 */
    @GetMapping("/depts/{id}")
    public Result getInfo(@PathVariable Integer id) {
        log.info("根据ID查询部门：{}", id);
        Dept dept = deptService.getById(id);
        return Result.success(dept);
    }

    /** 新增部门 */
    @PostMapping("/depts")
    public Result add(@RequestBody Dept dept) {
        log.info("添加部门数据：{}", dept);
        deptService.add(dept);
        return Result.success();
    }

    /** 修改部门 */
    @PutMapping("/depts")
    public Result update(@RequestBody Dept dept) {
        log.info("修改部门数据：{}", dept);
        deptService.update(dept);
        return Result.success();
    }

    /** 删除部门 */
    @DeleteMapping("/depts")
    public Result delete(Integer id) {
        log.info("删除部门数据：{}", id);
        deptService.deleteById(id);
        return Result.success();
    }
}
