package personal.css.UniversalSpringbootProject.module.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import personal.css.UniversalSpringbootProject.module.user.pojo.User;

/**
 * @Description: 用户信息持久层
 * @Author: CSS
 * @Date: 2024/2/29 10:28
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
