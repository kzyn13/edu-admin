# edu-admin 培训机构教务管理系统

基于 Spring Boot + MyBatis 的教务后台系统，包含部门、员工、班级、学员管理，
以及登录认证、操作日志、文件上传等基础能力。

## 技术栈

- Spring Boot 3.5 / Spring MVC
- MyBatis（注解 + XML 动态 SQL）
- MySQL 8
- PageHelper 分页
- JWT 登录认证
- Spring AOP 操作日志
- 阿里云 OSS 文件上传
- Maven 多模块

## 功能模块

| 模块 | 说明 |
|---|---|
| 部门管理 | 增删改查 |
| 员工管理 | 条件分页、主表子表关联写入、批量删除 |
| 班级管理 | 条件分页、增删改查 |
| 学员管理 | 条件分页、增删改查 |
| 登录认证 | JWT + 拦截器 |
| 操作日志 | 自定义注解 + AOP 切面 |
| 文件上传 | 阿里云 OSS |
| 数据统计 | 员工职位 / 性别分布 |

## 项目结构

```
edu-admin
├── edu-pojo      实体类、查询参数对象
├── edu-utils     工具类（JWT、OSS、ThreadLocal 上下文）
└── edu-web       控制器、服务、Mapper、配置
```

## 快速开始

### 1. 初始化数据库

```bash
# Linux / macOS / Git Bash
mysql -u root -p < edu-web/src/main/resources/db/schema.sql
mysql -u root -p < edu-web/src/main/resources/db/data.sql

# Windows PowerShell（不支持 < 重定向，用 source）
mysql -u root -p -e "source edu-web/src/main/resources/db/schema.sql"
mysql -u root -p -e "source edu-web/src/main/resources/db/data.sql"
```

`schema.sql` 会自动创建 `edu_admin` 库。

### 2. 修改配置

编辑 `edu-web/src/main/resources/application.yml`，把数据库密码改成你自己的：

```yaml
spring:
  datasource:
    username: root
    password: 你的密码
```

### 3. 编译并启动

```bash
mvn clean package
java -jar edu-web/target/edu-web-0.0.1-SNAPSHOT.jar
```

启动后访问 `http://localhost:8080`。

### 4. 测试账号

`data.sql` 里内置了一批示例数据，可直接登录：

| 用户名 | 密码 |
|---|---|
| `zhangming` | `123456` |
| `liqiang` | `123456` |
| `wanglei` | `123456` |

登录接口 `POST /login` 返回 JWT，后续请求在请求头带上 `token` 即可访问其他接口。

## 技术要点

### 1. 主表 + 子表的一致性写入

员工与其工作经历是一对多关系。新增时：

```java
@Transactional(rollbackFor = Exception.class)
public void save(Emp emp) {
    empMapper.insert(emp);                    // ① 写主表
    List<EmpExpr> exprList = emp.getExprList();
    if (!CollectionUtils.isEmpty(exprList)) {
        exprList.forEach(e -> e.setEmpId(emp.getId()));   // ② 回填外键
        empExperMapper.insertBatch(exprList);             // ③ 批量写子表
    }
}
```

- 主表的主键由 `@Options(useGeneratedKeys = true, keyProperty = "id")` 回填
- 子表用 `<foreach>` 拼成一条批量 INSERT，减少网络往返
- 全过程在一个事务里，任何一步失败都整体回滚

### 2. 条件分页

PageHelper 设置分页参数，配合 XML 动态 SQL 组合查询条件：

```xml
<select id="list" resultType="com.gaoshuang.pojo.Emp">
    SELECT e.*, d.name AS deptName
    FROM emp e LEFT JOIN dept d ON e.dept_id = d.id
    <where>
        <if test="name != null and name != ''">AND e.name LIKE CONCAT('%', #{name}, '%')</if>
        <if test="gender != null">AND e.gender = #{gender}</if>
        <if test="begin != null">AND e.entry_date &gt;= #{begin}</if>
        <if test="end != null">AND e.entry_date &lt;= #{end}</if>
    </where>
    ORDER BY e.update_time DESC
</select>
```

`<where>` 会自动去掉开头多余的 `AND`。

> ⚠️ PageHelper 的坑：`startPage()` 之后必须紧跟查询，
> 中间插入其他查询会导致分页参数被错误消费。

### 3. 登录认证与上下文传递

```
登录 → 签发 JWT
  ↓
后续请求携带 token
  ↓
拦截器 preHandle 解析 token，把员工 id 存入 ThreadLocal
  ↓
Service / AOP 通过 CurrentHolder.getCurrentId() 读取
  ↓
afterCompletion 中 remove()，避免线程池复用导致的数据串号
```

`ThreadLocal` 的 value 是强引用，不清理会造成内存泄漏；
更严重的是线程复用时下一个请求可能读到上一个请求的用户信息。

### 4. 操作日志

自定义注解 + AOP 环绕通知，按需增强而不是一刀切：

```java
@Retention(RetentionPolicy.RUNTIME)   // 必须 RUNTIME，否则运行时读不到
@Target(ElementType.METHOD)
public @interface Log { }
```

```java
@Around("@annotation(com.gaoshuang.anno.Log)")
public Object recordLog(ProceedingJoinPoint joinPoint) throws Throwable {
    long begin = System.currentTimeMillis();
    Object result = joinPoint.proceed();          // 执行目标方法
    long cost = System.currentTimeMillis() - begin;
    // ... 记录操作人、方法名、耗时并落库
    return result;
}
```

用 `@Around` 而不是 `@Before`，因为需要拿到返回值并计算耗时。

## 说明

本项目为个人学习实践项目，用于熟悉 Spring Boot 技术栈的完整落地流程。

阿里云 OSS 的配置留了占位符，不填写只影响文件上传功能，其他模块正常运行。
