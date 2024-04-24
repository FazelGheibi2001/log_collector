package com.example.log_collect.log;

import com.example.log_collect.models.Log;
import com.example.log_collect.parent.ParentRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogRepository extends ParentRepository<Log> {
    List<Log> findByProcessed(Boolean processed);

}
