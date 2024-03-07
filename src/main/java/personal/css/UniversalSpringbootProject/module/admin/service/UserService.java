package personal.css.UniversalSpringbootProject.module.admin.service;

import personal.css.UniversalSpringbootProject.common.service.MyBaseService;
import personal.css.UniversalSpringbootProject.module.admin.pojo.User;

/**
 * @Description: 用户信息业务处理接口
 * @Author: CSS
 * @Date: 2024/2/29 10:23
 */
public interface UserService extends MyBaseService<User> {

    User getByName(String name);
}
