package com.gaoshuang.service;

import com.gaoshuang.pojo.OperateLog;
import com.gaoshuang.pojo.PageResult;

public interface LogService {
    PageResult<OperateLog> page(OperateLog operateLog);
}
