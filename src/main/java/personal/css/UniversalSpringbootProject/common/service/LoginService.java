package personal.css.UniversalSpringbootProject.common.service;

import personal.css.UniversalSpringbootProject.common.pojo.Account;
import personal.css.UniversalSpringbootProject.module.user.pojo.User;

/**
 * @Description: 账号管理相关业务服务层接口
 * @Author: CSS
 * @Date: 2024/3/2 21:00
 */
public interface LoginService {

    Boolean isExist(String name);

    User registerAndSaveInfo(Account account, User user);

    Account getOneByName(String name);
}
