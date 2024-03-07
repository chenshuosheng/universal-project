package personal.css.UniversalSpringbootProject.module.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;
import personal.css.UniversalSpringbootProject.module.admin.mapper.Slf4jLoggingMapper;
import personal.css.UniversalSpringbootProject.common.vo.ListResult;
import personal.css.UniversalSpringbootProject.module.admin.pojo.Slf4jLogging;
import personal.css.UniversalSpringbootProject.module.admin.service.Slf4jLoggingService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @Description: Slf4j日志业务处理实现类
 * @Author: CSS
 * @Date: 2024/3/6 15:52
 */
@Service
public class Slf4jLoggingServiceImpl extends ServiceImpl<Slf4jLoggingMapper, Slf4jLogging> implements Slf4jLoggingService {

    @Autowired
    private Slf4jLoggingMapper slf4jLoggingMapper;

    @Override
    public void asyncSave(Slf4jLogging log) {
        CompletableFuture<Void> async = CompletableFuture.supplyAsync(() -> {
            slf4jLoggingMapper.insert(log);
            return null;
        });
    }

    @Override
    public ListResult<Slf4jLogging> list(String filter, String order) throws BadSqlGrammarException {
        QueryWrapper<Slf4jLogging> queryWrapper = new QueryWrapper<>();
        //使用封装类的方法，将filter、order条件填入QueryWrapper<T>的对象queryWrapper
        List<Slf4jLogging> list = super.list(queryWrapper);
        return new ListResult<>(list.size(), list);
    }

    @Override
    public ListResult<Slf4jLogging> pageList(String filter, Integer pageNum, Integer pageSize, String order) throws BadSqlGrammarException {
        QueryWrapper<Slf4jLogging> queryWrapper = new QueryWrapper<>();
        //使用封装类的方法，将filter、order条件填入QueryWrapper<T>的对象queryWrapper
        Page<Slf4jLogging> page = this.page(new Page<>(pageNum, pageSize), queryWrapper);
        return new ListResult<>(page.getTotal(), page.getRecords());
    }
}
