package site.hospital.question.manager.service;

import javax.servlet.ServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.hospital.question.user.domain.Question;

public interface ManagerQuestionService {


    Page<Question> searchHospitalQuestions(
            ServletRequest servletRequest,
            String nickName,
            String memberIdName,
            Pageable pageable
    );

    Page<Question> searchNoQuestions(
            ServletRequest servletRequest,
            String nickName,
            String memberIdName,
            Pageable pageable
    );
    
    Long countQuestionsWithNoAnswer(ServletRequest servletRequest);

}
