package personal.css.UniversalSpringbootProject.module.loginManage.controller;

import com.auth0.jwt.algorithms.Algorithm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import personal.css.UniversalSpringbootProject.common.utils.JWTUtil;
import personal.css.UniversalSpringbootProject.common.utils.Md5Util;
import personal.css.UniversalSpringbootProject.common.vo.ResultVo;
import personal.css.UniversalSpringbootProject.module.account.pojo.Account;
import personal.css.UniversalSpringbootProject.module.loginManage.service.LoginService;
import personal.css.UniversalSpringbootProject.module.loginManage.vo.RegisterVo;
import personal.css.UniversalSpringbootProject.module.loginManage.vo.UpdatePasswordVo;
import personal.css.UniversalSpringbootProject.module.user.pojo.User;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static personal.css.UniversalSpringbootProject.common.consts.MyConst.EXPIRATION_TIME;
import static personal.css.UniversalSpringbootProject.common.consts.MyConst.SECRET;

/**
 * @Description: 登录管理相关控制层接口
 * @Author: CSS
 * @Date: 2024/3/2 20:56
 */

@Api(value = "/api", tags = "登录管理")
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;


    @ApiOperation(value = "注册账户")
    @PostMapping("api/public/register")
    public ResponseEntity<ResultVo<String>> registerPublic(@Validated @RequestBody RegisterVo model) throws DuplicateKeyException {
        String name = model.getName();
        Boolean exist = loginService.isExist(name);

        if (exist)
            return new ResponseEntity<>(new ResultVo(false, "账号已存在！", null), HttpStatus.BAD_REQUEST);

        String password = model.getPassword();
        Integer age = model.getAge();
        String email = model.getEmail();

        //使用MD5加密密码
        String ep = Md5Util.getMD5String(password);

        //注册账号信息
        Account account = new Account();
        account.setName(name)
                .setPassword(ep);

        //保存用户信息
        User user = new User();
        user.setName(name).setAge(age).setEmail(email);

        //注册账号并保存用户信息
        return new ResponseEntity<>(new ResultVo(true, null, loginService.registerAndSaveInfo(account, user)), HttpStatus.OK);
    }


    @ApiOperation(value = "登录", notes = "使用账户名+密码进行登录")
    @PostMapping("api/public/login")
    public ResponseEntity<ResultVo<String>> loginPublic(@RequestParam String name, @RequestParam String password, HttpServletResponse response) {
        Account account = loginService.getOneByName(name);

        if (null == account)
            return new ResponseEntity<>(new ResultVo(false, "账户不存在！", null), HttpStatus.NOT_FOUND);

        //校对密码
        if (Md5Util.checkPassword(password, account.getPassword())) {
            //有效载荷主体信息
            Map<String, String> map = new HashMap<>();
            map.put("id", account.getId().toString());
            map.put("name", account.getName());

            //过期时间
            Date expires = new Date(System.currentTimeMillis() + EXPIRATION_TIME);

            //使用密钥与加密算法得到的签名
            Algorithm algorithm = Algorithm.HMAC256(SECRET);

            //生成token
            String jwt = JWTUtil.createJWT(map, expires, algorithm);

            String token = "Bearer " + jwt;

            //将身份令牌存储到客户端Cookie
            JWTUtil.setTokenToCookie(response, jwt);

            return new ResponseEntity<>(new ResultVo(false, null, token), HttpStatus.OK);
        }

        return new ResponseEntity<>(new ResultVo(false, "密码错误！登录失败！", null), HttpStatus.BAD_REQUEST);
    }


    @ApiOperation(value = "注销账户", notes = "使用唯一账户名+密码进行注销")
    @PostMapping("/loginManage/deleteAccount")
    public ResponseEntity<ResultVo<String>> deleteAccount(@ApiParam(hidden = true) Long userId, @RequestParam String name, @RequestParam String password) {
        Account account = loginService.getOneByName(name);

        if (null == account)
            return new ResponseEntity<>(new ResultVo(false, "账户不存在！", null), HttpStatus.NOT_FOUND);

        //校对密码
        if (Md5Util.checkPassword(password, account.getPassword())) {
            //删除账户和用户信息
            Boolean success = loginService.deleteAccountAndUser(userId, name);
            if (success)
                return new ResponseEntity<>(new ResultVo(false, "注销成功！期待与您的下次相见！", null), HttpStatus.BAD_REQUEST);
            else
                return new ResponseEntity<>(new ResultVo(false, "注销失败！", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new ResultVo(false, "密码错误！删除失败！", null), HttpStatus.BAD_REQUEST);
    }


    @ApiOperation(value = "退出登录")
    @PostMapping("/loginManage/logout")
    public ResponseEntity<ResultVo<String>> logout(@ApiParam(hidden = true) Long userId) {
        return new ResponseEntity<>(new ResultVo(true, null, "退出成功！"), HttpStatus.OK);
    }


    @ApiOperation(value = "修改密码")
    @PutMapping("/loginManage/updatePassword")
    public ResponseEntity<ResultVo<String>> updatePassword(@ApiParam(hidden = true) Long userId, @Validated @RequestBody UpdatePasswordVo model) {
        Account account = loginService.getOneByName(model.getName());

        if (null == account)
            return new ResponseEntity<>(new ResultVo(false, "账户不存在！", null), HttpStatus.NOT_FOUND);

        //校对原密码
        if (Md5Util.checkPassword(model.getOldPassword(), account.getPassword())) {
            //使用MD5加密密码
            String ep = Md5Util.getMD5String(model.getNewPassword());
            account.setPassword(ep);

            //修改密码
            account = loginService.updatePassword(userId, account);
            if (null != account)
                return new ResponseEntity<>(new ResultVo(false, null, "恭喜您，密码修改成功！"), HttpStatus.OK);
        }

        return new ResponseEntity<>(new ResultVo(false, "原密码错误！修改失败！", null), HttpStatus.BAD_REQUEST);
    }
}
