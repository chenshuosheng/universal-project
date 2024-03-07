package personal.css.UniversalSpringbootProject.module.admin.service;

import personal.css.UniversalSpringbootProject.common.service.MyBaseService;
import personal.css.UniversalSpringbootProject.module.admin.pojo.Account;

/**
 * @Description: 账户数据管理业务处理接口
 * @Author: CSS
 * @Date: 2024/2/29 10:23
 */
public interface AccountService extends MyBaseService<Account> {
    Account getByName(String name);
}
