package com.course.api;

import com.course.mapper.vo.SemesterVO;
import com.course.model.BasicResponse;
import com.course.model.CommonResponse;
import com.course.model.semesters.InsertSemestersRequest;
import com.course.model.semesters.Semester;
import com.course.model.semesters.SemestersResponse;
import com.course.model.semesters.UpdateSemestersRequest;
import com.course.security.authorize.AdminOnly;
import com.course.service.SemestersService;
import com.course.util.CommonUtil;
import com.course.util.ResponseUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/semesters")
public class SemestersController {

    @Autowired
    private SemestersService semestersService;

    // 取得學期資訊
    @GetMapping()
    @AdminOnly
    public BasicResponse<SemestersResponse> getSemesters() {
        return ResponseUtil.execute(
                () -> {
                    SemestersResponse res = new SemestersResponse();
                    List<Semester> semesters = new ArrayList<Semester>();
                    List<SemesterVO> list = semestersService.findAll();
                    for (SemesterVO vo : list) {
                        Semester semester = new Semester();
                        BeanUtils.copyProperties(semester, vo);
                        semesters.add(semester);
                    }
                    res.setSemesters(semesters);
                    return res;
                },
                "success",
                "查詢學期失敗"
        );
    }


    // 更新學期資訊
    @PutMapping()
    @AdminOnly
    public BasicResponse<CommonResponse> update(@RequestBody UpdateSemestersRequest query) {
        return ResponseUtil.execute(
                () -> {
                    SemesterVO vo = new SemesterVO();
                    BeanUtils.copyProperties(vo, query);
                    semestersService.update(vo);
                    return new CommonResponse();
                },
                "success",
                "更新學期失敗"
        );
    }

    //新增學期
    @PostMapping()
    @AdminOnly
    public BasicResponse<CommonResponse> insertCourses(
            @RequestBody InsertSemestersRequest query) {
        return ResponseUtil.execute(
                () -> {
                    SemesterVO vo = new SemesterVO();
                    BeanUtils.copyProperties(vo, query);
                    vo.setId(CommonUtil.getUUID());

                    semestersService.insert(vo);
                    return new CommonResponse();
                },
                "success",
                "新增學期失敗"
        );
    }

    // 刪除學期
    @DeleteMapping("/{id}")
    @AdminOnly
    public BasicResponse<CommonResponse> deleteCourses(@PathVariable String id) {
        return ResponseUtil.execute(
                () -> {
                    semestersService.delete(id);
                    return new CommonResponse();
                },
                "success",
                "刪除學期失敗"
        );
    }

}
