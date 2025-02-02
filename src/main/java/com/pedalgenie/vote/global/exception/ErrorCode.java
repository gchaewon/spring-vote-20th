package com.pedalgenie.vote.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 400
    BAD_REQUEST(HttpStatus.BAD_REQUEST, 400, "잘못된 요청입니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, 400, "비밀번호가 일치하지 않습니다."),
    NOT_EXISTS_PART_MEMBER(HttpStatus.BAD_REQUEST, 400, "해당 파트의 구성원이 아닙니다."),
    CANNOT_VOTE_FOR_ANOTHER_PART(HttpStatus.BAD_REQUEST, 400, "다른 파트의 후보를 투표할 수 없습니다."),
    CANNOT_VOTE_FOR_OWN_TEAM(HttpStatus.BAD_REQUEST, 400, "자신의 팀을 투표할 수 없습니다."),

    // 401
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, 401, "인증에 실패했습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, 401, "토큰이 만료되었습니다."),
    NOT_VALID_TOKEN(HttpStatus.UNAUTHORIZED, 401, "토큰이 유효하지 않습니다."),
    NOT_EXISTS_AUTHORIZATION(HttpStatus.UNAUTHORIZED, 401, "Authorization 헤더가 비어 있습니다."),
    NOT_VALID_BEARER_GRANT_TYPE(HttpStatus.UNAUTHORIZED, 401, "인증 타입이 Bearer 타입이 아닙니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, 401, "refresh token이 존재하지 않습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, 401, "refresh token이 만료되었습니다."),
    NOT_ACCESS_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, 401, "해당 토큰은 ACCESS TOKEN이 아닙니다."),

    // 403
    NO_PERMISSION(HttpStatus.FORBIDDEN, 403, "권한이 없습니다."),
    FORBIDDEN_ROLE(HttpStatus.FORBIDDEN, 403, "허용되지 않은 역할을 가진 유저의 요청입니다."),

    // 404
    NOT_EXISTS_MEMBER_ID(HttpStatus.NOT_FOUND, 404, "존재하지 않는 멤버 아이디입니다."),
    NOT_EXISTS_MEMBER_NAME(HttpStatus.NOT_FOUND, 404, "존재하지 않는 멤버 이름입니다."),
    NOT_EXISTS_MEMBER_EMAIL(HttpStatus.NOT_FOUND, 404, "존재하지 않는 멤버 이메일입니다."),


    // 409
    ALREADY_REGISTERED_MEMBER_EMAIL(HttpStatus.CONFLICT, 409, "이미 가입된 이메일입니다."),
    ALREADY_REGISTERED_LOGIN_ID(HttpStatus.CONFLICT,409,"이미 가입된 아이디입니다."),
    ALREADY_VOTED(HttpStatus.CONFLICT, 409,  "이미 투표한 유저의 요청입니다."),

    // 500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "서버 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
}