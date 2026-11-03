package com.gaoshuang.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.gaoshuang.mapper.LogMapper;
import com.gaoshuang.pojo.Clazz;
import com.gaoshuang.pojo.OperateLog;
import com.gaoshuang.pojo.PageResult;
import com.gaoshuang.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class LogServiceImpl implements LogService {
    @Autowired
    private LogMapper logMapper;
    @Override
    public PageResult<OperateLog> page(OperateLog operateLog) {
        // 1.开启分页pageHelper
        PageHelper.startPage(1,5);
        // 2.查询所有
        List<OperateLog> operateLogList = logMapper.list(operateLog);
        // 3.返回结果
        Page<OperateLog> p = (Page<OperateLog>) operateLogList;
        return new PageResult<OperateLog>(p.getTotal(), p.getResult());
    }
}
