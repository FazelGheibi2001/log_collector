package com.example.log_collect;

import com.example.log_collect.dtos.LoginDTO;
import com.example.log_collect.models.enums.LogType;

import java.util.UUID;

public class CommonTestData {
    public static final String DEFAULT_STRING = "AAAAAAAAAAAAAAA";
    public static final String UPDATED_STRING = "BBBBBBBBBBBBBBB";

    public static final String DEFAULT_USERNAME = "09924664362";
    public static final String DEFAULT_PASSWORD = "123456789";

    public static final LogType DEFAULT_TYPE = LogType.INFO;
    public static final LogType UPDATED_TYPE = LogType.WARNING;


    public static LoginDTO loginDTO() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setPassword(DEFAULT_PASSWORD);
        loginDTO.setUsername(DEFAULT_USERNAME);
        return loginDTO;
    }
}
