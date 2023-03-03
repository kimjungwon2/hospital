package site.hospital.question.user.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
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
import site.hospital.question.user.repository.hospital.HospitalQuestionRepository;
import site.hospital.question.user.repository.hospital.HospitalQuestionSelectQuery;
import site.hospital.question.user.repository.inquiry.UserQuestionRepository;
import site.hospital.question.user.repository.inquiry.UserQuestionSelectQuery;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    private final HospitalRepository hospitalRepository;
    private final HospitalQuestionRepository hospitalQuestionRepository;
    private final UserQuestionRepository userQuestionRepository;

    @Transactional
    @Override
    public QuestionCreateResponse createQuestion(QuestionCreateRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new IllegalStateException("멤버가 존재하지 않습니다."));
        Hospital hospital = hospitalRepository.findById(request.getHospitalId())
                .orElseThrow(() -> new IllegalStateException("병원이 존재하지 않습니다."));

        Question question = createQuestion(request, member, hospital);

        return QuestionCreateResponse.from(question.getId());
    }

    @Override
    public List<UserQuestionSelectQuery> inquireQuestionsByUser(Long memberId) {
        return userQuestionRepository.inquireQuestionsByUser(memberId);
    }

    @Override
    public List<HospitalQuestionSelectQuery> inquireHospitalQuestions(Long hospitalId) {
        return hospitalQuestionRepository.inquireHospitalQuestions(hospitalId);
    }

    private Question createQuestion(QuestionCreateRequest request, Member member, Hospital hospital) {
        Question question = Question.createQuestion(member, hospital, request.getContent());
        questionRepository.save(question);
        return question;
    }
}
