package personal.css.UniversalSpringbootProject.module.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import personal.css.UniversalSpringbootProject.module.admin.pojo.ApiLog;

/**
 * @Description: 接口日志管理持久层
 * @Author: CSS
 * @Date: 2024/2/29 10:28
 */
@Mapper
public interface ApiLogMapper extends BaseMapper<ApiLog> {
}
