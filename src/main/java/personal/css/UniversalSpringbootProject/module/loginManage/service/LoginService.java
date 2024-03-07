package personal.css.UniversalSpringbootProject.module.loginManage.service;

import personal.css.UniversalSpringbootProject.module.admin.pojo.Account;
import personal.css.UniversalSpringbootProject.module.admin.pojo.User;

/**
 * @Description: 登录管理相关业务服务层接口
 * @Author: CSS
 * @Date: 2024/3/2 21:00
 */
public interface LoginService {

    Boolean isExist(String name);

    User registerAndSaveInfo(Account account, User user);

    Account getOneByName(String name);

    Boolean deleteAccountAndUser(Long userId, String name);

    Account updatePassword(Long userId, Account account);
}
