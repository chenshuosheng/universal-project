package personal.css.UniversalSpringbootProject.module.account.service;

import personal.css.UniversalSpringbootProject.common.service.MyBaseService;
import personal.css.UniversalSpringbootProject.module.account.pojo.Account;

/**
 * @Description: 账户业务处理接口
 * @Author: CSS
 * @Date: 2024/2/29 10:23
 */
public interface AccountService extends MyBaseService<Account> {
    Account getByName(String name);
}
