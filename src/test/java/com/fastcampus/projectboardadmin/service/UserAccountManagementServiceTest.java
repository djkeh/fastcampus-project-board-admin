package com.fastcampus.projectboardadmin.service;

import com.fastcampus.projectboardadmin.dto.UserAccountDto;
import com.fastcampus.projectboardadmin.dto.properties.ProjectProperties;
import com.fastcampus.projectboardadmin.dto.response.UserAccountClientResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ActiveProfiles("test")
@DisplayName("비즈니스 로직 - 회원 관리")
class UserAccountManagementServiceTest {

    @Disabled("실제 API 호출 결과 관찰용이므로 평상시엔 비활성화")
    @DisplayName("실제 API 호출 테스트")
    @SpringBootTest
    @Nested
    class RealApiTest {

        private final UserAccountManagementService sut;

        @Autowired
        public RealApiTest(UserAccountManagementService sut) {
            this.sut = sut;
        }

        @DisplayName("회원 API을 호출하면, 회원 정보를 가져온다.")
        @Test
        void givenNothing_whenCallingUserAccountApi_thenReturnsUserAccountList() {
            // Arrange

            // Act
            List<UserAccountDto> result = sut.getUserAccounts();

            // Assert
            System.out.println(result.stream().findFirst()); // 이 테스트의 목적은 API 호출 결과를 관찰하는 것
            assertThat(result).isNotNull();
        }
    }


    @DisplayName("API mocking 테스트")
    @EnableConfigurationProperties(ProjectProperties.class)
    @AutoConfigureWebClient(registerRestTemplate = true)
    @RestClientTest(UserAccountManagementService.class)
    @Nested
    class RestTemplateTest {

        private final UserAccountManagementService sut;
        private final ProjectProperties projectProperties;
        private final MockRestServiceServer server;
        private final ObjectMapper mapper;

        @Autowired
        public RestTemplateTest(
                UserAccountManagementService sut,
                ProjectProperties projectProperties,
                MockRestServiceServer server,
                ObjectMapper mapper

        ) {
            this.sut = sut;
            this.projectProperties = projectProperties;
            this.server = server;
            this.mapper = mapper;
        }

        @DisplayName("회원 목록 API을 호출하면, 회원들을 가져온다.")
        @Test
        void givenNothing_whenCallingUserAccountsApi_thenReturnsUserAccountList() throws Exception {
            // Given
            UserAccountDto expectedUserAccount = createUserAccountDto("uno", "Uno");
            UserAccountClientResponse expectedResponse = UserAccountClientResponse.of(List.of(expectedUserAccount));
            server
                    .expect(requestTo(projectProperties.board().url() + "/api/userAccounts?size=10000"))
                    .andRespond(withSuccess(
                            mapper.writeValueAsString(expectedResponse),
                            MediaType.APPLICATION_JSON
                    ));


            // When
            List<UserAccountDto> result = sut.getUserAccounts();

            // Then
            assertThat(result).first()
                    .hasFieldOrPropertyWithValue("userId", expectedUserAccount.userId())
                    .hasFieldOrPropertyWithValue("nickname", expectedUserAccount.nickname());
            server.verify();
        }

        @DisplayName("회원 ID와 함께 회원 API을 호출하면, 회원을 가져온다.")
        @Test
        void givenUserAccountId_whenCallingUserAccountApi_thenReturnsUserAccount() throws Exception {
            // Given
            String userId = "uno";
            UserAccountDto expectedUserAccount = createUserAccountDto(userId, "Uno");
            server
                    .expect(requestTo(projectProperties.board().url() + "/api/userAccounts/" + userId))
                    .andRespond(withSuccess(
                            mapper.writeValueAsString(expectedUserAccount),
                            MediaType.APPLICATION_JSON
                    ));


            // When
            UserAccountDto result = sut.getUserAccount(userId);

            // Then
            assertThat(result)
                    .hasFieldOrPropertyWithValue("userId", expectedUserAccount.userId())
                    .hasFieldOrPropertyWithValue("nickname", expectedUserAccount.nickname());
            server.verify();
        }

        @DisplayName("회원 ID와 함께 회원 삭제 API을 호출하면, 회원을 삭제한다.")
        @Test
        void givenUserAccountId_whenCallingDeleteUserAccountApi_thenDeletesUserAccount() throws Exception {
            // Given
            String userId = "uno";
            server
                    .expect(requestTo(projectProperties.board().url() + "/api/userAccounts/" + userId))
                    .andExpect(method(HttpMethod.DELETE))
                    .andRespond(withSuccess());

            // When
            sut.deleteUserAccount(userId);

            // Then
            server.verify();
        }
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
