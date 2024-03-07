package personal.css.UniversalSpringbootProject.common.utils.code;

import personal.css.UniversalSpringbootProject.common.utils.ExceptionUtil;

import java.util.Base64;

/**
 * @Description: Base64编码-解码工具
 * @Author: CSS
 * @Date: 2024/3/4 16:43
 */
public class Base64Util {

    public static String encoderWithBase64(String s){
        try {
            Base64.Encoder encoder = Base64.getEncoder();
            byte[] bytes = s.getBytes();
            return encoder.encodeToString(bytes);
        } catch (Exception e) {
            ExceptionUtil.recordLogAndThrowException(RuntimeException.class, e.getMessage(), "数据编码异常！");
        }
        return null;
    }

    public static String decodeWithBase64(String s) throws RuntimeException{
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] decodedBytes = decoder.decode(s);
            return new String(decodedBytes);
        } catch (Exception e) {
            ExceptionUtil.recordLogAndThrowException(RuntimeException.class, e.getMessage(), "数据解析异常，请确认信息是否正确！");
        }
        return null;
    }
}
