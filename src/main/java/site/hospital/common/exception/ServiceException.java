package site.hospital.common.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ServiceException extends RuntimeException{

    private HttpStatus status;
    private String message;

    public ServiceException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
