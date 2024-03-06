package personal.css.UniversalSpringbootProject.module.loginManage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import personal.css.UniversalSpringbootProject.common.vo.SuccessCount;
import personal.css.UniversalSpringbootProject.module.account.mapper.AccountMapper;
import personal.css.UniversalSpringbootProject.module.account.pojo.Account;
import personal.css.UniversalSpringbootProject.module.account.service.AccountService;
import personal.css.UniversalSpringbootProject.module.loginManage.service.LoginService;
import personal.css.UniversalSpringbootProject.module.user.pojo.User;
import personal.css.UniversalSpringbootProject.module.user.service.UserService;

/**
 * @Description: 登录管理相关业务服务层实现类
 * @Author: CSS
 * @Date: 2024/3/2 21:01
 */
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    //private static final Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;


    @Override
    public Boolean isExist(String name) {
        LambdaQueryWrapper<Account> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Account::getName, name);
        return accountMapper.exists(wrapper);
    }


    @Override
    @Transactional
    public User registerAndSaveInfo(Account account, User user) throws DuplicateKeyException{
        try {
            account = accountService.insert(null, account);
            if (null != account) {
                user = userService.insert(account.getId(), user);
                if (null != user) {
                    return user;
                } else {
                    log.error("新增用户信息失败！");
                    throw new RuntimeException("注册账户失败！");
                }
            } else {
                log.error("注册账户失败！");
                throw new RuntimeException("注册账户失败！");
            }
        } catch (DuplicateKeyException e){
            e.printStackTrace();
            throw new DuplicateKeyException(e.getMessage()+"注册账户失败！");
        } catch(RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException("注册账户失败！");
        }
    }


    @Override
    public Account getOneByName(String name) {
        LambdaQueryWrapper<Account> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Account::getName, name);
        return accountMapper.selectOne(wrapper);
    }

    @Override
    @Transactional
    public Boolean deleteAccountAndUser(Long userId, String name) {
        try {
            //根据账户名获取记录
            Account account = accountService.getByName(name);

            //删除账户记录
            SuccessCount deleteAccount = accountService.delete(userId, account.getId());

            if (deleteAccount.getSuccessCount() == 1) {
                //根据用户(账户)名获取记录
                User user = userService.getByName(name);

                //删除用户信息记录
                SuccessCount deleteUser = userService.delete(userId, user.getId());
                if (deleteUser.getSuccessCount() == 1)
                    return true;
                else {
                    log.error("删除用户失败！");
                    throw new RuntimeException("注销账户失败！");
                }
            } else {
                log.error("删除账户失败！");
                throw new RuntimeException("注销账户失败！");
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException("注销账户失败！");
        }
    }

    @Override
    public Account updatePassword(Long userId, Account account) {
        return accountService.update(userId, account);
    }
}
