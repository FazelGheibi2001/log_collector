package com.example.log_collect.log;

import com.example.log_collect.LogCollectApplication;
import com.example.log_collect.dtos.LogDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;

import static com.example.log_collect.CommonTestData.*;
import static com.example.log_collect.CommonTestData.loginDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {LogCollectApplication.class})
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles({"test"})
@AutoConfigureMockMvc
public class LogControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private LogService service;
    @Autowired
    private LogDataProvider dataProvider;
    private LogDTO dto;

    @BeforeEach
    public void initTest() {
        dto = dataProvider.createEntity();
    }

    @Test
    @Transactional
    public void saveLog() throws Exception {
        int databaseSizeBeforeSave = service.findAll().size();

        mockMvc.perform(post("/api/v1/log")
                        .header("Authorization", token())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        LogDTO entity = service.findAll().get(databaseSizeBeforeSave);

        assertThat(service.findAll()).hasSize(databaseSizeBeforeSave + 1);
        assertThat(entity.getId()).isNotBlank();
        assertThat(entity.getBody()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getRequestId()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getServiceName()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(entity.getProcessed()).isNotNull();
        assertThat(entity.getCreatedAt()).isNotNull();
    }

    @Test
    @Transactional
    public void getLogs() throws Exception {
        LogDTO entity = service.save(dto);

        mockMvc.perform(get("/api/v1/log/find-all")
                        .header("Authorization", token()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].body").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[0].requestId").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[0].serviceName").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[0].type").value(DEFAULT_TYPE.name()))
                .andExpect(jsonPath("$.[0].processed").isNotEmpty())
                .andExpect(jsonPath("$.[0].createdAt").isNotEmpty());
    }

    @Test
    @Transactional
    public void getPageableLogs() throws Exception {
        LogDTO entity = service.save(dto);

        mockMvc.perform(get("/api/v1/log?size=10&page=0")
                        .header("Authorization", token()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.content.[0].body").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.content.[0].requestId").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.content.[0].serviceName").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.content.[0].type").value(DEFAULT_TYPE.name()))
                .andExpect(jsonPath("$.content.[0].processed").isNotEmpty())
                .andExpect(jsonPath("$.content.[0].createdAt").isNotEmpty());
    }

    @Test
    @Transactional
    public void getLogById() throws Exception {
        LogDTO entity = service.save(dto);

        mockMvc.perform(get("/api/v1/log/{id}", entity.getId())
                        .header("Authorization", token()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(entity.getId()))
                .andExpect(jsonPath("$.body").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.requestId").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.serviceName").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.name()))
                .andExpect(jsonPath("$.processed").isNotEmpty())
                .andExpect(jsonPath("$.createdAt").isNotEmpty());
    }

    @Test
    @Transactional
    public void updateLog() throws Exception {
        int databaseSizeBeforeSave = service.findAll().size();
        LogDTO entity = service.save(dto);

        entity = dataProvider.updateEntity(entity);

        mockMvc.perform(put("/api/v1/log")
                        .header("Authorization", token())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entity)))
                .andExpect(status().isAccepted());

        LogDTO newEntity = service.findAll().get(databaseSizeBeforeSave);

        assertThat(newEntity.getId()).isNotBlank();
        assertThat(newEntity.getBody()).isEqualTo(UPDATED_STRING);
        assertThat(newEntity.getRequestId()).isEqualTo(DEFAULT_STRING);
        assertThat(newEntity.getServiceName()).isEqualTo(UPDATED_STRING);
        assertThat(newEntity.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(newEntity.getProcessed()).isNotNull();
        assertThat(newEntity.getCreatedAt()).isNotNull();
    }

    @Test
    @Transactional
    public void deleteLog() throws Exception {
        LogDTO entity = service.save(dto);
        int databaseSizeAfterSave = service.findAll().size();

        mockMvc.perform(delete("/api/v1/log/{id}", entity.getId())
                        .header("Authorization", token()))
                .andExpect(status().isNoContent());

        assertThat(service.findAll()).hasSize(databaseSizeAfterSave - 1);
    }

    @Test
    @Transactional
    public void getLogsByBody() throws Exception {
        LogDTO entity = service.save(dto);

        mockMvc.perform(get("/api/v1/log/filter?page=0&size=10&body=" + entity.getBody())
                        .header("Authorization", token()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.content.[0].body").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.content.[0].requestId").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.content.[0].serviceName").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.content.[0].type").value(DEFAULT_TYPE.name()))
                .andExpect(jsonPath("$.content.[0].processed").isNotEmpty())
                .andExpect(jsonPath("$.content.[0].createdAt").isNotEmpty());
    }

    @Test
    @Transactional
    public void getLogsByType() throws Exception {
        LogDTO entity = service.save(dto);

        mockMvc.perform(get("/api/v1/log/filter?page=0&size=10&type=" + entity.getType())
                        .header("Authorization", token()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.content.[0].body").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.content.[0].requestId").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.content.[0].serviceName").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.content.[0].type").value(DEFAULT_TYPE.name()))
                .andExpect(jsonPath("$.content.[0].processed").isNotEmpty())
                .andExpect(jsonPath("$.content.[0].createdAt").isNotEmpty());
    }

    private String token() {
        MvcResult mvcResult = null;
        try {
            mvcResult = mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginDTO())))
                    .andExpect(status().isOk())
                    .andReturn();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return mvcResult.getResponse().getHeader("Authorization");
    }

}
