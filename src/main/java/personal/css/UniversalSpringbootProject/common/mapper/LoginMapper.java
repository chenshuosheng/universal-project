package personal.css.UniversalSpringbootProject.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import personal.css.UniversalSpringbootProject.common.pojo.Account;

/**
 * @Description: 账号管理相关持久层接口
 * @Author: CSS
 * @Date: 2024/3/2 21:03
 */
@Mapper
public interface LoginMapper extends BaseMapper<Account> {

}
