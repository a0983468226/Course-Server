package com.course;

import com.course.redis.ResidKeyValueCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class LongService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    public String getAccessTokenByUserId(String userId) {
        return stringRedisTemplate.opsForValue().get(ResidKeyValueCode.USERID_2_ACCESS_TOKEN + userId);
    }

    public String setRefreshTokenByUserId(String userId) {
        return stringRedisTemplate.opsForValue().get(ResidKeyValueCode.USERID_2_ACCESS_TOKEN + userId);
    }

}
