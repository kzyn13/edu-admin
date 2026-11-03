package com.gaoshuang.mapper;
/*
* 员工信息接口
* */

import com.gaoshuang.pojo.Emp;
import com.gaoshuang.pojo.EmpQueryParam;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface EmpMapper {
//    //------------------------常规分页方法-------------
//    /*
//    * 查询总记录数
//    * */
//    @Select("select count(*) from emp e left join dept d on e.dept_id = d.id")
//    public Long count();
//
//
//
//    //* 分页查询
//    //*
//   @Select("select e.*, d.name deptname from emp e left join dept d on e.dept_id = d.id " +
//           "order by e.update_time desc limit #{start},#{pageSize}")
//    public List<Emp> list(Integer start,Integer pageSize);

    /*
    * 条件查询员工信息
    * */
    //@Select("select e.*, d.name deptname from emp e left join dept d on e.dept_id = d.id  order by e.update_time desc" )
    public List<Emp> list(EmpQueryParam empQueryParam);

    @Options(useGeneratedKeys = true,keyProperty = "id")
    //新增员工信息
    @Insert("insert into emp(username,  name, gender, phone, job, salary, image, entry_date, dept_id, create_time, update_time)" +
            "    values (#{username},#{name},#{gender},#{phone},#{job},#{salary},#{image},#{entryDate},#{deptId},#{createTime},#{updateTime});")
    void insert(Emp emp);
    //批量删除员工信息
    void deleteByIds(List<Integer> ids);
    //根据id查询员工信息
    Emp getById(Integer id);
    //根据id更新员工基本信息
    void upDateById(Emp emp);
    //统计职位员工人数
    @MapKey("pos")
    List<Map<String, Object>> countEmpJobDate();
    //统计员工性别人数
    @MapKey("name")
    List<Map<String, Object>> countEmpGenderDate();

    @Select("select id, name from emp where job = 1")
    List<Map<String, Object>> listMasters();
    //统计学员学历信息
    @MapKey("name")
    List<Map<String, Object>> getEducationData();

    //统计班级人数信息
    @MapKey("name")
    List<Map<String, Object>> getStudentCountData();

    //员工登录
    @Select("select id,emp.username,name from emp where username = #{username} and password = #{password}")
    Emp selectByNameAndPwd(Emp emp);
}
