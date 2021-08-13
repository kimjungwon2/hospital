package site.hospital.exception.advice;

import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.UserException;
import org.springframework.core.annotation.Order;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import site.hospital.api.MemberApiController;
import site.hospital.exception.ErrorResponse;

import javax.servlet.ServletException;

@Slf4j
@ControllerAdvice(assignableTypes = MemberApiController.class)
@Order(1)
public class MemberApiControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse BadCredentialsException(BadCredentialsException e){
        log.error("BadCredentialsException:",e);
        return new ErrorResponse("BAD_REQUEST",e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse ServletException(ServletException e){
        log.error("ServletException:",e);
        return new ErrorResponse("BAD_REQUEST","아이디와 비밀번호가 일치하지 않습니다.");
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> signUpValidException(MethodArgumentNotValidException e){
        log.error("signUp validation error",e);
        ErrorResponse errorResponse = new ErrorResponse("BAD_REQUEST",
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
