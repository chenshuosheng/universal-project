package personal.css.UniversalSpringbootProject.module.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import personal.css.UniversalSpringbootProject.module.account.pojo.Account;

/**
 * @Description: 账户持久层
 * @Author: CSS
 * @Date: 2024/2/29 10:28
 */
@Mapper
public interface AccountMapper extends BaseMapper<Account> {
}