package site.hospital.answer.manager.service;

import javax.servlet.ServletRequest;
import site.hospital.answer.manager.api.dto.AnswerCreateRequest;

public interface ManagerAnswerService {


    Long registerAnswer(
            ServletRequest servletRequest,
            AnswerCreateRequest request
    );

}
