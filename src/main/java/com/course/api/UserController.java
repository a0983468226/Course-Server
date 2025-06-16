package com.course.api;

import com.course.mapper.vo.UserVO;
import com.course.model.BasicResponse;
import com.course.model.user.FinduserByIdResponse;
import com.course.model.user.UpdateUserRequest;
import com.course.model.user.UpdateUserResponse;
import com.course.security.authorize.AdminOnly;
import com.course.service.UserService;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public BasicResponse<FinduserByIdResponse> finduserById(@PathVariable String id) {
        BasicResponse<FinduserByIdResponse> response = new BasicResponse<FinduserByIdResponse>();
        FinduserByIdResponse data = new FinduserByIdResponse();
        try {
            UserVO vo = userService.findById(id);
            if (vo != null) {
                BeanUtils.copyProperties(vo, data);
            }
            response.setData(data);
            response.setMessage("success");
            response.setSuccess(true);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage("使用者查詢失敗");
            response.setSuccess(false);
            return response;
        }
    }

    @PutMapping()
    @AdminOnly
    public BasicResponse<UpdateUserResponse> updateUser(@Validated @RequestBody UpdateUserRequest request) {
        BasicResponse<UpdateUserResponse> response = new BasicResponse<UpdateUserResponse>();
        UpdateUserResponse data = new UpdateUserResponse();
        try {
            UserVO vo = new UserVO();
            BeanUtils.copyProperties(vo, request);
            vo.setPasswordHash(request.getPassword());
            userService.update(vo);

        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage("更新使用者失敗");
            response.setSuccess(false);
            return response;
        }

        response.setData(data);
        response.setMessage("success");
        response.setSuccess(true);
        return response;
    }

}
