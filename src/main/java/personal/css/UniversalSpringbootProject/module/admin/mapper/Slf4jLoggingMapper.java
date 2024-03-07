package personal.css.UniversalSpringbootProject.module.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import personal.css.UniversalSpringbootProject.module.admin.pojo.Slf4jLogging;

/**
 * @Description: Slf4j日志持久层接口
 * @Author: CSS
 * @Date: 2024/3/6 16:06
 */
@Mapper
public interface Slf4jLoggingMapper extends BaseMapper<Slf4jLogging> {
}
