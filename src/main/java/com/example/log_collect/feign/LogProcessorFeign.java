package com.example.log_collect.feign;

import com.example.log_collect.models.Log;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "log-processor", url = "${client.log-processor.baseUrl}")
public interface LogProcessorFeign {
    @PostMapping("/api/v1/logs/save")
    ResponseEntity<Void> saveLogs(List<Log> entities);
}
