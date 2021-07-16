package site.hospital.exception.advice;

import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import site.hospital.api.MemberApiController;
import site.hospital.exception.ErrorResponse;

@Slf4j
@ControllerAdvice(assignableTypes = MemberApiController.class)
public class MemberApiControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> signUpValidException(MethodArgumentNotValidException e){
        log.error("signUp validation error",e);
        ErrorResponse errorResponse = new ErrorResponse("BAD_REQUEST",
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResponse serverHandle(Exception e){
        log.error("InternalServerException:",e);
        return new ErrorResponse("SERVER_ERROR",e.getMessage());
    }
}
