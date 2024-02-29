package personal.css.UniversalSpringbootProject.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import personal.css.UniversalSpringbootProject.common.vo.ListResult;
import personal.css.UniversalSpringbootProject.common.vo.SuccessCount;

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

    SuccessCount delete(Long userId, String id);
}
