package site.hospital.exception.advice;

import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.hospital.exception.ErrorResponse;


@Slf4j
@RestControllerAdvice
public class ApiControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse illegalHandle(IllegalArgumentException e){
        log.error("IllegalArgumentException:",e);
        return new ErrorResponse("BAD",e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse illegalHandle(IllegalStateException e){
        log.error("IllegalStateException:",e);
        return new ErrorResponse("BAD",e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse userHandle(UserException e){
        log.error("UserException:",e);
        return new ErrorResponse("BAD",e.getMessage());
    }

}
