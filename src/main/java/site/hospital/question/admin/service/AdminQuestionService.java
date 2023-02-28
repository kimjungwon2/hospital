package site.hospital.question.admin.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.hospital.question.admin.repository.search.AdminQuestionSearchSelectQuery;

public interface AdminQuestionService {

    Page<AdminQuestionSearchSelectQuery> searchQuestions(
            String nickName,
            String hospitalName,
            String memberIdName,
            Pageable pageable
    );

    void deleteQuestion(Long questionId, Long answerId);

}
