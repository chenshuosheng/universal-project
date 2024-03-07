package personal.css.UniversalSpringbootProject.module.log.service;

import com.baomidou.mybatisplus.extension.service.IService;
import personal.css.UniversalSpringbootProject.common.vo.ListResult;
import personal.css.UniversalSpringbootProject.module.account.pojo.ApiLog;

/**
 * @Description: 接口日志管理业务处理接口
 * @Author: CSS
 * @Date: 2024/2/29 10:23
 */
public interface ApiLogService extends IService<ApiLog> {
    ListResult<ApiLog> list(String filter, String order);

    ListResult<ApiLog> pageList(String filter, Integer pageNum, Integer pageSize, String order);

    void insert(ApiLog apiLog);
}
