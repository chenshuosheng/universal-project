package personal.css.UniversalSpringbootProject.common.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: TODO
 * @Author: CSS
 * @Date: 2024/2/29 17:52
 */
@Mapper
@Repository
@DS("universal_project")
public interface CommonMapper extends BaseMapper {

    @Select("select country.name from universal_project.country")
    List<String> getCountries();

    @Select("select nation.name from universal_project.nation")
    List<String> getNations();
}
