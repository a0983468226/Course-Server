package com.course.security.jwt;

import com.course.service.LoginService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JWTValidationPerFilter extends OncePerRequestFilter {

    private final LoginService loginService;

    private final String[] authUrls = {"/login", "/refresh-token", "/captcha"};

    public JWTValidationPerFilter(LoginService loginService) {
        this.loginService = loginService;
    }

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

            String txKey = JwtTokenUtil.getTxKeyFromToken(accessToken);
            String userId = JwtTokenUtil.getUserIdFromToken(accessToken);
            String role = JwtTokenUtil.getRoleFromToken(accessToken);

            if (StringUtils.isEmpty(role)) {
                sendResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }
            List<String> roles = Arrays.asList(role.split(","));

            JwtUserDetails userDetails = new JwtUserDetails(userId, roles);

            String access = loginService.getAccessTokenByUserId(userId);
            String[] accessSp = access.split("&");

            boolean check = false;
            for (String sp : accessSp) {
                if (sp.equals(accessToken)) {
                    check = true;
                    break;
                }
            }

            if (check) {
                List<GrantedAuthority> authorities = roles.stream()
                        .map(String::trim)
                        .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                        .collect(Collectors.toList());

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authToken);
                JWTServletRequestWrapper wrappedRequest = new JWTServletRequestWrapper(request, txKey, userId, new Date());
                filterChain.doFilter(wrappedRequest, response);
            } else {
                sendResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
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
           return true;
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
