package com.course.service;

import com.course.mapper.UsersMapper;
import com.course.mapper.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UsersMapper usersMapper;

    public UserVO findByUsername(String username) throws Exception {
        return usersMapper.findByUsername(username);
    }

    public UserVO findById(String userid) throws Exception {
        return usersMapper.findById(userid);
    }
}
