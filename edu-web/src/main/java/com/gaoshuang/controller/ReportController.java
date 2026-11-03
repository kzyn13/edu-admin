package com.gaoshuang.controller;


import com.gaoshuang.pojo.ClazzOption;
import com.gaoshuang.pojo.JobOption;
import com.gaoshuang.pojo.Result;
import com.gaoshuang.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("/report")
@RestController


public class ReportController {
    @Autowired
    private ReportService reportService;
    /*
    * 统计职位员工人数
    * */

    @GetMapping("/empJobData")
    public Result getEmpJobData(){
        log.info("员工职位人数：");
        JobOption jobOption = reportService.getEmpJobData();
        return Result.success(jobOption);
    }
    /*
     * 统计员工性别人数
     * */
    @GetMapping("/empGenderData")
    public Result getEmpGenderData(){
        log.info("员工性别人数");
        List<Map<String,Object>> genderList = reportService.getEmpGenderData();
        return Result.success(genderList);
    }
    /*
     * 统计学员学历信息
     * */
    @GetMapping("/studentDegreeData")
    public Result getEducationData(){
        log.info("学员学历信息");
        List<Map<String,Object>> educationList = reportService.getEducationData();
        return Result.success(educationList);
    }
    /*
     * 班级人数统计
     * */
    @GetMapping("/studentCountData")
    public Result getStudentCountData(){
        log.info("班级人数统计");
        ClazzOption clazzOption = reportService.getStudentCountData();
        return Result.success(clazzOption);
    }


}
