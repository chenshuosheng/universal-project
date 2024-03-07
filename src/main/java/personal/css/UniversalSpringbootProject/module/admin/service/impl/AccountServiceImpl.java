package personal.css.UniversalSpringbootProject.module.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import personal.css.UniversalSpringbootProject.common.service.impl.MyBaseServiceImpl;
import personal.css.UniversalSpringbootProject.module.admin.mapper.AccountMapper;
import personal.css.UniversalSpringbootProject.module.admin.pojo.Account;
import personal.css.UniversalSpringbootProject.module.admin.service.AccountService;

/**
 * @Description: 账户数据管理业务处理实现类
 * @Author: CSS
 * @Date: 2024/2/29 10:24
 */
@Service
public class AccountServiceImpl extends MyBaseServiceImpl<AccountMapper, Account> implements AccountService {
    @Override
    public Account getByName(String name) {
        LambdaQueryWrapper<Account> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Account::getName,name);
        return this.getOne(wrapper);
    }
}
