package com.course.api;

import com.course.security.jwt.JwtTokenUtil;
import com.course.mapper.vo.MenuVO;
import com.course.mapper.vo.UserVO;
import com.course.model.*;
import com.course.model.auth.*;
import com.course.service.LoginService;
import com.course.service.UserService;
import com.course.util.AESUtil;
import com.course.util.CommonUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public BasicResponse<LoginResponse> login(@RequestBody LoginRequest loginRequest,
                                              HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        BasicResponse<LoginResponse> response = new BasicResponse<>();
        LoginResponse data = new LoginResponse();

        try {
            if (StringUtils.isEmpty(loginRequest.getUsername()) || StringUtils.isEmpty(loginRequest.getPassword())) {
                response.setSuccess(false);
                response.setMessage("登入失敗 : 請輸入使用者帳號密碼");
                return response;
            }
            if (StringUtils.isEmpty(loginRequest.getCaptchaCode()) || StringUtils.isEmpty(loginRequest.getCaptchaId())) {
                response.setSuccess(false);
                response.setMessage("登入失敗 : 請輸入驗證碼");
                return response;
            }

            String captcha = null;
            if (StringUtils.isNotBlank(loginRequest.getCaptchaId())) {
                captcha = loginService.getCaptchaByCaptchaId(loginRequest.getCaptchaId());
                loginService.clearCaptchaByCaptchaId(loginRequest.getCaptchaId());
            }

            if (!StringUtils.equalsIgnoreCase(captcha, loginRequest.getCaptchaCode())) {
                response.setSuccess(false);
                response.setMessage("登入失敗 : 驗證碼錯誤");
                return response;
            }


            UserVO user = loginService.findByUsername(loginRequest.getUsername());
            if (user == null) {
                response.setSuccess(false);
                response.setMessage("登入失敗 : 查無使用者");
                return response;
            }
            if (!loginRequest.getPassword().equals(AESUtil.ECBdecode(user.getPasswordHash()))) {
                response.setSuccess(false);
                response.setMessage("登入失敗 : 密碼錯誤");
                return response;
            }

            List<MenuVO> menus = loginService.findMenusByRole(user.getRole());
            List<Menu> menuList = new ArrayList<Menu>();
            for (MenuVO vo : menus) {
                Menu m = new Menu();
                BeanUtils.copyProperties(m, vo);
                menuList.add(m);
            }

            data.setUserId(user.getId());
            data.setName(user.getName());
            data.setEmail(user.getEmail());
            data.setRole(user.getRole());
            data.setIsFirstLogin(user.getIsFirstLogin());
            data.setMenus(menuList);

            Integer accessTokenTime = loginService.getAccessTokenSessionTime();
            Integer refreshTokenTime = loginService.geRefreshTokenSessionTime();

            String accessToken = JwtTokenUtil.generateToken(user.getId(), user.getUsername(), accessTokenTime, user.getRole());
            data.setAccessToken(accessToken);
            data.setAccessTokenExp(JwtTokenUtil.getExpirationDateFromToken(accessToken));

            loginService.clearAccessTokenByUserId(user.getId());
            loginService.setAccessTokenByUserId(accessToken, user.getId(), loginService.getAccessTokenSessionTime());

            String txKey = JwtTokenUtil.getTxKeyFromToken(accessToken);
            String refreshToken = JwtTokenUtil.generateRefreshToken(user.getId());

            data.setRefreshTokenExp(new Date(System.currentTimeMillis() + refreshTokenTime * 60 * 1000));
            loginService.setUserIdAndTxKeyByRefreshToken(refreshToken, user.getId(), txKey, refreshTokenTime);


            loginService.setRefreshTokenToCookie(httpResponse, refreshToken, refreshTokenTime);
            loginService.setRefreshTokenByUserId(refreshToken, user.getId(), loginService.getRedisRefreshTokenTime());

            UserVO vo = new UserVO();
            vo.setId(user.getId());
            vo.setLastLoginTime(new Date());
            vo.setStatus(1);
            userService.update(vo);

            response.setData(data);
            response.setSuccess(true);
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            response.setMessage("登入失敗 : 使用者資料更新錯誤");
            return response;
        }
    }


    @PostMapping("/refresh-token")
    public BasicResponse<RefreshTokenResponseData> refreshToken(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        BasicResponse<RefreshTokenResponseData> response = new BasicResponse<>();
        RefreshTokenResponseData data = new RefreshTokenResponseData();
        Cookie[] cookies = httpRequest.getCookies();
        if (cookies == null) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication Error!");
            response.setMessage("Missing Authentication cookie!");
            return response;
        }

        String refreshToken = null;
        for (Cookie cookie : cookies) {
            if (StringUtils.equals(JwtTokenUtil.REFRESH_TOKEN_MANE, cookie.getName())) {
                refreshToken = cookie.getValue();
            }
        }

        if (StringUtils.isBlank(refreshToken)) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication Error!");
            response.setMessage("Missing Authentication!");
            return response;
        }

        String txKey = loginService.getTxKeyByRefreshToken(refreshToken);

        if (StringUtils.isBlank(txKey)) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication Error!");
            response.setMessage("Refresh Token Illegal!");
            return response;
        }

        String userId = loginService.getUserIdByRefreshToken(refreshToken);

        if (StringUtils.isBlank(userId)) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication Error!");
            response.setMessage("Refresh Token Fail , user is null!");
            return response;
        }

        UserVO userVo = loginService.findUserByUserId(userId);
        if (userVo == null) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication Error!");
            response.setMessage("Refresh Token Fail , user is null!");
            return response;
        }

        String id = userVo.getId();
        String uid = userVo.getUsername();

        Integer accessTokenTime = loginService.getAccessTokenSessionTime();
        Integer refreshTokenTime = loginService.geRefreshTokenSessionTime();
        String accessToken = JwtTokenUtil.generateToken(id, uid, accessTokenTime, userVo.getRole());
        data.setAccessToken(accessToken);
        data.setAccessTokenExp(JwtTokenUtil.getExpirationDateFromToken(accessToken));

        loginService.clearAccessTokenByUserId(id);
        loginService.setAccessTokenByUserId(accessToken, id, loginService.getAccessTokenSessionTime());

        refreshToken = JwtTokenUtil.generateRefreshToken(id);

        data.setRefreshTokenExp(new Date(System.currentTimeMillis() + refreshTokenTime * 60 * 1000));

        loginService.setUserIdAndTxKeyByRefreshToken(refreshToken, id, txKey, refreshTokenTime);


        loginService.setRefreshTokenToCookie(httpResponse, refreshToken, refreshTokenTime);
        loginService.setRefreshTokenByUserId(refreshToken, id, loginService.getRedisRefreshTokenTime());

        response.setData(data);
        response.setSuccess(true);
        return response;

    }

    @PostMapping("/captcha")
    public BasicResponse<CaptchaResponse> captcha() throws Exception {
        BasicResponse<CaptchaResponse> response = new BasicResponse<>();
        CaptchaResponse responseData = new CaptchaResponse();

        String uuid = CommonUtil.getUUID();
        String captcha = RandomStringUtils.random(4, false, true);
        loginService.setCaptchaByCaptchaId(uuid, captcha);
        responseData.setImage(CommonUtil.getCaptchaBase64String(captcha));
        responseData.setId(uuid);
        response.setData(responseData);
        response.setSuccess(true);
        return response;
    }


    @PostMapping("/logout")
    public BasicResponse<LogoutResponse> logout(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws Exception {
        BasicResponse<LogoutResponse> response = new BasicResponse<>();
        LogoutResponse responseData = new LogoutResponse();
        Cookie[] cookies = httpRequest.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (StringUtils.equals(JwtTokenUtil.REFRESH_TOKEN_MANE, cookie.getName())) {
                    cookie.setValue("");
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    cookie.setSecure(true);
                    httpResponse.addCookie(cookie);
                }
            }
        }

        Cookie cookie = new Cookie(JwtTokenUtil.REFRESH_TOKEN_MANE, "logout");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setSecure(true);
        httpResponse.addCookie(cookie);

        response.setData(responseData);
        response.setSuccess(true);
        return response;
    }

}
