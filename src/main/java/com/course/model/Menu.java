package com.course.model;

import lombok.Data;

@Data
public class Menu {
    private String id;
    private String title;
    private String description;
    private String role;
    private String path;
    private String icon;
}
