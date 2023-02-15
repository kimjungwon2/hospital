package site.hospital.common.exception.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import site.hospital.hospital.user.api.HospitalApiController;
import site.hospital.common.exception.ErrorResponse;

@Slf4j
@ControllerAdvice(assignableTypes = HospitalApiController.class)
@Order(2)
public class HospitalApiControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> hospitalSearchValidException(
            MethodArgumentNotValidException e) {
        log.error("hospitalSearch validation error", e);
        ErrorResponse errorResponse = new ErrorResponse("BAD_REQUEST",
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
