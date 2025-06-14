package com.course.service;

import com.course.jwt.JwtTokenUtil;
import com.course.mapper.vo.MenuVO;
import com.course.mapper.vo.UserVO;
import com.course.redis.RedisKeyValueCode;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class LoginService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private MenuService menuService;

    public String getCaptchaByCaptchaId(String captchaId) {
        return stringRedisTemplate.opsForValue().get(RedisKeyValueCode.CAPTCHAID_2_CAPTCHA + captchaId);
    }

    public void clearCaptchaByCaptchaId(String captchaId) {
        if (stringRedisTemplate.hasKey(RedisKeyValueCode.CAPTCHAID_2_CAPTCHA + captchaId)) {
            stringRedisTemplate.delete(RedisKeyValueCode.CAPTCHAID_2_CAPTCHA + captchaId);
        }
    }

    public String getAccessTokenByUserId(String userId) {
        return stringRedisTemplate.opsForValue().get(RedisKeyValueCode.USERID_2_ACCESS_TOKEN + userId);
    }

    public String setRefreshTokenByUserId(String userId) {
        return stringRedisTemplate.opsForValue().get(RedisKeyValueCode.USERID_2_ACCESS_TOKEN + userId);
    }

    public String getTxKeyByRefreshToken(String refreshToken) {
        return stringRedisTemplate.opsForValue().get(RedisKeyValueCode.REFRESH_TOKEN_2_TXKEY + refreshToken);
    }

    public String getUserIdByRefreshToken(String refreshToken) {
        return stringRedisTemplate.opsForValue().get(RedisKeyValueCode.REFRESH_TOKEN_2_USERID + refreshToken);
    }

    @Cacheable(value = "access_token_session_time")
    public Integer getAccessTokenSessionTime() {
        return JwtTokenUtil.ACCESS_TOKEN_TIME;
    }

    @Cacheable(value = "refresh_token_session_time")
    public Integer geRefreshTokenSessionTime() {
        return JwtTokenUtil.REFRESH_TOKEN_TIMEE;
    }

    public void clearAccessTokenByUserId(String userId) {
        try {
            if (stringRedisTemplate.hasKey(RedisKeyValueCode.USERID_2_ACCESS_TOKEN + userId)) {
                stringRedisTemplate.delete(RedisKeyValueCode.USERID_2_ACCESS_TOKEN + userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAccessTokenByUserId(String accessToken, String userId, Integer limitTime) {
        String at = this.getAccessTokenByUserId(userId);
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(at)) {
            sb.append(at);
            sb.append("&");
        }
        sb.append(accessToken);
        stringRedisTemplate.opsForValue().set(RedisKeyValueCode.USERID_2_ACCESS_TOKEN + userId,
                sb.toString(), limitTime, TimeUnit.MINUTES);
    }

    public void setUserIdAndTxKeyByRefreshToken(String refreshToken, String userId, String txKey, Integer limitTime) {
        stringRedisTemplate.opsForValue().set(RedisKeyValueCode.REFRESH_TOKEN_2_USERID + refreshToken,
                userId, limitTime, TimeUnit.MINUTES);
        stringRedisTemplate.opsForValue().set(RedisKeyValueCode.REFRESH_TOKEN_2_TXKEY + refreshToken,
                txKey, limitTime, TimeUnit.MINUTES);
    }

    public void setRefreshTokenByUserId(String refreshToken, String userId, Integer limitTime) {
        stringRedisTemplate.opsForValue().set(RedisKeyValueCode.USERID_2_REFRESH_TOKEN + userId, refreshToken, limitTime, TimeUnit.MINUTES);
    }

    public Integer getRedisRefreshTokenTime() {
        return JwtTokenUtil.REDIS_REFRESH_TOKEN_TIME;
    }

    public void setRefreshTokenToCookie(HttpServletResponse response, String refreshToken, int refreshTokenExpireTime) {
        Cookie cookie = new Cookie(JwtTokenUtil.REFRESH_TOKEN_MANE, refreshToken);
        cookie.setMaxAge(refreshTokenExpireTime * 60);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        response.setHeader("X-Frame-Options", "SAMEORIGIN");

        boolean firstHeader = true;
        for (String header : response.getHeaders("Set-Cookie")) {
            if (firstHeader) {
                response.setHeader("Set-Cookie", String.format("%s;%s", header, "httpOnly;SameSite=Strict"));
                firstHeader = false;
                continue;
            }
            response.addHeader("Set-Cookie", String.format("%s;%s", header, "httpOnly;SameSite=Strict"));
        }
    }


    public UserVO findByUsername(String username) throws Exception {
        return userService.findByUsername(username);
    }

    public UserVO findUserByUserId(String userid) throws Exception {
        return userService.findById(userid);
    }

    public List<MenuVO> findMenusByRole(String role) throws Exception {
        return menuService.findByRole(role);
    }

}
