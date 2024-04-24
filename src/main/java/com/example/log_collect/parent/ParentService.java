package com.example.log_collect.parent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public abstract class ParentService<ENTITY, DTO extends BaseDTO, REPOSITORY extends ParentRepository<ENTITY>, FILTER> {
    private final REPOSITORY repository;

    protected ParentService(REPOSITORY repository) {
        this.repository = repository;
    }

    public abstract ENTITY toEntity(DTO dto);

    public abstract DTO toDto(ENTITY entity);

    public void preSave(DTO dto) {
    }

    public DTO postSave(DTO dto, ENTITY entity, DTO savedDto) {
        return savedDto;
    }

    @Transactional
    public DTO save(DTO dto) {
        this.preSave(dto);
        ENTITY entity = this.toEntity(dto);
        entity = this.repository.save(entity);
        DTO savedDto = this.toDto(entity);
        savedDto = this.postSave(dto, entity, savedDto);
        return savedDto;
    }

    public void preUpdate(ENTITY oldEntity, DTO dto) {
    }

    public void postUpdate(ENTITY entity) {
    }

    @Transactional
    public DTO update(DTO dto) {
        if (this.repository.existsById(dto.getId())) {
            ENTITY oldEntity = this.repository.findById(dto.getId()).get();
            this.preUpdate(oldEntity, dto);
            ENTITY entity = this.prepareUpdate(oldEntity, dto);
            entity = this.repository.save(entity);
            DTO savedDto = this.toDto(entity);
            this.postUpdate(entity);
            return savedDto;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Model with id %s not found", dto.getId()));
        }
    }

    public abstract ENTITY prepareUpdate(ENTITY entity, DTO dto);

    @Transactional(
            readOnly = true
    )
    public DTO findById(String id) {
        Optional<ENTITY> byId = this.repository.findById(id);
        if (byId.isPresent()) {
            ENTITY entity = byId.get();
            entity = this.postFetch(entity);
            return this.toDto(entity);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Model with id %s not found", id));
        }
    }

    public ENTITY postFetch(ENTITY entity) {
        return entity;
    }

    @Transactional(
            readOnly = true
    )
    public Optional<DTO> findByIdOptional(String id) {
        Optional<ENTITY> byId = this.repository.findById(id);
        return byId.isPresent() ? byId.map(this::toDto) : Optional.empty();
    }

    @Transactional(
            readOnly = true
    )
    public List<DTO> findAll() {
        List<ENTITY> entities = this.repository.findAll();
        entities = this.postFetch(entities);
        List<DTO> dtoList = new ArrayList();
        Iterator var3 = entities.iterator();

        while(var3.hasNext()) {
            ENTITY entity = (ENTITY) var3.next();
            dtoList.add(this.toDto(entity));
        }

        return dtoList;
    }

    public List<ENTITY> postFetch(List<ENTITY> entities) {
        return entities;
    }

    @Transactional(
            readOnly = true
    )
    public Page<DTO> findAll(Pageable pageable) {
        Page<ENTITY> entities = this.repository.findAll(pageable);
        entities = this.postFetchPage(entities);
        return entities.map(this::toDto);
    }

    public Page<ENTITY> postFetchPage(Page<ENTITY> entities) {
        return entities;
    }

    public Page<DTO> findAllByFilter(FILTER filter, Pageable pageable) {
        this.preSearch();
        Specification<ENTITY> specification = this.search(filter);
        Page<ENTITY> entities = this.repository.findAll(specification, pageable);
        Page<ENTITY> resultPage = this.postSearch(filter, entities);
        return resultPage.map(this::toDto);
    }

    public List<DTO> findAllByFilter(FILTER filter) {
        this.preSearch();
        Specification<ENTITY> specification = this.search(filter);
        List<ENTITY> entities = this.repository.findAll(specification);
        List<ENTITY> resultPage = this.postSearch(filter, entities);
        return resultPage.stream().map(this::toDto).toList();
    }

    public abstract Specification<ENTITY> search(FILTER filter);

    @Transactional(
            readOnly = true
    )
    public boolean existById(String id) {
        return this.repository.existsById(id);
    }

    @Transactional
    public void delete(String id) {
        boolean existsByIdAndDeletedIsFalse = this.repository.existsById(id);
        if (existsByIdAndDeletedIsFalse) {
            this.repository.deleteById(id);
            this.postDelete(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Model with id %s not found", id));
        }
    }

    public void postDelete(String id) {
    }

    public List<ENTITY> preSearch() {
        return null;
    }

    public Page<ENTITY> postSearch(FILTER filter, Page<ENTITY> entities) {
        return entities;
    }

    public List<ENTITY> postSearch(FILTER filter, List<ENTITY> entities) {
        return entities;
    }


}
