package site.hospital.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TespApiController {

    @GetMapping("/test")
    public String test(){
        return "hello";
    }

}
