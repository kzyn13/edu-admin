package com.gaoshuang.mapper;


import com.gaoshuang.pojo.EmpExpr;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/*
* 员工工作经历接口
* */
@Mapper
public interface EmpExperMapper {
    //批量新增员工工作经历
    void insertBatch(List<EmpExpr> exprList);
    //批量删除员工工作经历
    void deleteByEmpIds(List<Integer> empIds);
}
