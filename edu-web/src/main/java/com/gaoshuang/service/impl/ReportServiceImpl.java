package com.gaoshuang.service.impl;

import com.gaoshuang.mapper.EmpMapper;
import com.gaoshuang.pojo.ClazzOption;
import com.gaoshuang.pojo.JobOption;
import com.gaoshuang.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class  ReportServiceImpl implements ReportService {
    @Autowired
    private EmpMapper empMapper;
    /*
     * 统计职位员工人数
     * */
    @Override
    public JobOption getEmpJobData() {
        //1.调用mapper接口，获取统计职位员工数
        List<Map<String, Object>> list= empMapper.countEmpJobDate();
        //2.组装结果，并返回
        List<Object> jobList = list.stream().map(datamap -> datamap.get("pos")).toList();
        List<Object> dataList = list.stream().map(datamap -> datamap.get("num")).toList();
        return new JobOption(jobList,dataList);
    }
    /*
     * 统计员工性别人数
     * */
    @Override
    public List<Map<String, Object>> getEmpGenderData() {
        return empMapper.countEmpGenderDate();
    }

    /*
     * 统计学员学历信息
     * */
    @Override
    public List<Map<String, Object>> getEducationData() {
        return empMapper.getEducationData();
    }
    /*
     * 统计班级人数信息
     * */

    @Override
    public ClazzOption getStudentCountData() {
        //1.调用mapper接口，获取班级人数信息
        List<Map<String, Object>> list= empMapper.getStudentCountData();
        //2.组装结果，并返回
        List<Object> clazzList = list.stream().map(datamap -> datamap.get("name")).toList();
        List<Object> dataList = list.stream().map(datamap -> datamap.get("num")).toList();
        return new ClazzOption(clazzList,dataList);

    }
}
