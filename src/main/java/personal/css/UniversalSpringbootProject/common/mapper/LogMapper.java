package personal.css.UniversalSpringbootProject.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import personal.css.UniversalSpringbootProject.common.pojo.ProjectLog;

/**
 * @Description: TODO
 * @Author: CSS
 * @Date: 2024/3/6 17:11
 */
@Mapper
public interface LogMapper extends BaseMapper<ProjectLog> {
}
