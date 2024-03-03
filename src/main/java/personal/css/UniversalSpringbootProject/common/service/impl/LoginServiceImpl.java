package personal.css.UniversalSpringbootProject.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import personal.css.UniversalSpringbootProject.common.mapper.LoginMapper;
import personal.css.UniversalSpringbootProject.common.pojo.Account;
import personal.css.UniversalSpringbootProject.common.service.LoginService;

/**
 * @Description: 账号管理相关业务服务层实现类
 * @Author: CSS
 * @Date: 2024/3/2 21:01
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginMapper loginMapper;


    @Override
    public Boolean isExist(String name) {
        LambdaQueryWrapper<Account> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Account::getName,name);
        return loginMapper.exists(wrapper);
    }


    @Override
    public Boolean register(String name, String ep) {
        return loginMapper.insert(new Account(null,name,ep)) == 1 ? true :false;
    }


    @Override
    public Account getOneByName(String name) {
        LambdaQueryWrapper<Account> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Account::getName,name);
        return loginMapper.selectOne(wrapper);
    }
}
