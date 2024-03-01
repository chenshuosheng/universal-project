package personal.css.UniversalSpringbootProject.common.utils.sql;


import org.apache.commons.lang3.StringUtils;

/**
 * @Description: sql校验工具
 * @Author: CSS
 * @Date: 2024/3/1 14:57
 */
public class SqlValidateUtil {


    //单表sql操作限制注入词
    public static final String SINGLE_TABLE_SQL_REGEX = "show |create |drop |alter |join |count |exec |chr |mid |master |truncate |char |declare ";

    public static Boolean detectSQLInjectionRisk(String value) {
        if (StringUtils.isEmpty(value)) {
            return false;
        }
        String[] sqlKeywords = StringUtils.split(SINGLE_TABLE_SQL_REGEX, "\\|");
        for (String sqlKeyword : sqlKeywords) {
            if (StringUtils.indexOfIgnoreCase(value, sqlKeyword) > -1) {
                return true;
            }
        }
        return false;
    }
}
