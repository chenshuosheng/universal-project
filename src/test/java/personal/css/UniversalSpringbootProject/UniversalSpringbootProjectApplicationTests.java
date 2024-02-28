package personal.css.UniversalSpringbootProject;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import personal.css.UniversalSpringbootProject.module.test_mybatis_plus.mapper.UserMapper;
import personal.css.UniversalSpringbootProject.module.test_mybatis_plus.pojo.User;

import java.util.List;

@SpringBootTest
class UniversalSpringbootProjectApplicationTests {

    @Autowired
    public UserMapper userMapper;

    @Test
    void contextLoads() {
        List<User> users = userMapper.selectList(null);
        users.forEach(System.out::println);
    }

    @Test
    void testPage(){
        Page<User> userPage = new Page<>(1,3);
        userPage = userMapper.selectPage(userPage, null);
        System.out.println(userPage.getTotal());
        userPage.getRecords().forEach(System.out::println);
    }
}
