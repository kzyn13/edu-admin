package com.gaoshuang.controller;

import com.gaoshuang.pojo.OperateLog;
import com.gaoshuang.pojo.PageResult;
import com.gaoshuang.pojo.Result;
import com.gaoshuang.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/log")
public class LogController {
    @Autowired
    private LogService logService;

    @GetMapping("/page")
    public Result page(OperateLog operateLog){
        log.info("查询操作日志{}",operateLog);
        PageResult<OperateLog> OperateLog = logService.page(operateLog);
        return Result.success(OperateLog);


    }

}
