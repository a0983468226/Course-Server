package com.course.api;

import com.course.mapper.vo.UserVO;
import com.course.model.BasicResponse;
import com.course.model.user.UpdateUserRequest;
import com.course.model.user.UpdateUserResponse;
import com.course.service.UserService;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PutMapping("/auth/user")
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
