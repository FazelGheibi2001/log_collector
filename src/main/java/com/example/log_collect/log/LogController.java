package com.example.log_collect.log;

import com.example.log_collect.dtos.LogDTO;
import com.example.log_collect.filters.LogFilter;
import com.example.log_collect.permission.ControllerPermissions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/log")
public class LogController {
    private final LogService service;

    public LogController(LogService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize(ControllerPermissions.LOG_ACCESS)
    public ResponseEntity<LogDTO> saveLog(@RequestBody @Valid LogDTO dto) {
        return new ResponseEntity<>(service.save(dto), HttpStatus.CREATED);
    }

    @GetMapping("/find-all")
    @PreAuthorize(ControllerPermissions.LOG_ACCESS)
    public ResponseEntity<List<LogDTO>> findAllLogs() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize(ControllerPermissions.LOG_ACCESS)
    public ResponseEntity<Page<LogDTO>> findAllLogPageable(Pageable pageable) {
        return new ResponseEntity<>(service.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize(ControllerPermissions.LOG_ACCESS)
    public ResponseEntity<LogDTO> findLogById(@PathVariable String id) {
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @PutMapping
    @PreAuthorize(ControllerPermissions.LOG_ACCESS)
    public ResponseEntity<LogDTO> updateLog(@RequestBody @Valid LogDTO dto) {
        return new ResponseEntity<>(service.update(dto), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(ControllerPermissions.LOG_ACCESS)
    public ResponseEntity<Void> deleteLogById(@PathVariable String id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/filter")
    @ResponseBody
    @PreAuthorize(ControllerPermissions.LOG_ACCESS)
    public ResponseEntity<Page<LogDTO>> getLogByFilter(LogFilter filter, Pageable pageable) {
        return new ResponseEntity<>(service.findAllByFilter(filter, pageable), HttpStatus.OK);
    }
}
