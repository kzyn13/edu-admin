package com.gaoshuang.service;

import com.gaoshuang.pojo.PageResult;
import com.gaoshuang.pojo.Student;
import com.gaoshuang.pojo.StudentQueryParam;
import org.springframework.stereotype.Service;


public interface StudentService {
    //分页查询
    PageResult<Student> page(StudentQueryParam studentQueryParam);

    //增加学员信息
    void save(Student student);
    //根据id查询
    Student selectById(Integer id);
    //修改学员信息
    void update(Student student);
    //删除学员信息
    void delete(Integer id);
    //学员违纪
    void updateViolationScore(Integer id, Integer score);
}
