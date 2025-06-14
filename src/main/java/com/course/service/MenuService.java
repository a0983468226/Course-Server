package com.course.service;

import com.course.mapper.MenuMapper;
import com.course.mapper.vo.MenuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {

    @Autowired
    private MenuMapper menuMapper;

    public List<MenuVO> findByRole(String role) throws Exception {
        return menuMapper.findByRole(role);
    }
}
