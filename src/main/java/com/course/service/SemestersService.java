package com.course.service;

import com.course.mapper.SemestersMapper;
import com.course.mapper.vo.SemesterVO;
import com.course.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SemestersService {

    @Autowired
    private SemestersMapper emestersMapper;


    @Transactional
    public synchronized void insert(SemesterVO vo) throws Exception {
        vo.setId(CommonUtil.getUUID());
        int count = emestersMapper.insert(vo);
        if (count != 1) {
            throw new IllegalStateException("新增資料不為1筆");
        }
    }


    @Transactional
    public synchronized void update(SemesterVO semester) throws Exception {
        int count = emestersMapper.update(semester);
        if (count != 1) {
            throw new IllegalStateException("更新資料不為1筆");
        }
    }

    @Transactional
    public synchronized void delete(String id) throws Exception {
        int count = emestersMapper.delete(id);
        if (count != 1) {
            throw new IllegalStateException("刪除資料不為1筆");
        }
    }
}
