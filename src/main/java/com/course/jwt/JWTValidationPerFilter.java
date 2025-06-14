package com.course.jwt;

import com.course.service.LoginService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Date;

@Component
public class JWTValidationPerFilter extends OncePerRequestFilter {

    private String[] authUrls = {"/login"};


    @Autowired
    private LoginService longService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        Date now = new Date();
        String requestUrl = request.getRequestURL().toString();

        if (StringUtils.containsAny(requestUrl, authUrls)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String accessToken = request.getHeader("Authorization");
            if (accessToken == null) {
                sendResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }


            String[] splitToken = accessToken.split(" ");
            accessToken = splitToken[1];

            if (splitToken.length != 2 || isTokenExpired(accessToken)) {
                sendResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }

            if (StringUtils.indexOf(request.getContentType(), "json") < 0) {
                filterChain.doFilter(request, response);
            } else {
                String txKey = JwtTokenUtil.getTxKeyFromToken(accessToken);
                String userId = JwtTokenUtil.getUserIdFromToken(accessToken);
                String access = longService.getAccessTokenByUserId(userId);

                String[] accessSp = access.split("&");

                boolean check = false;
                for (String sp : accessSp) {
                    if (sp.equals(accessToken)) {
                        check = true;
                    }
                }

                if (check) {
                    JWTServletRequestWrapper jetRequest = new JWTServletRequestWrapper(request, txKey, userId, now);
                    filterChain.doFilter(request, response);
                } else {
                    sendResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                    return;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean isTokenExpired(String accessToken) {
        try {
            Date expiration = JwtTokenUtil.getExpirationDateFromToken(accessToken);
            return expiration.before(new Date());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    ;


    private void sendResponse(HttpServletResponse response, int responseCode, String msg) {
        try {
            response.setStatus(responseCode);
            response.sendError(responseCode, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
