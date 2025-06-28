package com.course.service;

import com.course.mapper.UsersMapper;
import com.course.mapper.vo.UserVO;
import com.course.util.AESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

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

    public List<UserVO> getAllUsers() throws Exception {
        return usersMapper.getAllUsers();
    }

    @Transactional
    public synchronized void insert(UserVO vo) throws Exception {
        if (vo.getPasswordHash() != null) {
            vo.setPasswordHash(AESUtil.ECBencrypt(vo.getPasswordHash()));
        }
        int count = usersMapper.insert(vo);
        if (count != 1) {
            throw new IllegalStateException("新增資料不為1筆");
        }
    }

    @Transactional
    public synchronized void update(UserVO vo) throws Exception {
        vo.setUpdateAt(new Date());
        if (vo.getPasswordHash() != null) {
            vo.setPasswordHash(AESUtil.ECBencrypt(vo.getPasswordHash()));
        }
        int count = usersMapper.update(vo);
        if (count != 1) {
            throw new IllegalStateException("更新資料不為1筆");
        }
    }

    @Transactional
    public synchronized void delete(String id) throws Exception {

        int count = usersMapper.delete(id);
        if (count != 1) {
            throw new IllegalStateException("更新資料不為1筆");
        }
    }
}
