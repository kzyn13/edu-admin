package com.gaoshuang.service;

import com.gaoshuang.pojo.Clazz;
import com.gaoshuang.pojo.ClazzQueryParam;
import com.gaoshuang.pojo.PageResult;

import java.util.List;

public interface ClazzService {
    //分页查询
    PageResult<Clazz> page(ClazzQueryParam clazzQueryParam);
    void save(Clazz clazz);
    //根据id查询班级信息
    Clazz selectById(Integer id);
    //修改班级信息
    void update(Clazz clazz);
    //删除班级信息
    void delete(Integer id);
    //查询所有班级
    List<Clazz> list();

//    List<Integer> selectMasterId();
}
