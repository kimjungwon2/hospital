package site.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.domain.Answer;
import site.hospital.domain.Hospital;
import site.hospital.domain.Question;
import site.hospital.domain.member.Member;
import site.hospital.dto.AdminQuestionSearchCondition;
import site.hospital.repository.AnswerRepository;
import site.hospital.repository.hospital.HospitalRepository;
import site.hospital.repository.question.QuestionRepository;
import site.hospital.repository.member.MemberRepository;
import site.hospital.repository.question.adminSearchQuery.AdminQuestionSearchRepository;
import site.hospital.repository.question.adminSearchQuery.AdminSearchQuestionDto;
import site.hospital.repository.question.simpleQuery.HospitalQuestionRepository;
import site.hospital.repository.question.simpleQuery.SearchHospitalQuestionDTO;
import site.hospital.repository.question.userQuery.SearchUserQuestionDTO;
import site.hospital.repository.question.userQuery.UserQuestionRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    private final HospitalRepository hospitalRepository;
    private final HospitalQuestionRepository hospitalQuestionRepository;
    private final UserQuestionRepository userQuestionRepository;
    private final AdminQuestionSearchRepository adminQuestionSearchRepository;
    private final AnswerRepository answerRepository;

    //Question 작성
    @Transactional
    public Long questionCreate(Long memberId, Long hospitalId, String content){
        Member member = memberRepository.findById(memberId).orElse(null);
        Hospital hospital = hospitalRepository.findById(hospitalId).orElse(null);

        Question question = Question.CreateQuestion(member,hospital,content);
        questionRepository.save(question);

        return question.getId();
    }

    //QandA 수정
    @Transactional
    public Long modifyQandA(Long qanda_id, String content){
        QandA qandA = qandARepository.findById(qanda_id).orElse(null);
        qandA.modifyQandA(content);

        return qandA.getId();
    }

    //QandA 삭제
    @Transactional
    public void deleteQandA(Long id){
        qandARepository.deleteById(id);
    }

    //병원 Question user가 조회
    public List<SearchUserQuestionDTO> searchUserQuestion(Long memberId){
        List<SearchUserQuestionDTO> question = userQuestionRepository.viewUserQuestion(memberId);

        return question;
    }

    //병원 Question 조회
    public List<Question> searchHospitalQandA2(Long hospitalId){
        List<Question> question = questionRepository.searchHospitalQuestion(hospitalId);

        return question;
    }

    //멤버 자신의 Question 조회
    public Page<Question> searchMemberQuestion(Long memberId, Pageable pageable){
        Page<Question> question = questionRepository.searchQuestion(memberId, null, pageable);

        return question;
    }

    //관리자 병원 Question 조회
    public Page<AdminSearchQuestionDto> adminQuestions(Pageable pageable){
        return adminQuestionSearchRepository.adminQuestions(pageable);
    }

    //관리자 병원 Question 검색
    public Page<AdminSearchQuestionDto> adminSearchQuestions(AdminQuestionSearchCondition condition, Pageable pageable){
        return adminQuestionSearchRepository.adminSearchQuestions(condition,pageable);
    }

    //관리자 병원 Question 삭제
    @Transactional
    public void questionDelete(Long questionId, Long answerId){
        Question question = questionRepository.findById(questionId).orElse(null);
        questionRepository.deleteById(questionId);

        //Answer가 있으면 answer도 삭제.
        if(answerId !=null) {
            Answer answer =   answerRepository.findById(answerId).orElse(null);
            answerRepository.deleteById(answerId);
        }
    }
}
