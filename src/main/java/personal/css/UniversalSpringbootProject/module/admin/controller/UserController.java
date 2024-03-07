package personal.css.UniversalSpringbootProject.module.admin.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import personal.css.UniversalSpringbootProject.common.controller.MyBaseController;
import personal.css.UniversalSpringbootProject.common.vo.ResultVo;
import personal.css.UniversalSpringbootProject.module.admin.pojo.User;
import personal.css.UniversalSpringbootProject.module.admin.service.UserService;
import personal.css.UniversalSpringbootProject.module.admin.vo.UserInfo;

import javax.servlet.http.HttpServletRequest;

import static personal.css.UniversalSpringbootProject.common.consts.MyConst.ACCOUNT;

/**
 * @Description: 用户信息控制层
 * @Author: CSS
 * @Date: 2024/2/29 10:22
 */

@Api(tags = "用户(超级管理员可见，除个人信息接口)")
@RestController
@RequestMapping("/admin/user")
public class UserController extends MyBaseController<UserService, User> {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "获取我的信息")
    @GetMapping("/getUserInfo")
    public ResponseEntity<ResultVo<UserInfo>> getUserInfo(@ApiParam(hidden = true) Long userId, HttpServletRequest request){
        String name = (String) request.getAttribute(ACCOUNT);
        User user = userService.getByName(name);

        if(null == user)
            return new ResponseEntity<>(new ResultVo<>(false, "用户状态异常！", null), HttpStatus.UNAUTHORIZED);

        UserInfo userInfo = new UserInfo()
                .setUserId(userId)
                .setName(name)
                .setAge(user.getAge())
                .setEmail(user.getEmail());

        return new ResponseEntity<>(new ResultVo<>(true, null,userInfo), HttpStatus.OK);
    }
}
