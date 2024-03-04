package personal.css.UniversalSpringbootProject.common.utils.code;

import java.util.Base64;

/**
 * @Description: Base64编码-解码工具
 * @Author: CSS
 * @Date: 2024/3/4 16:43
 */
public class Base64Util {

    public static String encoderWithBase64(String s){
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] bytes = s.getBytes();
        return encoder.encodeToString(bytes);
    }

    public static String decodeWithBase64(String s) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decodedBytes = decoder.decode(s);
        return new String(decodedBytes);
    }
}
