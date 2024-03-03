package personal.css.UniversalSpringbootProject.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import personal.css.UniversalSpringbootProject.common.vo.ListResult;
import personal.css.UniversalSpringbootProject.common.vo.SuccessCount;

import java.sql.SQLException;
import java.text.ParseException;

/**
 * @Description: 自定义基础业务实现抽象接口
 * @Author: CSS
 * @Date: 2024/2/29 8:38
 */

public interface MyBaseService<T> extends IService<T> {

    ListResult<T> list(String filter, String order);

    ListResult<T> pageList(String filter, Integer pageNum, Integer pageSize, String order);

    T insert(Long userId, T model);

    T update(Long userId, T model);

    SuccessCount delete(Long userId, Long id);

    String getCreateTableSql() throws SQLException;

    Object queryBySql(String sql) throws SQLException;

    Object insertBySql(String sql) throws SQLException;

    Object updateBySql(String sql) throws SQLException;

    Object deleteBySql(String sql) throws SQLException;

    /**
     * 使用拼接sql方式进行新增(完完全全按照sql执行)
     * @param ps        属性数组
     * @param vs        值数组
     * @return
     * @throws SQLException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    Object insertByPVS(String[] ps, String[] vs) throws SQLException, InstantiationException, IllegalAccessException;

    /**
     * 使用反射调用insert方法进行新增(按我规定的来)
     * @param userId    用户id
     * @param ps        属性数组
     * @param vs        值数组
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     * @throws ParseException
     */
    Object insertByPVS2(Long userId, String[] ps, String[] vs) throws InstantiationException, IllegalAccessException, NoSuchFieldException, ParseException;
}
