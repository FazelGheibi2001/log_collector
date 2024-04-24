package com.example.log_collect.dtos;

import com.example.log_collect.models.enums.LogType;
import com.example.log_collect.parent.BaseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class LogDTO extends BaseDTO {
    private @Size(max = 1000) String body;
    private LocalDateTime createdAt;
    private @Size(max = 50) String serviceName;
    private String requestId;
    private LogType type;
    private Boolean processed;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public LogType getType() {
        return type;
    }

    public void setType(LogType type) {
        this.type = type;
    }

    public Boolean getProcessed() {
        return processed;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }
}
