package personal.css.UniversalSpringbootProject.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import personal.css.UniversalSpringbootProject.common.mapper.LoginMapper;
import personal.css.UniversalSpringbootProject.common.pojo.Account;
import personal.css.UniversalSpringbootProject.common.service.LoginService;
import personal.css.UniversalSpringbootProject.module.user.pojo.User;
import personal.css.UniversalSpringbootProject.module.user.service.UserService;

/**
 * @Description: 账号管理相关业务服务层实现类
 * @Author: CSS
 * @Date: 2024/3/2 21:01
 */
@Service
public class LoginServiceImpl implements LoginService {

    private static final Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private LoginMapper loginMapper;

    @Autowired
    private UserService userService;


    @Override
    public Boolean isExist(String name) {
        LambdaQueryWrapper<Account> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Account::getName, name);
        return loginMapper.exists(wrapper);
    }


    @Override
    @Transactional
    public User registerAndSaveInfo(Account account, User user) {
        try {
            int insertAccount = loginMapper.insert(account);
            if (insertAccount == 1) {
                user = userService.insert(null, user);
                if(null == user){
                    log.error("新增用户信息失败！");
                    throw new RuntimeException("注册账户失败！");
                }
                return user;
            }else{
                log.error("注册账户失败！");
                throw new RuntimeException("注册账户失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("注册账户失败！");
        }
    }


    @Override
    public Account getOneByName(String name) {
        LambdaQueryWrapper<Account> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Account::getName, name);
        return loginMapper.selectOne(wrapper);
    }
}
