package com.gaoshuang.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.gaoshuang.exception.BusinessException;
import com.gaoshuang.mapper.ClazzMapper;
import com.gaoshuang.pojo.Clazz;
import com.gaoshuang.pojo.ClazzQueryParam;
import com.gaoshuang.pojo.PageResult;
import com.gaoshuang.service.ClazzService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClazzServiceImpl implements ClazzService {

    @Autowired
    private ClazzMapper clazzMapper;
    @Override
    public PageResult<Clazz> page(ClazzQueryParam clazzQueryParam) {
        // 1.开启分页pageHelper
        PageHelper.startPage(clazzQueryParam.getPage(),clazzQueryParam.getPageSize());
        // 2.查询所有
        List<Clazz> clazzList = clazzMapper.list(clazzQueryParam);
        // 3.返回结果
        Page<Clazz> p = (Page<Clazz>) clazzList;
        return new PageResult<Clazz>(p.getTotal(), p.getResult());
    }


    //新增班级
    @Override
    public void save(Clazz clazz) {
        //1.保存班级基本信息
        clazz.setCreateTime(LocalDateTime.now());
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.insert(clazz);

    }
    /*//查询班主任信息
    @Override
    public List<Integer> selectMasterId() {
        return clazzMapper.selectMasterId();
    }*/
    //根据id查询班级信息
    @Override
    public Clazz selectById(Integer id) {
        return clazzMapper.selectById(id);

    }
    //修改班级信息
    @Override
    public void update(Clazz clazz) {
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.update(clazz);

    }

    @Override
    public void delete(Integer id) {
        //1.检查是否有学生属于该班级
        int count = clazzMapper.countStudentsByClazzId(id);
        // 2. 如果有学生，抛出异常，不允许删除
        if (count > 0) {
            throw new BusinessException("该班级下还有" + count + "名学生，无法删除！");
        }
        // 3. 如果没有学生，执行删除操作
        clazzMapper.deleteById(id);
    }

    @Override
    public List<Clazz> list() {
        return clazzMapper.selectAll();
    }
}
