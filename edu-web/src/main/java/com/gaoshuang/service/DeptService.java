package com.gaoshuang.service;

import com.gaoshuang.pojo.Dept;

import java.util.List;

public interface DeptService {
    List<Dept> findAll();

    void deleteById(Integer id);
    //新增 部门
    void add(Dept dept);

    //根据ID查询部门数据
    Dept getById(Integer id);
    //修改部门
    void update(Dept dept);
}