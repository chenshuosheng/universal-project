package personal.css.UniversalSpringbootProject.module.test_mybatis_plus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import personal.css.UniversalSpringbootProject.module.test_mybatis_plus.pojo.User;

/**
 * @Description: TODO
 * @Author: CSS
 * @Date: 2024/2/28 16:03
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
