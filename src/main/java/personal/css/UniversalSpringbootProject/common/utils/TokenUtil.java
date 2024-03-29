package personal.css.UniversalSpringbootProject.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import personal.css.UniversalSpringbootProject.common.utils.code.Base64Util;
import personal.css.UniversalSpringbootProject.module.loginManage.vo.TokenVo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static personal.css.UniversalSpringbootProject.common.consts.MyConst.EXPIRATION_TIME;
import static personal.css.UniversalSpringbootProject.common.consts.MyConst.SECRET;

/**
 * @Description: Token相关工具
 * @Author: CSS
 * @Date: 2024/3/3 0:22
 */
public class TokenUtil {


    /**
     * 创建JWT令牌
     *
     * @param claims    有效载荷主体信息
     * @param expires   过期时间
     * @param algorithm 使用密钥与加密算法得到的签名
     * @return
     */
    public static String createJWT(Map<String, Object> claims, Date expires, Algorithm algorithm) {
        JWTCreator.Builder builder = JWT.create();

        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Double) {
                builder.withClaim(key, (Double) value);
            } else if (value instanceof Long) {
                builder.withClaim(key, (Long) value);
            } else if (value instanceof Integer) {
                builder.withClaim(key, (Integer) value);
            } else if (value instanceof Boolean) {
                builder.withClaim(key, (Boolean) value);
            } else {
                builder.withClaim(key, (String) value);
            }
        }

        return builder.withExpiresAt(expires)
                .sign(algorithm);
    }


    /**
     * 根据账户信息及默认jwt构造值，生成token组对象
     *
     * @param userId
     * @param name
     * @return
     */
    public static TokenVo getTokenVo(Long userId, String name) throws IllegalArgumentException {
        //有效载荷主体信息
        Map<String, Object> map = new HashMap<>();
        map.put("id", userId);
        map.put("name", name);

        //过期时间
        Date accessExpires = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        Date refreshExpires = new Date(System.currentTimeMillis() + EXPIRATION_TIME * 2);

        //使用密钥与加密算法得到的签名
        Algorithm algorithm = Algorithm.HMAC256(SECRET);

        //生成token
        String accessJwt = TokenUtil.createJWT(map, accessExpires, algorithm);
        String refreshJwt = TokenUtil.createJWT(map, refreshExpires, algorithm);

        //编码
        String accessToken = Base64Util.encoderWithBase64("Bearer " + accessJwt);
        String refreshToken = Base64Util.encoderWithBase64("Bearer " + refreshJwt);

        TokenVo tokenVo = new TokenVo();
        tokenVo.setAccessToken(accessToken);
        tokenVo.setRefreshToken(refreshToken);
        return tokenVo;
    }


    /**
     *
     *
     * @param token
     * @return
     */
    public static String getJwtWithoutBearer(String token) {
        String jwt;
        if (token.startsWith("Bearer "))
            jwt = token.substring(7);
        else
            jwt = token;
        return jwt;
    }


    /**
     * 解析JWT令牌得到有效载荷
     *
     * @param jwt
     * @return
     */
    public static Map<String, Claim> getPayLoadByAnalysisJWT(String jwt) throws IllegalArgumentException, TokenExpiredException, JWTDecodeException{
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);

            JWTVerifier verifier = JWT.require(algorithm).build();

            DecodedJWT verify = verifier.verify(jwt);

            return verify.getClaims();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (TokenExpiredException e) {
            throw new TokenExpiredException(e.getMessage());
        } catch (JWTDecodeException e) {
            throw new JWTDecodeException(e.getMessage());
        } catch (RuntimeException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
