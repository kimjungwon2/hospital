package site.hospital.common.exception.advice;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import java.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.hospital.common.exception.ErrorResponse;


@Slf4j
@RestControllerAdvice
@Order
public class ApiControllerAdvice {

    private static final String BAD_CODE = "BAD_REQUEST";

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse illegalArgumentHandle(IllegalArgumentException e) {
        log.error("IllegalArgumentException:", e);
        return new ErrorResponse(BAD_CODE, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse illegalStateHandle(IllegalStateException e) {
        log.error("IllegalStateException:", e);
        return new ErrorResponse(BAD_CODE, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse userHandle(HttpRequestMethodNotSupportedException e) {
        log.error("HttpRequestMethodNotSupportedException:", e);
        return new ErrorResponse(BAD_CODE, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse emptyResultDataAccess(EmptyResultDataAccessException e) {
        log.error("EmptyResultDataAccessException:", e);
        return new ErrorResponse(BAD_CODE, e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ErrorResponse nullPointerHandle(java.lang.NullPointerException e) {
        log.error("NullPointerException:", e);
        return new ErrorResponse("NOT_FOUND", "조건에 해당하는 데이터가 없습니다.");
    }


    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler
    public ErrorResponse badCredentialsException(
            org.springframework.security.authentication.BadCredentialsException e) {
        log.error("BadCredentialsException:", e);
        return new ErrorResponse(BAD_CODE, e.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler
    public ErrorResponse badCredentialsException(
            org.springframework.security.access.AccessDeniedException e) {
        log.error("AccessDeniedException:", e);
        return new ErrorResponse("FORBIDDEN", e.getMessage());
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ErrorResponse nullPointerHandle(HttpMessageNotReadableException e) {
        log.error("HttpMessageNotReadableException:", e);
        return new ErrorResponse("NOT_FOUND", e.getMessage());
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResponse serverHandle(Exception e) {
        log.error("InternalServerException:", e);
        return new ErrorResponse("SERVER_ERROR", e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler
    public ErrorResponse signatureHandle(SignatureException e) {
        log.error("UnAuthorizedException:", e);
        return new ErrorResponse(BAD_CODE, "아이디와 비밀번호가 일치하지 않습니다.");
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler
    public ErrorResponse malformedJwtHandle(MalformedJwtException e) {
        log.error("UnAuthorizedException:", e);
        return new ErrorResponse(BAD_CODE, "올바르지 않은 토큰입니다.");
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler
    public ErrorResponse expiredJwtHandle(ExpiredJwtException e) {
        log.error("UnAuthorizedException:", e);
        return new ErrorResponse(BAD_CODE, "토큰이 만료되었습니다. 다시 로그인해주세요.");
    }

}
