package personal.css.UniversalSpringbootProject.common.controller;

import com.auth0.jwt.algorithms.Algorithm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import personal.css.UniversalSpringbootProject.common.pojo.Account;
import personal.css.UniversalSpringbootProject.common.service.LoginService;
import personal.css.UniversalSpringbootProject.common.utils.JWTUtil;
import personal.css.UniversalSpringbootProject.common.vo.ResultVo;
import personal.css.UniversalSpringbootProject.common.vo.UserVo;
import personal.css.UniversalSpringbootProject.module.user.pojo.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static personal.css.UniversalSpringbootProject.common.consts.MyConst.SECRET;

/**
 * @Description: 账号管理相关控制层接口
 * @Author: CSS
 * @Date: 2024/3/2 20:56
 */

@Api(value = "/api", tags = "登录")
@RestController
@RequestMapping("/account")
public class LoginController {

    @Autowired
    private LoginService loginService;


    @ApiOperation(value = "注册", notes = "使用唯一账户名+密码进行注册")
    @PostMapping("/register")
    public ResponseEntity<ResultVo<String>> registerPublic(@Validated @RequestBody UserVo model) {
        String name = model.getName();
        Boolean exist = loginService.isExist(name);

        if (exist)
            throw new RuntimeException("账号已存在！");

        String password = model.getPassword();
        Integer age = model.getAge();
        String email = model.getEmail();

        //加密密码
        String ep = password;

        //注册账号信息
        Account account = new Account(null, name, ep);

        //保存用户信息
        User user = new User();
        user.setName(name).setAge(age).setEmail(email);

        //注册账号并保存用户信息
        return new ResponseEntity<>(new ResultVo(true, null, loginService.registerAndSaveInfo(account, user)), HttpStatus.OK);
    }


    @ApiOperation(value = "登录", notes = "使用账户名+密码进行登录")
    @PostMapping("/login")
    public ResponseEntity<ResultVo<String>> loginPublic(@RequestParam String name, @RequestParam String password) {
        Account account = loginService.getOneByName(name);

        if (null == account)
            throw new RuntimeException("账号不存在！");

        //加密密码
        String ep = password;

        //比对密码
        if (account.getPassword().equals(ep)) {
            //有效载荷主体信息
            Map<String, String> map = new HashMap<>();
            map.put("id", account.getId().toString());
            map.put("name", account.getName());

            //过期时间
            Date expires = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24);

            //使用密钥与加密算法得到的签名
            Algorithm algorithm = Algorithm.HMAC256(SECRET);

            //生成token
            String jwt = JWTUtil.createJWT(map, expires, algorithm);

            String token = "Bearer " + jwt;

            return new ResponseEntity<>(new ResultVo(false, null, token), HttpStatus.OK);
        }

        return new ResponseEntity<>(new ResultVo(false, "密码错误！登录失败！", null), HttpStatus.BAD_REQUEST);
    }
}
