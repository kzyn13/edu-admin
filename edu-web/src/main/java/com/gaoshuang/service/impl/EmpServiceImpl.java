package com.gaoshuang.service.impl;

import com.gaoshuang.mapper.EmpExperMapper;
import com.gaoshuang.mapper.EmpMapper;
import com.gaoshuang.pojo.Emp;
import com.gaoshuang.pojo.EmpExpr;
import com.gaoshuang.pojo.EmpQueryParam;
import com.gaoshuang.pojo.LoginInfo;
import com.gaoshuang.pojo.PageResult;
import com.gaoshuang.service.EmpService;
import com.gaoshuang.utils.JwtUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 员工管理
 */
@Slf4j
@Service
public class EmpServiceImpl implements EmpService {

    @Autowired
    private EmpMapper empMapper;

    @Autowired
    private EmpExperMapper empExperMapper;

    /**
     * 条件分页查询
     * <p>
     * 注意：PageHelper.startPage 会把分页参数放进 ThreadLocal，
     * 拦截器只对「紧跟其后」的那一条查询生效，中间不能插入其他查询。
     */
    @Override
    public PageResult<Emp> page(EmpQueryParam empQueryParam) {
        PageHelper.startPage(empQueryParam.getPage(), empQueryParam.getPageSize());

        List<Emp> empList = empMapper.list(empQueryParam);

        // PageHelper 返回的 list 实际是 Page 实例（Page 继承自 ArrayList），强转后可取 total
        Page<Emp> p = (Page<Emp>) empList;
        return new PageResult<>(p.getTotal(), p.getResult());
    }

    /** 查询班主任列表（用于前端下拉框） */
    @Override
    public List<Map<String, Object>> listMasters() {
        return empMapper.listMasters();
    }

    /**
     * 新增员工
     * <p>
     * 主表和子表（工作经历）必须在同一个事务里，否则可能出现「有员工但没有经历」的脏数据。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(Emp emp) {
        emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());

        // ① 写主表：主键由 @Options(useGeneratedKeys = true, keyProperty = "id") 回填到 emp.id
        empMapper.insert(emp);

        // ② 写子表：用回填的主键填外键，再批量插入
        List<EmpExpr> exprList = emp.getExprList();
        if (!CollectionUtils.isEmpty(exprList)) {
            exprList.forEach(expr -> expr.setEmpId(emp.getId()));
            empExperMapper.insertBatch(exprList);
        }
    }

    /**
     * 批量删除员工
     * <p>
     * 主表、子表都要删，否则会留下没有归属的工作经历记录。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<Integer> ids) {
        empMapper.deleteByIds(ids);
        empExperMapper.deleteByEmpIds(ids);
    }

    /** 根据 id 查询员工信息 */
    @Override
    public Emp getInfo(Integer id) {
        return empMapper.getById(id);
    }

    /**
     * 修改员工
     * <p>
     * 工作经历采用「先删后插」：实现简单，不用比对差异；
     * 代价是子表主键每次都会变，如果有其他表引用子表 id 就不能这么做。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Emp emp) {
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.upDateById(emp);

        // 先删掉原有的工作经历
        empExperMapper.deleteByEmpIds(List.of(emp.getId()));

        // 再按入参重新插入
        List<EmpExpr> exprList = emp.getExprList();
        if (!CollectionUtils.isEmpty(exprList)) {
            exprList.forEach(expr -> expr.setEmpId(emp.getId()));
            empExperMapper.insertBatch(exprList);
        }
    }

    /**
     * 员工登录：校验用户名密码，签发 JWT
     *
     * @return 登录成功返回用户信息与 token，失败返回 null
     */
    @Override
    public LoginInfo login(Emp emp) {
        Emp e = empMapper.selectByNameAndPwd(emp);
        if (e == null) {
            return null;
        }

        log.info("员工登录成功：{}", e);
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", e.getId());
        claims.put("username", e.getUsername());
        String jwt = JwtUtils.generateJwt(claims);

        return new LoginInfo(e.getId(), e.getUsername(), e.getName(), jwt);
    }
}
