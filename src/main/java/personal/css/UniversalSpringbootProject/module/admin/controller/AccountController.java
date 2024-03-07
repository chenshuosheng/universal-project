package personal.css.UniversalSpringbootProject.module.admin.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import personal.css.UniversalSpringbootProject.common.controller.MyBaseController;
import personal.css.UniversalSpringbootProject.module.admin.pojo.Account;
import personal.css.UniversalSpringbootProject.module.admin.service.AccountService;

/**
 * @Description: 账户数据管理控制层
 * @Author: CSS
 * @Date: 2024/2/29 10:22
 */

@Api(tags = "账户数据管理(超级管理员可见)")
@RestController
@RequestMapping("/admin/account")
public class AccountController extends MyBaseController<AccountService, Account> {
}
