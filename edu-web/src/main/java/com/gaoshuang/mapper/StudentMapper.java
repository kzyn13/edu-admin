package com.gaoshuang.mapper;

import com.gaoshuang.pojo.Student;
import com.gaoshuang.pojo.StudentQueryParam;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface StudentMapper {
    //分页查询
    public List<Student> list(StudentQueryParam studentQueryParam);

    //新增学生
    @Insert("insert into student(name, no, gender, phone, id_card, is_college, address, degree, graduation_date, clazz_id, create_time, update_time) values(#{name}, #{no}, #{gender}, #{phone}, #{idCard}, #{isCollege}, #{address}, #{degree}, #{graduationDate}, #{clazzId}, #{createTime}, #{updateTime})")
    void insert(Student student);

    //根据id查询学生信息
    @Select("select * from student where id = #{id}")
    Student selectById(Integer id);

    @Update("update student set name = #{name}, no = #{no}, gender = #{gender}, phone = #{phone}, id_card = #{idCard}, is_college = #{isCollege}, address = #{address}, degree = #{degree}, graduation_date = #{graduationDate}, clazz_id = #{clazzId}, update_time = #{updateTime} where id = #{id}")
    void update(Student student);
    @Delete("delete from student where id = #{id}")
    void deleteById(Integer id);

    //学员违纪
    @Update("update student set violation_score = #{score} where id = #{id}")
    void updateViolationScore(@Param("id") Integer id, @Param("score")Integer score);

}

