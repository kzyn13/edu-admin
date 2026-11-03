package com.gaoshuang.mapper;


import com.gaoshuang.pojo.Clazz;
import com.gaoshuang.pojo.ClazzQueryParam;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ClazzMapper {
    /*@Select("select c.*, e.name as master_name\n" +
            "from clazz c\n" +
            "         left join emp e on c.master_id = e.id order by c.update_time")*/
    /*@Select("""
        select 
            c.*, 
            IFNULL(e.name, '') as master_name,
            case
                when now() > c.end_date then '已结课'
                when now() < c.begin_date then '未开班'
                else '在读中'
            end as status
        from clazz c
        left join emp e on c.master_id = e.id 
        where c.name like concat('%', 'java', '%') and c.begin_date between '2020-01-01' and '2023-10-19'
        order by c.update_time
        """)*/
    public List<Clazz> list(ClazzQueryParam clazzQueryParam);
    //新增班级基本信息
    @Insert("insert into clazz values(null, #{name}, #{room}, #{beginDate}, #{endDate}, #{masterId}, #{subject}, #{createTime}, #{updateTime})")
    void insert(Clazz clazz);

    //根据ID查询班级基本信息
    @Select("select * from clazz where id = #{id}")
    Clazz selectById(Integer id);

    //修改班级基本信息
    @Update("update clazz set name = #{name}, room = #{room}, begin_date = #{beginDate}, end_date = #{endDate}, master_id = #{masterId}, subject = #{subject}, update_time = #{updateTime} where id = #{id}")
    void update(Clazz clazz);
    //根据ID删除班级基本信息，有关联的学生不允许删除
    @Delete("delete from clazz where id = #{id}")
    void deleteById(Integer id);
    //统计该班级的学生数量
    @Select("select count(*) from student where clazz_id = #{id}")
    int countStudentsByClazzId(Integer id);
    //查询班级信息
    @Select("select * from clazz")
    List<Clazz> selectAll();
    /*//查询班主任信息
    @Select("select id from emp")
    List<Integer> selectMasterId();*/
}
