package com.fastcampus.projectboardadmin.dto.response;

import com.fastcampus.projectboardadmin.dto.UserAccountDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record UserAccountClientResponse(
        @JsonProperty("_embedded") Embedded embedded,
        @JsonProperty("page") Page page
) {

    public static UserAccountClientResponse empty() {
        return new UserAccountClientResponse(
                new Embedded(List.of()),
                new Page(1, 0, 1, 0)
        );
    }

    public static UserAccountClientResponse of(List<UserAccountDto> userAccounts) {
        return new UserAccountClientResponse(
                new Embedded(userAccounts),
                new Page(userAccounts.size(), userAccounts.size(), 1, 0)
        );
    }

    public List<UserAccountDto> userAccounts() { return this.embedded().userAccounts(); }

    public record Embedded(List<UserAccountDto> userAccounts) {}

    public record Page(
            int size,
            long totalElements,
            int totalPages,
            int number
    ) {}

}
