package com.course.api;

import com.course.mapper.vo.UserVO;
import com.course.model.BasicResponse;
import com.course.model.user.*;
import com.course.security.authorize.AdminOnly;
import com.course.security.jwt.JwtUserDetails;
import com.course.service.UserService;
import com.course.util.CommonUtil;
import com.course.util.ResponseUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 查看個人資料
    @GetMapping("my")
    public BasicResponse<FinduserByIdResponse> finduserById(@AuthenticationPrincipal JwtUserDetails user) {
        return ResponseUtil.execute(
                () -> {
                    FinduserByIdResponse data = new FinduserByIdResponse();
                    UserVO vo = userService.findById(user.getUserId());
                    if (vo != null) {
                        BeanUtils.copyProperties(data, vo);
                    }
                    return data;
                },
                "success",
                "使用者查詢失敗"
        );
    }

    // 更新個人資料
    @PutMapping("/my")
    public BasicResponse<UpdateUserResponse> updateUserById(@AuthenticationPrincipal JwtUserDetails user,
                                                            @RequestBody UpdateUserRequest request) {
        return ResponseUtil.execute(
                () -> {
                    UserVO vo = new UserVO();
                    vo.setName(request.getName());
                    vo.setEmail(request.getEmail());
                    vo.setId(user.getUserId());
                    vo.setPasswordHash(request.getPassword());
                    userService.update(vo);
                    return new UpdateUserResponse();
                },
                "success",
                "更新使用者失敗"
        );
    }

    // 取得所有使用者資料
    @GetMapping()
    @AdminOnly
    public BasicResponse<FinduserResponse> findUser() {
        return ResponseUtil.execute(
                () -> {
                    List<UserVO> vos = userService.getAllUsers();
                    List<User> users = new ArrayList<>();
                    for (UserVO vo : vos) {
                        User user = new User();
                        BeanUtils.copyProperties(user, vo);
                        users.add(user);
                    }
                    FinduserResponse data = new FinduserResponse();
                    data.setUsers(users);
                    return data;
                },
                "success",
                "使用者查詢失敗"
        );
    }

    // 建立帳號
    @PostMapping()
    @AdminOnly
    public BasicResponse<UpdateUserResponse> insert(@Validated @RequestBody InsertUserRequest request) {
        return ResponseUtil.execute(
                () -> {
                    UserVO vo = new UserVO();
                    BeanUtils.copyProperties(vo, request);
                    vo.setPasswordHash("123456");
                    vo.setId(CommonUtil.getUUID());
                    vo.setCreatedAt(new Date());
                    vo.setStatus(1);
                    vo.setIsFirstLogin("N");
                    vo.setUpdateAt(new Date());
                    userService.insert(vo);
                    return new UpdateUserResponse();
                },
                "success",
                "更新使用者失敗"
        );
    }

    // 更新指定使用者資料
    @PutMapping()
    @AdminOnly
    public BasicResponse<UpdateUserResponse> updateUser( @RequestBody UpdateUserRequest request) {
        return ResponseUtil.execute(
                () -> {
                    UserVO vo = new UserVO();
                    BeanUtils.copyProperties(vo, request);
                    vo.setPasswordHash(request.getPassword());
                    userService.update(vo);
                    return new UpdateUserResponse();
                },
                "success",
                "更新使用者失敗"
        );
    }

    //刪除使用者
    @DeleteMapping("/{id}")
    @AdminOnly
    public BasicResponse<UpdateUserResponse> deleteUser(@PathVariable String id) {
        return ResponseUtil.execute(
                () -> {
                    userService.delete(id);
                    return new UpdateUserResponse();
                },
                "success",
                "更新使用者失敗"
        );
    }

}
