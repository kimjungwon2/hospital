package site.hospital.common.exception.advice;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import site.hospital.common.exception.ServiceException;
import site.hospital.member.user.api.MemberController;
import site.hospital.common.exception.ErrorResponse;

@Slf4j
@ControllerAdvice(assignableTypes = MemberController.class)
@Order(1)
public class MemberApiControllerAdvice {

    private static final String BAD_CODE = "BAD_REQUEST";

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse badCredentialsException(BadCredentialsException e) {
        log.error("BadCredentialsException:", e);
        return new ErrorResponse(BAD_CODE, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse servletException(ServletException e) {
        log.error("ServletException:", e);
        return new ErrorResponse(BAD_CODE, "아이디와 비밀번호가 일치하지 않습니다.");
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> signUpValidException(MethodArgumentNotValidException e) {
        log.error("signUp validation error", e);
        ErrorResponse errorResponse = new ErrorResponse(BAD_CODE,
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResponse serverHandle(Exception e) {
        log.error("InternalServerException:", e);
        return new ErrorResponse("SERVER_ERROR", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse illegalStateHandle(IllegalStateException e) {
        log.error("IllegalStateException:", e);
        return new ErrorResponse(BAD_CODE, e.getMessage());
    }


    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Map<String, Object>> ServiceHandle(ServiceException e) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("code", e.getStatus());
        responseBody.put("message", e.getMessage());

        log.error("ServiceException:", e);

        return new ResponseEntity<>(responseBody, e.getStatus());
    }
}
