package personal.css.UniversalSpringbootProject.common.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 转换工具
 */
public class ConvertUtil {

    /**
     * 获取类的所有属性，包括父类
     *
     * @param object
     * @return
     */
    public static Field[] getAllFields(Object object) {
        Class<?> clazz = object.getClass();
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        return fields;
    }

    /**
     * 不为空返回原值，为null则返回""
     *
     * @param s
     * @return
     */
    public static String getString(String s) {
        return getString(s, "");
    }

    /**
     * 不为空返回原值，如果为空则返回第二个参数
     *
     * @param s      不为空返回此字符串
     * @param defval 为空则返回此字符串
     * @return
     */
    public static String getString(String s, String defval) {
        if (isEmpty(s)) {
            return (defval);
        }
        return s.trim();
    }

    /**
     * 判断是否为空
     *
     * @param object
     * @return
     */
    public static boolean isEmpty(Object object) {
        if (object == null) {
            return (true);
        }
        if ("".equals(object)) {
            return (true);
        }
        if ("null".equals(object)) {
            return (true);
        }
        return (false);
    }

    /**
     * 判断是否不为空
     *
     * @param object
     * @return
     */
    public static boolean isNotEmpty(Object object) {
        return object != null && !object.equals("") && !object.equals("null");
    }


    /**
     * 将驼峰命名转化成下划线
     * @param para
     * @return
     */
    public static String camelToUnderline(String para){
        if(para.length()<3){
            return para.toLowerCase();
        }
        StringBuilder sb=new StringBuilder(para);
        int temp=0;//定位
        //从第三个字符开始 避免命名不规范
        for(int i=2;i<para.length();i++){
            if(Character.isUpperCase(para.charAt(i))){
                sb.insert(i+temp, "_");
                temp+=1;
            }
        }
        return sb.toString().toLowerCase();
    }

}
