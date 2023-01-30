package site.hospital.service;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.api.dto.question.QuestionCreateRequest;
import site.hospital.api.dto.question.QuestionCreateResponse;
import site.hospital.api.dto.question.QuestionSearchResponse;
import site.hospital.domain.Answer;
import site.hospital.domain.Question;
import site.hospital.domain.hospital.Hospital;
import site.hospital.domain.member.Member;
import site.hospital.dto.AdminQuestionSearchCondition;
import site.hospital.dto.StaffQuestionSearchCondition;
import site.hospital.repository.AnswerRepository;
import site.hospital.repository.hospital.HospitalRepository;
import site.hospital.repository.member.MemberRepository;
import site.hospital.repository.question.QuestionRepository;
import site.hospital.repository.question.adminSearchQuery.AdminQuestionSearchRepository;
import site.hospital.repository.question.adminSearchQuery.AdminSearchQuestionDto;
import site.hospital.repository.question.simpleQuery.HospitalQuestionRepository;
import site.hospital.repository.question.simpleQuery.SearchHospitalQuestionDTO;
import site.hospital.repository.question.userQuery.SearchUserQuestionDTO;
import site.hospital.repository.question.userQuery.UserQuestionRepository;

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
    private final JwtStaffAccessService jwtStaffAccessService;

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

    //병원 관계자 Question 검색
    public Page<Question> staffSearchHospitalQuestion(
            ServletRequest servletRequest,
            String nickName,
            String memberIdName,
            Pageable pageable
    ) {
        StaffQuestionSearchCondition condition = StaffQuestionSearchCondition
                .builder()
                .nickName(nickName)
                .memberIdName(memberIdName)
                .build();

        Long JwtHospitalId = jwtStaffAccessService.getHospitalNumber(servletRequest);

        Page<Question> questions = questionRepository
                .staffSearchHospitalQuestion(JwtHospitalId, condition, pageable);

        List<QuestionSearchResponse> result = questions.stream()
                .map(q -> QuestionSearchResponse.from(q))
                .collect(Collectors.toList());

        Long total = questions.getTotalElements();

        return new PageImpl(result, pageable, total);
    }

    //병원 관계자 미답변 Question 검색
    public Page<Question> staffSearchNoQuestion(
            ServletRequest servletRequest,
            String nickName,
            String memberIdName,
            Pageable pageable
    ) {
        StaffQuestionSearchCondition condition =
                StaffQuestionSearchCondition
                        .builder()
                        .nickName(nickName)
                        .memberIdName(memberIdName)
                        .build();

        Long JwtHospitalId = jwtStaffAccessService.getHospitalNumber(servletRequest);

        Page<Question> questions = questionRepository
                .staffSearchNoQuestion(JwtHospitalId, condition, pageable);

        List<QuestionSearchResponse> result = questions.stream()
                .map(q -> QuestionSearchResponse.from(q))
                .collect(Collectors.toList());

        Long total = questions.getTotalElements();

        return new PageImpl(result, pageable, total);
    }

    //병원 관계자 미답변 question 갯수 확인
    public Long staffQuestionNoAnswer(ServletRequest servletRequest) {
        Long JwtHospitalId = jwtStaffAccessService.getHospitalNumber(servletRequest);

        return questionRepository.staffQuestionNoAnswer(JwtHospitalId);
    }

    //관리자 병원 Question 검색
    public Page<AdminSearchQuestionDto> adminSearchQuestions(
            String nickName,
            String hospitalName,
            String memberIdName,
            Pageable pageable
    ) {
        AdminQuestionSearchCondition condition = AdminQuestionSearchCondition.builder()
                .nickName(nickName).hospitalName(hospitalName).memberIdName(memberIdName).build();
        return adminQuestionSearchRepository.adminSearchQuestions(condition, pageable);
    }

    //관리자 병원 Question 삭제
    @Transactional
    public void questionDelete(Long questionId, Long answerId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 질문이 존재하지 않습니다."));
        questionRepository.deleteById(questionId);

        //Answer가 있으면 answer도 삭제.
        if (answerId != null) {
            Answer answer = answerRepository.findById(answerId)
                    .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 답변이 존재하지 않습니다."));
            answerRepository.deleteById(answerId);
        }
    }
}
