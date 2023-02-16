package site.hospital.question.user.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.hospital.user.domain.Hospital;
import site.hospital.hospital.user.repository.HospitalRepository;
import site.hospital.member.user.domain.Member;
import site.hospital.member.user.repository.MemberRepository;
import site.hospital.question.user.api.dto.QuestionCreateRequest;
import site.hospital.question.user.api.dto.QuestionCreateResponse;
import site.hospital.question.user.domain.Question;
import site.hospital.question.user.repository.QuestionRepository;
import site.hospital.question.user.repository.simpleQuery.HospitalQuestionRepository;
import site.hospital.question.user.repository.simpleQuery.SearchHospitalQuestionDTO;
import site.hospital.question.user.repository.userQuery.SearchUserQuestionDTO;
import site.hospital.question.user.repository.userQuery.UserQuestionRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    private final HospitalRepository hospitalRepository;
    private final HospitalQuestionRepository hospitalQuestionRepository;
    private final UserQuestionRepository userQuestionRepository;

    //Question 작성
    @Transactional
    public QuestionCreateResponse questionCreate(QuestionCreateRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 멤버가 존재하지 않습니다."));
        Hospital hospital = hospitalRepository.findById(request.getHospitalId())
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 병원이 존재하지 않습니다."));

        Question question = Question.CreateQuestion(member, hospital, request.getContent());
        questionRepository.save(question);

        return QuestionCreateResponse.from(question.getId());
    }

    //QandA 수정
    @Transactional
    public Long modifyQandA(Long qanda_id, String content) {
        Question question = questionRepository.findById(qanda_id)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 질문이 존재하지 않습니다."));
        question.modifyQandA(content);

        return question.getId();
    }

    //QandA 삭제
    @Transactional
    public void deleteQandA(Long id) {
        questionRepository.deleteById(id);
    }

    //병원 Question user가 조회
    public List<SearchUserQuestionDTO> searchUserQuestion(Long memberId) {
        List<SearchUserQuestionDTO> question = userQuestionRepository.viewUserQuestion(memberId);

        return question;
    }

    //병원 Question 조회
    public List<SearchHospitalQuestionDTO> searchHospitalQuestion(Long hospitalId) {
        return hospitalQuestionRepository.viewHospitalQuestion(hospitalId);
    }

    //멤버 자신의 Question 조회
    public Page<Question> searchMemberQuestion(Long memberId, Pageable pageable) {
        Page<Question> question = questionRepository.searchQuestion(memberId, pageable);

        return question;
    }

}
