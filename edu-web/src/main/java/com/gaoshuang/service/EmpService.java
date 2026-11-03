package com.gaoshuang.service;

import com.gaoshuang.pojo.Emp;
import com.gaoshuang.pojo.EmpQueryParam;
import com.gaoshuang.pojo.LoginInfo;
import com.gaoshuang.pojo.PageResult;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface EmpService {
    //分页查询
    PageResult<Emp> page(EmpQueryParam empQueryParam);
    //新增员工信息
    void save(Emp emp);
    //删除员工信息
    void delete(List<Integer> ids);
    //根据id查询员工信息
    Emp getInfo(Integer id);
    //根据id修改员工信息
    void update(Emp emp);
    //查询班主任信息
    List<Map<String, Object>> listMasters();
    //员工登录
    LoginInfo login(Emp emp);

    /*
    * 分页查询的实现方法
    * */
    //PageResult<Emp> page(Integer page, Integer pageSize, String name, Integer gender, LocalDate begin, LocalDate end);
}
