package com.course.jwt;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

public class JwtTokenUtil {

    public static final int REDIS_REFRESH_TOKEN_TIME = 70;

    public static final int REFRESH_TOKEN_TIMEE = 60;

    public static final int ACCESS_TOKEN_TIME = 70;


    public static final String REFRESH_TOKEN_MANE = "COURSE_REFRESH_TOKEN";


    public static String getUsernameFromToken(String token) throws Exception {
        return getClaimFromToken(token, Claims::getSubject);
    }

    ;

    public static String getUserIdFromToken(String token) throws Exception {
        final Claims clams = getAllClaimsFromToken(token);
        return clams.get("userId", String.class);
    }

    ;

    public static String getTxKeyFromToken(String token) throws Exception {
        final Claims clams = getAllClaimsFromToken(token);
        return clams.get("txKey", String.class);
    }

    ;


    public static Date getExpirationDateFromToken(String token) throws Exception {
        return getClaimFromToken(token, Claims::getExpiration);


    }

    public static <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) throws Exception {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private static Claims getAllClaimsFromToken(String token) throws Exception {
        return Jwts.parser().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();

    }

    public static String generateToken(String userId, String userName, Integer limitTime) throws Exception {
        String txKey = DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS") + "-" + RandomStringUtils.random(8, userId);
        return generateTokenWithTxKey(userId, userName, txKey, limitTime);

    }

    public static String generateTokenWithTxKey(String userId, String userName, String txKey, Integer limitTime) throws Exception {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("txKey", txKey);
        return doGenerateToken(claims, userName, limitTime);

    }

    public static String generateRefreshToken(String userId)throws Exception {
        return UUID.randomUUID().toString().replace("-","")+ RandomStringUtils.random(8, userId);
    }

    private static String doGenerateToken(Map<String, Object> claims, String subject, Integer limitTime) throws Exception {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(
                new Date(System.currentTimeMillis() + limitTime * 60 * 1000)).signWith(SignatureAlgorithm.HS512, getKey()).compact();
    }


    private static String getKey() throws Exception {
        String key = "COURSE_KEY";
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.reset();
        md.update(key.getBytes("utf8"));
        return String.format("%0128x", new BigInteger(1, md.digest()));
    }
}
