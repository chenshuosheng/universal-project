package personal.css.UniversalSpringbootProject;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import personal.css.UniversalSpringbootProject.module.test_mybatis_plus.mapper.UserMapper_0;
import personal.css.UniversalSpringbootProject.module.test_mybatis_plus.pojo.User;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class UniversalSpringbootProjectApplicationTests {

    @Autowired
    public UserMapper_0 userMapper0;

    @Test
    void contextLoads() {
        List<User> users = userMapper0.selectList(null);
        users.forEach(System.out::println);
    }

    @Test
    void testPage(){
        Page<User> userPage = new Page<>(1,3);
        userPage = userMapper0.selectPage(userPage, null);
        System.out.println(userPage.getTotal());
        userPage.getRecords().forEach(System.out::println);
    }


    @Test
    void testJWT(){
        Map<String,Object> map = new HashMap<>();
        map.put("id",1001L);
        map.put("username","css");

        String token = JWT.create()
                .withClaim("claim", map)
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .sign(Algorithm.HMAC256("universalSpringbootProject"));

        System.out.println(token);
    }
}
