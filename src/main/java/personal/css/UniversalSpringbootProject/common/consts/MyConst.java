package personal.css.UniversalSpringbootProject.common.consts;

/**
 * @Description: 全局常量
 * @Author: CSS
 * @Date: 2024/2/29 14:33
 */

public interface MyConst {

    //平台id键名
    public static final String ABP_TENANT_ID = "Abp.Tenantid";
    //token键名
    public static final String AUTHORIZATION = "Authorization";
    //过期时间
    public  static final Long EXPIRATION_TIME = 60000L;       //单位毫秒，1分钟
    //用户id键名，在过滤器中从token中解析得到
    public static final String USER_ID = "userId";
    //账户名键名，在过滤器中从token中解析得到
    public static final String ACCOUNT = "account";


    //密钥
    public static final String SECRET = "universalSpringbootProject";


    //msql驱动
    public static final String DRIVER_CLASSNAME = "com.mysql.cj.jdbc.Driver";

    //连接url
    public static final String BASEURL = "jdbc:mysql://localhost:3306/";

    //用户名
    public static final String USERNAME = "root";

    //密码
    public static final String PASSWORD = "123456";
    //数据库名
    public static final String DATABASE = "universal_project";
}
