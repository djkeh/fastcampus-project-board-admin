package com.fastcampus.projectboardadmin.config;

import com.fastcampus.projectboardadmin.service.VisitCounterService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import static org.mockito.BDDMockito.given;

@TestConfiguration
public class GlobalControllerConfig {

    @MockBean private VisitCounterService visitCounterService;

    @BeforeTestMethod
    public void securitySetup() {
        given(visitCounterService.visitCount()).willReturn(0L);
    }

}
