package personal.css.UniversalSpringbootProject.common.service;

import personal.css.UniversalSpringbootProject.common.pojo.Account;

/**
 * @Description: 账号管理相关业务服务层接口
 * @Author: CSS
 * @Date: 2024/3/2 21:00
 */
public interface LoginService {

    Boolean isExist(String name);

    Boolean register(String name, String ep);

    Account getOneByName(String name);
}
