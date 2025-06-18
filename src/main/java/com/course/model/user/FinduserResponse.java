package com.course.model.user;

import com.course.model.AbstractResponseData;
import lombok.Data;

import java.util.List;

@Data
public class FinduserResponse extends AbstractResponseData {

    private List<User> users;
}
