package personal.css.UniversalSpringbootProject.common.service.impl;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import personal.css.UniversalSpringbootProject.common.pojo.BaseEntity;
import personal.css.UniversalSpringbootProject.common.service.MyBaseService;
import personal.css.UniversalSpringbootProject.common.vo.ListResult;
import personal.css.UniversalSpringbootProject.common.vo.SuccessCount;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 自定义基础业务实现抽象类
 * @Author: CSS
 * @Date: 2024/2/29 8:38
 */
@Service
public abstract class MyBaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> implements MyBaseService<T> {

    private final String TABLE_NAME = this.entityClass.getAnnotation(TableName.class).value();

    private Map<String, String> COLUMN_MAP = new HashMap<>();

    protected MyBaseServiceImpl(){
        try {
            Field column_map = this.entityClass.getField("COLUMN_MAP" );
            this.COLUMN_MAP = (Map<String, String>) column_map.get(null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ListResult<T> list(String filter, String order) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        //使用封装类的方法，将filter、order条件填入QueryWrapper<T>的对象queryWrapper
        List<T> list = super.list(queryWrapper);
        return new ListResult<>(list.size(), list);
    }

    @Override
    public ListResult<T> pageList(String filter, Integer pageNum, Integer pageSize, String order) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        //使用封装类的方法，将filter、order条件填入QueryWrapper<T>的对象queryWrapper
        Page<T> page = this.page(new Page<>(pageNum, pageSize), queryWrapper);
        return new ListResult<>(page.getTotal(), page.getRecords());
    }

    @Override
    public T insert(Long userId, T model) {
        model.setCreatorUserId(userId);
        model.setCreationTime(new Date());
        model.setIsDeleted(false);
        this.save(model);
        //这里会包含乐观锁版本信息？
        return model;
    }

    @Override
    public T update(Long userId, T model) {
        model.setLastModifierUserId(userId);
        if (!updateWithBooleanResult(model)) {
            throw new RuntimeException("当前数据已被修改，请获取最新数据后重新进行修改" );
        }
        //更新成功，返回最新数据
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(T::getId, model.getId());
        return this.getOne(wrapper);
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
    public SuccessCount delete(Long userId, String id) {
        //查询记录
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(T::getId, id);
        T model = this.getOne(wrapper);
        //为什么不直接使用 T model = this.getById(id);

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
}
