package site.hospital.exception.advice;

import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.UserException;
import org.springframework.core.annotation.Order;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.hospital.exception.ErrorResponse;



@Slf4j
@RestControllerAdvice
@Order
public class ApiControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse illegalArgumentHandle(IllegalArgumentException e){
        log.error("IllegalArgumentException:",e);
        return new ErrorResponse("BAD_REQUEST",e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse illegalStateHandle(IllegalStateException e){
        log.error("IllegalStateException:",e);
        return new ErrorResponse("BAD_REQUEST",e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse userHandle(UserException e){
        log.error("UserException:",e);
        return new ErrorResponse("BAD_REQUEST",e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse userHandle(HttpRequestMethodNotSupportedException e){
        log.error("HttpRequestMethodNotSupportedException:",e);
        return new ErrorResponse("BAD_REQUEST",e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse EmptyResultDataAccess(EmptyResultDataAccessException e){
        log.error("EmptyResultDataAccessException:",e);
        return new ErrorResponse("BAD_REQUEST",e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ErrorResponse NullPointerHandle(java.lang.NullPointerException e){
        log.error("NullPointerException:",e);
        return new ErrorResponse("NOT_FOUND", "조건에 해당하는 데이터가 없습니다.");
    }


    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler
    public ErrorResponse BadCredentialsException(org.springframework.security.authentication.BadCredentialsException e){
        log.error("BadCredentialsException:",e);
        return new ErrorResponse("BAD_REQUEST",e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler
    public ErrorResponse ServletException(javax.servlet.ServletException e){
        log.error("javax.servlet.ServletException:",e);
        return new ErrorResponse("BAD_REQUEST","아이디와 비밀번호가 일치하지 않습니다.");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ErrorResponse NullPointerHandle(HttpMessageNotReadableException e){
        log.error("HttpMessageNotReadableException:",e);
        return new ErrorResponse("NOT_FOUND", e.getMessage());
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResponse serverHandle(Exception e){
        log.error("InternalServerException:",e);
        return new ErrorResponse("SERVER_ERROR",e.getMessage());
    }
}
