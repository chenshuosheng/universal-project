package personal.css.UniversalSpringbootProject.module.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import personal.css.UniversalSpringbootProject.common.service.impl.MyBaseServiceImpl;
import personal.css.UniversalSpringbootProject.module.user.mapper.UserMapper;
import personal.css.UniversalSpringbootProject.module.user.pojo.User;
import personal.css.UniversalSpringbootProject.module.user.service.UserService;

/**
 * @Description: 用户信息业务处理实现类
 * @Author: CSS
 * @Date: 2024/2/29 10:24
 */
@Service
public class UserServiceImpl extends MyBaseServiceImpl<UserMapper, User> implements UserService {
    @Override
    public User getByName(String name) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getName, name);
        return this.baseMapper.selectOne(wrapper);
    }
}
