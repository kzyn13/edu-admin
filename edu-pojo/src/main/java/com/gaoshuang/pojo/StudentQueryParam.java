package com.gaoshuang.pojo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class StudentQueryParam {
    private Integer page = 1;
    private Integer pageSize = 10;
    private String name; // 姓名

    private Integer degree; // 学历，1: 初中，2: 高中，3: 大专，4: 本科，5: 硕士，6: 博士
    private Integer clazzId; // 班级 ID


}
