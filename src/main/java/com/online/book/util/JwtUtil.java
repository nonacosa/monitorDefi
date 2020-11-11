package com.online.book.util;

import com.google.api.client.util.Lists;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author wenda.zhuang
 * @Date 2020/8/19 7:37 下午
 * @Description ...
 * @E-mail sis.nonacosa@gmail.com
 */
public class JwtUtil {

	// Token过期时间30分钟
	public static final long EXPIRE_TIME = 30 * 60 * 1000;

	/* *
	 * <p> 校验token是否正确 </p>
	 * @Param token
	 * @Param username
	 * @Param secret
	 * @Return boolean
	 */
	public static boolean verify(String token, String username, String secret) {
		try {
			// 设置加密算法
			Algorithm algorithm = Algorithm.HMAC256(secret);
			JWTVerifier verifier = JWT.require(algorithm)
					.withClaim("username", username)
					.build();
			// 效验TOKEN
			DecodedJWT jwt = verifier.verify(token);
			return true;
		} catch (Exception exception) {
			return false;
		}
	}



	/* *
	 * <p>生成签名,30min后过期 </p>
	 * @Param [username, secret]
	 * @Return java.lang.String
	 */
	public static String sign(String username, String secret) {
		Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
		Algorithm algorithm = Algorithm.HMAC256(secret);
		// 附带username信息
		return JWT.create()
				.withClaim("username", username)
				.withExpiresAt(date)
				.sign(algorithm);

	}

	/* *
	 * <p> 获得用户名 </p>
	 * @Param [request]
	 * @Return java.lang.String
	 */
	public static String getUserNameByToken(HttpServletRequest request)  {
		String token = getToken(request);
		if(token == null) return null;
		DecodedJWT jwt = JWT.decode(token);
		return jwt.getClaim("username")
				.asString();
	}


	public static String getToken(HttpServletRequest request)  {
		Cookie[] cookies = request.getCookies();
		if(cookies == null) return null;
		List<Cookie> cookieList = Lists.newArrayList(Arrays.asList(cookies));
		for (Cookie cookie : cookieList) {
			if("token".equals(cookie.getName())) {
				return cookie.getValue();
			}
		}
		return null;
	}


}
