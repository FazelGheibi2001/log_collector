package com.example.log_collect.log;

import com.example.log_collect.dtos.LogDTO;
import com.example.log_collect.filters.LogFilter;
import com.example.log_collect.filters.query.LogQuery;
import com.example.log_collect.models.Log;
import com.example.log_collect.parent.ParentService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class LogService extends ParentService<Log, LogDTO, LogRepository, LogFilter> {
    private final LogQuery query;

    protected LogService(LogRepository repository, LogQuery query) {
        super(repository);
        this.query = query;
    }

    @Override
    public Log toEntity(LogDTO dto) {
        Log entity = new Log();
        entity.setBody(dto.getBody());
        entity.setServiceName(dto.getServiceName());
        entity.setType(dto.getType());
        entity.setRequestId(dto.getRequestId());
        return entity;
    }

    @Override
    public LogDTO toDto(Log entity) {
        LogDTO dto = new LogDTO();
        dto.setId(entity.getId());
        dto.setBody(entity.getBody());
        dto.setServiceName(entity.getServiceName());
        dto.setType(entity.getType());
        dto.setRequestId(entity.getRequestId());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setProcessed(entity.getProcessed());
        return dto;
    }

    @Override
    public Log prepareUpdate(Log entity, LogDTO dto) {
        entity.setBody(dto.getBody() != null ? dto.getBody() : entity.getBody());
        entity.setServiceName(dto.getServiceName() != null ? dto.getServiceName() : entity.getServiceName());
        entity.setType(dto.getType() != null ? dto.getType() : entity.getType());
        return entity;
    }

    @Override
    public Specification<Log> search(LogFilter filter) {
        return Specification
                .where(query.findByBody(filter.getBody()))
                .and(query.findByStartTime(filter.getStartTime()))
                .and(query.findByEndTime(filter.getEndTime()))
                .and(query.findByServiceName(filter.getServiceName()))
                .and(query.findByRequestId(filter.getRequestId()))
                .and(query.findByType(filter.getType()))
                .and(query.findByProcessed(filter.getProcessed()));
    }
}
