package personal.css.UniversalSpringbootProject.module.user.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import personal.css.UniversalSpringbootProject.common.controller.MyBaseController;
import personal.css.UniversalSpringbootProject.module.user.pojo.User;
import personal.css.UniversalSpringbootProject.module.user.service.UserService;

/**
 * @Description: 用户信息控制层
 * @Author: CSS
 * @Date: 2024/2/29 10:22
 */

@Api(tags = "用户")
@RestController
@RequestMapping("/user")
public class UserController extends MyBaseController<UserService, User> {
}
