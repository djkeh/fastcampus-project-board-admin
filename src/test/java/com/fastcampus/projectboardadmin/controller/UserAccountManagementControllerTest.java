package com.fastcampus.projectboardadmin.controller;

import com.fastcampus.projectboardadmin.config.GlobalControllerConfig;
import com.fastcampus.projectboardadmin.config.TestSecurityConfig;
import com.fastcampus.projectboardadmin.dto.UserAccountDto;
import com.fastcampus.projectboardadmin.service.UserAccountManagementService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("컨트롤러 - 회원 관리")
@Import({TestSecurityConfig.class, GlobalControllerConfig.class})
@WebMvcTest(UserAccountManagementController.class)
class UserAccountManagementControllerTest {

    private final MockMvc mvc;

    @MockBean private UserAccountManagementService userAccountManagementService;


    public UserAccountManagementControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @WithMockUser(username = "tester", roles = "USER")
    @DisplayName("[view][GET] 회원 관리 페이지 - 정상 호출")
    @Test
    void givenNothing_whenRequestingUserAccountManagementView_thenReturnsUserAccountManagementView() throws Exception {
        // Given
        given(userAccountManagementService.getUserAccounts()).willReturn(List.of());

        // When & Then
        mvc.perform(get("/management/user-accounts"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("management/user-accounts"))
                .andExpect(model().attribute("userAccounts", List.of()));
        then(userAccountManagementService).should().getUserAccounts();
    }

    @WithMockUser(username = "tester", roles = "USER")
    @DisplayName("[data][GET] 회원 1개 - 정상 호출")
    @Test
    void givenUserAccountId_whenRequestingUserAccount_thenReturnsUserAccount() throws Exception {
        // Given
        String userId = "uno";
        UserAccountDto userAccountDto = createUserAccountDto(userId, "Uno");
        given(userAccountManagementService.getUserAccount(userId)).willReturn(userAccountDto);

        // When & Then
        mvc.perform(get("/management/user-accounts/" + userId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.nickname").value(userAccountDto.nickname()));
        then(userAccountManagementService).should().getUserAccount(userId);
    }

    @WithMockUser(username = "tester", roles = "MANAGER")
    @DisplayName("[view][POST] 회원 삭제 - 정상 호출")
    @Test
    void givenUserAccountId_whenRequestingDeletion_thenRedirectsToUserAccountManagementView() throws Exception {
        // Given
        String userId = "uno";
        willDoNothing().given(userAccountManagementService).deleteUserAccount(userId);

        // When & Then
        mvc.perform(
                post("/management/user-accounts/" + userId)
                        .with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/management/user-accounts"))
                .andExpect(redirectedUrl("/management/user-accounts"));
        then(userAccountManagementService).should().deleteUserAccount(userId);
    }


    private UserAccountDto createUserAccountDto(String userId, String nickname) {
        return UserAccountDto.of(
                userId,
                "uno-test@email.com",
                nickname,
                "test memo"
        );
    }

}
