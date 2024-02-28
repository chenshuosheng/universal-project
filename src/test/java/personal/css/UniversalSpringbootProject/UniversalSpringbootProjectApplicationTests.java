package personal.css.UniversalSpringbootProject;

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

}
