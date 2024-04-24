package com.example.log_collect.filters.query;

import com.example.log_collect.models.Log;
import com.example.log_collect.models.enums.LogType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class LogQuery {

    public Specification<Log> findByBody(String body) {
        if (body == null || body.isEmpty()) return null;
        return (root, cq, cb) -> cb.like(root.get("body"), "%" + body + "%");
    }

    public Specification<Log> findByStartTime(LocalDateTime startTime) {
        if (startTime == null) return null;
        return (root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("createdAt"), startTime);
    }

    public Specification<Log> findByEndTime(LocalDateTime endTime) {
        if (endTime == null) return null;
        return (root, cq, cb) -> cb.lessThanOrEqualTo(root.get("createdAt"), endTime);
    }

    public Specification<Log> findByServiceName(String serviceName) {
        if (serviceName == null || serviceName.isEmpty()) return null;
        return (root, cq, cb) -> cb.like(root.get("serviceName"), "%" + serviceName + "%");
    }

    public Specification<Log> findByRequestId(String requestId) {
        if (requestId == null || requestId.isEmpty()) return null;
        return (root, cq, cb) -> cb.like(root.get("requestId"), "%" + requestId + "%");
    }

    public Specification<Log> findByType(LogType type) {
        if (type == null) return null;
        return (root, cq, cb) -> cb.equal(root.get("type"), type);
    }

    public Specification<Log> findByProcessed(Boolean processed) {
        if (processed == null) return null;
        return (root, cq, cb) -> cb.equal(root.get("processed"), processed);
    }
}
