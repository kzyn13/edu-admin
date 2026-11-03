package com.gaoshuang.mapper;


import com.gaoshuang.pojo.OperateLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LogMapper {


    List<OperateLog> list(OperateLog operateLog);
}
