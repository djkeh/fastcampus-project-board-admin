package com.fastcampus.projectboardadmin.controller;

import com.fastcampus.projectboardadmin.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 게시글 관리")
@Import(SecurityConfig.class)
@WebMvcTest(ArticleManagementController.class)
class ArticleManagementControllerTest {

    private final MockMvc mvc;

    public ArticleManagementControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[view][GET] 게시글 관리 페이지 - 정상 호출")
    @Test
    void givenNothing_whenRequestingArticleManagementView_thenReturnsArticleManagementView() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/management/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("management/articles"));
    }
}
