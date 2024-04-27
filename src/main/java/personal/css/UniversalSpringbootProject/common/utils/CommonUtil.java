package personal.css.UniversalSpringbootProject.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * @Description: TODO
 * @Author: CSS
 * @Date: 2024/1/10 14:05
 */

public class CommonUtil {

    //用于对象更新
    public static void handleSectionUpdate(JSONObject modelObject, JSONObject newObject) {
        Set<Map.Entry<String, Object>> entries = modelObject.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            Object value = entry.getValue();
            boolean flag = value instanceof String;
            //if (flag && StringUtils.isNotBlank(value.toString()) ||  !flag && value != null) {
            //    newObject.put(entry.getKey(), value);
            //}
            if (flag && value != null)
                newObject.put(entry.getKey(), value);
        }
    }

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }
}
