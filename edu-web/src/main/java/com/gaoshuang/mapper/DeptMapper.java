package com.gaoshuang.mapper;

import com.gaoshuang.pojo.Dept;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 部门 Mapper
 * <p>
 * 单表操作，直接用注解写 SQL。
 * 下划线转驼峰由 application.yml 的 map-underscore-to-camel-case 统一开启。
 */
@Mapper
public interface DeptMapper {

    /** 查询全部部门（按更新时间倒序） */
    @Select("select id, name, create_time, update_time from dept order by update_time desc")
    List<Dept> findAll();

    /** 根据 id 查询 */
    @Select("select id, name, create_time, update_time from dept where id = #{id}")
    Dept getById(Integer id);

    /** 新增 */
    @Insert("insert into dept(name, create_time, update_time) values(#{name}, #{createTime}, #{updateTime})")
    void insert(Dept dept);

    /** 更新 */
    @Update("update dept set name = #{name}, update_time = #{updateTime} where id = #{id}")
    void update(Dept dept);

    /** 根据 id 删除 */
    @Delete("delete from dept where id = #{id}")
    void deleteById(Integer id);
}
