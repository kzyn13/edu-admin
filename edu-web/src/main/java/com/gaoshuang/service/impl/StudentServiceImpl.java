package com.gaoshuang.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.gaoshuang.mapper.StudentMapper;
import com.gaoshuang.pojo.PageResult;
import com.gaoshuang.pojo.Student;
import com.gaoshuang.pojo.StudentQueryParam;
import com.gaoshuang.service.StudentService;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;
    @Override
    public PageResult<Student> page(StudentQueryParam studentQueryParam) {
        // 1.开启分页pageHelper
        PageHelper.startPage(studentQueryParam.getPage(), studentQueryParam.getPageSize());
        // 1.查询
        List<Student> studentList = studentMapper.list(studentQueryParam);

        // 3.返回结果
        Page<Student> p = (Page<Student>) studentList;
        return new PageResult<Student>(p.getTotal(), p.getResult());
    }
    //新增学生


    @Override
    public void save(Student student) {
        student.setCreateTime(LocalDateTime.now());
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.insert(student);
    }
    //根据id查询学生信息

    @Override
    public Student selectById(Integer id) {
        return studentMapper.selectById(id);
    }
    //修改学生信息

    @Override
    public void update(Student student) {
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.update(student);
    }
    //删除学员信息
    @Override
    public void delete(Integer id) {
        studentMapper.deleteById(id);
    }

    @Override
    public void updateViolationScore(Integer id, Integer score) {
        studentMapper.updateViolationScore(id, score);

    }
}
