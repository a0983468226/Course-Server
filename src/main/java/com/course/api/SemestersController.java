package com.course.api;

import com.course.mapper.vo.SemesterVO;
import com.course.model.BasicResponse;
import com.course.model.semesters.InsertSemestersRequest;
import com.course.model.semesters.SemestersResponse;
import com.course.model.semesters.UpdateSemestersRequest;
import com.course.security.authorize.AdminOnly;
import com.course.service.SemestersService;
import com.course.util.CommonUtil;
import com.course.util.ResponseUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/semesters")
public class SemestersController {

    @Autowired
    private SemestersService semestersService;

    @PutMapping()
    @AdminOnly
    public BasicResponse<SemestersResponse> update(@RequestBody UpdateSemestersRequest query) {
        return ResponseUtil.execute(
                () -> {
                    SemesterVO vo = new SemesterVO();
                    BeanUtils.copyProperties(query, vo);
                    semestersService.update(vo);

                    return new SemestersResponse();
                },
                "success",
                "更新學期失敗"
        );
    }

    @PostMapping()
    @AdminOnly
    public BasicResponse<SemestersResponse> insertCourses(
            @RequestBody InsertSemestersRequest query) {
        return ResponseUtil.execute(
                () -> {
                    SemesterVO vo = new SemesterVO();
                    BeanUtils.copyProperties(query, vo);
                    vo.setId(CommonUtil.getUUID());

                    semestersService.insert(vo);
                    return new SemestersResponse();
                },
                "success",
                "新增學期失敗"
        );
    }

    @DeleteMapping("/{id}")
    @AdminOnly
    public BasicResponse<SemestersResponse> deleteCourses(@PathVariable String id) {
        return ResponseUtil.execute(
                () -> {
                    semestersService.delete(id);
                    return new SemestersResponse();
                },
                "success",
                "刪除學期失敗"
        );
    }

}
