package personal.css.UniversalSpringbootProject.common.service.impl;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;
import personal.css.UniversalSpringbootProject.common.pojo.BaseEntity;
import personal.css.UniversalSpringbootProject.common.service.MyBaseService;
import personal.css.UniversalSpringbootProject.common.utils.sql.ExecuteSQL;
import personal.css.UniversalSpringbootProject.common.utils.sql.SqlValidateUtil;
import personal.css.UniversalSpringbootProject.common.vo.ListResult;
import personal.css.UniversalSpringbootProject.common.vo.SuccessCount;
import personal.css.UniversalSpringbootProject.module.admin.pojo.Account;
import personal.css.UniversalSpringbootProject.module.admin.pojo.User;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: 自定义基础业务实现抽象类
 * @Author: CSS
 * @Date: 2024/2/29 8:38
 */
public abstract class MyBaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> implements MyBaseService<T> {

    private final String TABLE_NAME = this.entityClass.getAnnotation(TableName.class).value();

    private Map<String, String> COLUMN_MAP = new HashMap<>();

    protected MyBaseServiceImpl() {
        try {
            Field column_map = this.entityClass.getField("COLUMN_MAP");
            this.COLUMN_MAP = (Map<String, String>) column_map.get(null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ListResult<T> list(String filter, String order) throws BadSqlGrammarException {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        //使用封装类的方法，将filter、order条件填入QueryWrapper<T>的对象queryWrapper
        List<T> list = super.list(queryWrapper);
        return new ListResult<>(list.size(), list);
    }

    @Override
    public ListResult<T> pageList(String filter, Integer pageNum, Integer pageSize, String order) throws BadSqlGrammarException {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        //使用封装类的方法，将filter、order条件填入QueryWrapper<T>的对象queryWrapper
        Page<T> page = this.page(new Page<>(pageNum, pageSize), queryWrapper);
        return new ListResult<>(page.getTotal(), page.getRecords());
    }

    @Override
    public T insert(Long userId, T model) {
        try {
            model.setCreatorUserId(userId);
            model.setCreationTime(new Date());
            model.setIsDeleted(false);
            //插入会自动生成id
            this.save(model);
        } catch (DuplicateKeyException e) {
            if(model instanceof User || model instanceof Account)
            throw new DuplicateKeyException("账户名(用户名)已存在！");
        }
        return this.getById(model.getId());
    }

    @Override
    public T update(Long userId, T model) {
        model.setLastModifierUserId(userId);
        if (!updateWithBooleanResult(model)) {
            throw new RuntimeException("当前数据已被修改，请获取最新数据后重新进行修改");
        }
        //更新成功，返回最新数据
        return this.getById(model.getId());
    }


    //根据对比乐观锁版本进行判断‘数据是否已被修改’
    //若未被修改，则更新成功并返回true
    //若已被修改，则更新失败并返回false
    private Boolean updateWithBooleanResult(T model) {
        model.setLastModificationTime(new Date());
        model.setDeletionTime(null);
        model.setDeleterUserId(null);
        return this.updateById(model);
    }

    @Override
    public SuccessCount delete(Long userId, Long id) {
        //查询记录
        T model = this.getById(id);
        //不可以使用下列方法: 泛型问题
        //异常：nested exception is org.apache.ibatis.builder.BuilderException:
        // Error evaluating expression 'ew.sqlSegment != null and ew.sqlSegment != '' and ew.nonEmptyOfNormal'.
        // Cause: org.apache.ibatis.ognl.OgnlException: sqlSegment [com.baomidou.mybatisplus.core.exceptions.MybatisPlusException:
        // can not find lambda cache for this entity [personal.css.UniversalSpringbootProject.common.pojo.BaseEntity]]
        //LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>();
        //wrapper.eq(T::getId, id);
        //T model = this.getOne(wrapper);

        //如果得到记录为null，说明不能删除
        if (model == null) {
            return new SuccessCount(0);
        }

        //更新记录
        model.setDeletionTime(new Date());
        model.setDeleterUserId(userId);
        //model.setId(id);  //似乎是多余的
        this.updateById(model);

        //逻辑删除记录
        return new SuccessCount(this.removeById(id) ? 1 : 0);
    }

    @Override
    public String getCreateTableSql() throws SQLException {
        String table = this.TABLE_NAME;
        return ExecuteSQL.getTableSql(table);
    }

    @Override
    public Object queryBySql(String sql) throws SQLException {
        if (SqlValidateUtil.detectSQLInjectionRisk(sql)) {
            throw new RuntimeException("参数存在SQL注入风险");
        }
        sql = sql.replaceAll("TABLE_NAME", this.TABLE_NAME);
        return ExecuteSQL.executeQuery(sql);
    }

    @Override
    public Object insertBySql(String sql) throws SQLException {
        if (SqlValidateUtil.detectSQLInjectionRisk(sql)) {
            throw new RuntimeException("参数存在SQL注入风险");
        }
        sql = sql.replaceAll("TABLE_NAME", this.TABLE_NAME);
        return ExecuteSQL.executeInsert(sql);
    }


    @Override
    public Object updateBySql(String sql) throws SQLException {
        if (SqlValidateUtil.detectSQLInjectionRisk(sql)) {
            throw new RuntimeException("参数存在SQL注入风险");
        }
        sql = sql.replaceAll("TABLE_NAME", this.TABLE_NAME);
        //return ExecuteSQL.executeUpdate(sql);
        return ExecuteSQL.executeInsert(sql);
    }

    @Override
    public Object deleteBySql(String sql) throws SQLException {
        if (SqlValidateUtil.detectSQLInjectionRisk(sql)) {
            throw new RuntimeException("参数存在SQL注入风险");
        }
        sql = sql.replaceAll("TABLE_NAME", this.TABLE_NAME);
        //return ExecuteSQL.executeDelete(sql);
        return ExecuteSQL.executeInsert(sql);
    }


    @Override
    public Object insertByPVS(String[] ps, String[] vs) throws SQLException {
        //使用拼接sql的方式
        StringJoiner sj = new StringJoiner(",", "(", ")");
        for (int i = 0; i < ps.length; i++) {
            String s = this.COLUMN_MAP.get(ps[i]);
            if (null == s)
                throw new RuntimeException(ps[i] + "属性不存在");
            else
                sj.add(s);
        }

        StringJoiner sj2 = new StringJoiner(",", "(", ")");
        for (int i = 0; i < vs.length; i++) {
            sj2.add(vs[i]);
        }

        StringBuilder sql = new StringBuilder()
                .append("INSERT INTO ")
                .append(this.TABLE_NAME)
                .append(sj)
                .append(" VALUES")
                .append(sj2);
        return ExecuteSQL.executeInsert(sql.toString());
    }


    @Override
    public Object insertByPVS2(Long userId, String[] ps, String[] vs) throws InstantiationException, IllegalAccessException, NoSuchFieldException, ParseException {
        //使用直接赋值的方式
        T t = this.getEntityClass().newInstance();
        StringJoiner sj = new StringJoiner(",", "(", ")");
        for (int i = 0; i < ps.length; i++) {
            Field field = null;
            try {
                field = this.entityClass.getDeclaredField(ps[i]);
            } catch (Exception e) {
                field = this.entityClass.getSuperclass().getDeclaredField(ps[i]);
            }

            if (null == field)
                throw new RuntimeException(ps[i] + "属性不存在");
            else {
                //开启属性访问权限
                field.setAccessible(true);
                //对日期数据进行处理
                if (field.getType().newInstance() instanceof Date) {
                    //去除日期时间的单引号
                    vs[i] = vs[i].replaceAll("'","");
                    ThreadLocal<SimpleDateFormat> dateFormat = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                    SimpleDateFormat sdf = dateFormat.get();
                    Date date = sdf.parse(vs[i]);
                    field.set(t, date);
                } else {
                    field.set(t, vs[i]);
                }
            }
        }

        return this.insert(userId, t);
    }
}
