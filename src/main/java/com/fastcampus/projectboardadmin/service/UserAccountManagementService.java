package com.fastcampus.projectboardadmin.service;

import com.fastcampus.projectboardadmin.dto.UserAccountDto;
import com.fastcampus.projectboardadmin.dto.properties.ProjectProperties;
import com.fastcampus.projectboardadmin.dto.response.UserAccountClientResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserAccountManagementService {

    private final RestTemplate restTemplate;
    private final ProjectProperties projectProperties;

    public List<UserAccountDto> getUserAccounts() {
        URI uri = UriComponentsBuilder.fromHttpUrl(projectProperties.board().url() + "/api/userAccounts")
                .queryParam("size", 10000) // TODO: 전체 회원 목록을 가져오기 위해 충분히 큰 사이즈를 전달하는 방식. 불완전하다.
                .build()
                .toUri();
        UserAccountClientResponse response = restTemplate.getForObject(uri, UserAccountClientResponse.class);

        return Optional.ofNullable(response).orElseGet(UserAccountClientResponse::empty).userAccounts();
    }

    public UserAccountDto getUserAccount(String userId) {
        URI uri = UriComponentsBuilder.fromHttpUrl(projectProperties.board().url() + "/api/userAccounts/" + userId)
                .build()
                .toUri();
        UserAccountDto response = restTemplate.getForObject(uri, UserAccountDto.class);

        return Optional.ofNullable(response)
                .orElseThrow(() -> new NoSuchElementException("게시글이 없습니다 - userId: " + userId));
    }

    public void deleteUserAccount(String userId) {
        URI uri = UriComponentsBuilder.fromHttpUrl(projectProperties.board().url() + "/api/userAccounts/" + userId)
                .build()
                .toUri();
        restTemplate.delete(uri);
    }

}
