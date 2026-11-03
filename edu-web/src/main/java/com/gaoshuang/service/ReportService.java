package com.gaoshuang.service;

import com.gaoshuang.pojo.ClazzOption;
import com.gaoshuang.pojo.JobOption;

import java.util.List;
import java.util.Map;

public interface ReportService {
    /*
     * 统计职位员工人数
     * */
    JobOption getEmpJobData();
    /*
     * 统计员工性别人数
     * */
    List<Map<String, Object>> getEmpGenderData();
    /*
    * 统计学员学历信息
    * */
    List<Map<String, Object>> getEducationData();

    /*
     * 统计班级人数信息
     * */
    ClazzOption getStudentCountData();
}
