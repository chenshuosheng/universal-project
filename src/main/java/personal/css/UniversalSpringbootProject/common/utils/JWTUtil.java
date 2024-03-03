package personal.css.UniversalSpringbootProject.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.Map;

import static personal.css.UniversalSpringbootProject.common.consts.MyConst.SECRET;

/**
 * @Description: JWT口令相关工具
 * @Author: CSS
 * @Date: 2024/3/3 0:22
 */
public class JWTUtil {


    /**
     * 创建JWT令牌
     *
     * @param claims        有效载荷主体信息
     * @param expires       过期时间
     * @param algorithm     使用密钥与加密算法得到的签名
     * @return
     */
    public static String createJWT(Map<String, String> claims, Date expires, Algorithm algorithm) {
        JWTCreator.Builder builder = JWT.create();
        for (Map.Entry<String, String> claim : claims.entrySet()) {
            builder.withClaim(claim.getKey(), claim.getValue());
        }
        return builder.withExpiresAt(expires)
                .sign(algorithm);
    }


    /**
     * 解析JWT令牌得到有效载荷
     *
     * @param jwt
     * @return
     */
    public static Map<String, Claim> getPayLoadByAnalysisJWT(String jwt) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);

        JWTVerifier verifier = JWT.require(algorithm).build();

        DecodedJWT verify = verifier.verify(jwt);

        return verify.getClaims();
    }
}
