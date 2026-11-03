package com.gaoshuang.controller;

import com.gaoshuang.anno.Log;
import com.gaoshuang.pojo.Clazz;
import com.gaoshuang.pojo.ClazzQueryParam;
import com.gaoshuang.pojo.PageResult;
import com.gaoshuang.pojo.Result;
import com.gaoshuang.service.ClazzService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/clazzs")
public class ClazzController {

    @Autowired
    private ClazzService clazzService;
    /*
     * 分页查询
     * */
    @GetMapping
    public Result page(ClazzQueryParam clazzQueryParam){
        log.info("分页查询记录数：{}",clazzQueryParam);
        PageResult<Clazz> clazz = clazzService.page(clazzQueryParam); // 调用服务层方法进行分页查询
        return Result.success(clazz);
    }

    @PostMapping
    public Result save(@RequestBody Clazz clazz){
        log.info("新增课程信息：{}",clazz);
        clazzService.save(clazz);
        return Result.success();
    }
//    @GetMapping("/test")
//    public Result selectMasterName(){
//        log.info("查询班主任姓名：{}");
//        List<Integer> integers = clazzService.selectMasterId();
//        return Result.success(integers);
//    }
    //根据id查询班级信息
    @GetMapping("/{id}")
    public Result selectById(@PathVariable Integer id){
        log.info("根据id查询班级信息：{}",id);
        Clazz clazz = clazzService.selectById(id);
        return Result.success(clazz);
    }
    //修改班级信息
    @Log
    @PutMapping
    public Result update(@RequestBody Clazz clazz){
        log.info("修改班级信息：{}",clazz);
        clazzService.update(clazz);
        return Result.success();
    }
    //根据id删除班级信息
    @Log
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        log.info("根据id删除班级信息：{}",id);
        clazzService.delete(id);
        return Result.success();
    }
    //查询所有班级
    @GetMapping("/list")
    public Result list(){
        log.info("查询所有班级");
        List<Clazz> clazzList = clazzService.list();
        return Result.success(clazzList);
    }
}
