package personal.css.UniversalSpringbootProject.module.log.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;
import personal.css.UniversalSpringbootProject.common.vo.ListResult;
import personal.css.UniversalSpringbootProject.module.account.pojo.ApiLog;
import personal.css.UniversalSpringbootProject.module.log.mapper.ApiLogMapper;
import personal.css.UniversalSpringbootProject.module.log.service.ApiLogService;

import java.util.List;

/**
 * @Description: 接口日志管理业务处理实现类
 * @Author: CSS
 * @Date: 2024/2/29 10:24
 */
@Service
public class ApiLogServiceImpl extends ServiceImpl <ApiLogMapper,ApiLog> implements ApiLogService {

    @Autowired
    private ApiLogMapper apiLogMapper;

    @Override
    public ListResult<ApiLog> list(String filter, String order) throws BadSqlGrammarException {
        QueryWrapper<ApiLog> queryWrapper = new QueryWrapper<>();
        //使用封装类的方法，将filter、order条件填入QueryWrapper<T>的对象queryWrapper
        List<ApiLog> list = super.list(queryWrapper);
        return new ListResult<>(list.size(), list);
    }

    @Override
    public ListResult<ApiLog> pageList(String filter, Integer pageNum, Integer pageSize, String order) throws BadSqlGrammarException {
        QueryWrapper<ApiLog> queryWrapper = new QueryWrapper<>();
        //使用封装类的方法，将filter、order条件填入QueryWrapper<T>的对象queryWrapper
        Page<ApiLog> page = this.page(new Page<>(pageNum, pageSize), queryWrapper);
        return new ListResult<>(page.getTotal(), page.getRecords());
    }

    @Override
    public void insert(ApiLog apiLog) {
        apiLogMapper.insert(apiLog);
    }
}
