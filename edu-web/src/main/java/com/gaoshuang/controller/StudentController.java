package com.gaoshuang.controller;

import com.gaoshuang.pojo.PageResult;
import com.gaoshuang.pojo.Result;
import com.gaoshuang.pojo.Student;
import com.gaoshuang.pojo.StudentQueryParam;
import com.gaoshuang.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    //分页查询学生
    @GetMapping
    public Result page(StudentQueryParam studentQueryParam){
        log.info("分页查询学生信息，page={}, pageSize={}", studentQueryParam.getPage(), studentQueryParam.getPageSize());
        PageResult<Student> result = studentService.page(studentQueryParam);
        return Result.success(result);
    }

    //新增学生
    @PostMapping
    public Result save(@RequestBody Student student){
        log.info("新增学生信息：{}",student);
        studentService.save(student);
        return Result.success();
    }
    //根据id查询学生信息
    @GetMapping("/{id}")
    public Result findById(@PathVariable Integer id){
        log.info("根据id查询学生信息，id={}",id);
        Student student = studentService.selectById(id);
        return Result.success(student);
    }
    //修改学生信息
    @PutMapping
    public Result update(@RequestBody Student student){
        log.info("修改学生信息：{}",student);
        studentService.update(student);
        return Result.success();
    }
    //删除学生信息
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        log.info("删除学生信息，id={}",id);
        studentService.delete(id);
        return Result.success();
    }
    //违纪处理
    @PutMapping("/violation/{id}/{score}")
    public Result upDateViolationScore(@PathVariable Integer id,@PathVariable Integer score){
        log.info("违纪处理，ID={}，score={}",id, score);
        studentService.updateViolationScore(id, score);
        return Result.success();
    }
    //学员信息统计

}

