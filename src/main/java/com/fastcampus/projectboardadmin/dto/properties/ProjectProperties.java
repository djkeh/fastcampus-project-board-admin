package com.fastcampus.projectboardadmin.dto.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 어드민 프로젝트 전용 프로퍼티
 *
 * @param board 게시판 관련 프로퍼티
 */
@ConfigurationProperties("project")
public record ProjectProperties(Board board) {

    /**
     * 게시판 관련 프로퍼티
     *
     * @param url 게시판 서비스 호스트명
     */
    public record Board(String url) {}
}
