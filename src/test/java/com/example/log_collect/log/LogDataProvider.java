package com.example.log_collect.log;

import com.example.log_collect.dtos.LogDTO;
import org.springframework.stereotype.Component;

import static com.example.log_collect.CommonTestData.*;

@Component
public class LogDataProvider {

    public LogDTO createEntity() {
        LogDTO dto = new LogDTO();
        dto.setBody(DEFAULT_STRING);
        dto.setRequestId(DEFAULT_STRING);
        dto.setServiceName(DEFAULT_STRING);
        dto.setType(DEFAULT_TYPE);
        return dto;
    }

    public LogDTO updateEntity(LogDTO entity) {
        entity.setBody(UPDATED_STRING);
        entity.setServiceName(UPDATED_STRING);
        entity.setType(UPDATED_TYPE);
        return entity;
    }
}
