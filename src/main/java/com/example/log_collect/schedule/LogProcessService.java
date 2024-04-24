package com.example.log_collect.schedule;

import com.example.log_collect.feign.LogProcessorFeign;
import com.example.log_collect.log.LogRepository;
import com.example.log_collect.models.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
public class LogProcessService {
    private final LogRepository repository;
    private final LogProcessorFeign feign;
    private final KafkaTemplate<String, List<Log>> kafkaTemplate;

    private static final Logger log = LoggerFactory.getLogger(LogProcessService.class);

    public LogProcessService(LogRepository repository, LogProcessorFeign feign, KafkaTemplate<String, List<Log>> kafkaTemplate) {
        this.repository = repository;
        this.feign = feign;

        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(cron = TimeFrame.M1)
    private void processLogs() {
        List<Log> unprocessLogs = repository.findByProcessed(false);
        sendLogs(unprocessLogs);
        updateLogs(unprocessLogs);
    }

    private void updateLogs(List<Log> unprocessLogs) {
        unprocessLogs
                .forEach(entity -> {
                    entity.setProcessed(true);
                    repository.save(entity);
                });
    }

    private void sendLogs(List<Log> unprocessLogs) {
        try {
            ResponseEntity<Void> response = feign.saveLogs(unprocessLogs);
            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("logs sent. : {}", LocalTime.now());

            } else if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                log.warn("logs can't sent. UNAUTHORIZED request : {}", LocalTime.now());
                throw new Exception("can't send logs");

            } else if (response.getStatusCode() == HttpStatus.FORBIDDEN) {
                log.warn("logs can't sent. FORBIDDEN request : {}", LocalTime.now());
                throw new Exception("can't send logs");

            }
        } catch (Exception ex) {
            log.error("error when sending logs to third party. try with kafka");
            kafkaTemplate.send("LOG_TOPIC", unprocessLogs);

        }
    }
}
