package personal.css.UniversalSpringbootProject;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@ServletComponentScan  //启用Servlet组件扫描，以便识别和注册过滤器
@EnableSwaggerBootstrapUI
@SpringBootApplication
public class UniversalSpringbootProjectApplication {

    private static final Logger log = LoggerFactory.getLogger(UniversalSpringbootProjectApplication.class);

    public static void main(String[] args) throws UnknownHostException {

        ApplicationContext context = SpringApplication.run(UniversalSpringbootProjectApplication.class, args);

        //电脑名称+'/'+ip地址
        InetAddress localHost = InetAddress.getLocalHost();
        //电脑名称
        String hostName = InetAddress.getLocalHost().getHostName();
        //ip地址(如：192.168.159.1)
        String hostAddress = localHost.getHostAddress();

        //localhost/127.0.0.1
        InetAddress loopbackAddress = InetAddress.getLoopbackAddress();
        String localhost = loopbackAddress.getHostName();
        String localhostIp = loopbackAddress.getHostAddress();

        Environment env = context.getEnvironment();
        String port = env.getProperty("server.port");

        log.info("\n---------------------------------------------------------------" +
                        "\n本地knif4j文档地址：\thttp://{}:{}/doc.html" +
                        "\n联网knif4j文档地址：\thttp://{}:{}/doc.html" +
                        "\n---------------------------------------------------------------"
                , localhostIp, port, hostAddress, port);
    }

}
