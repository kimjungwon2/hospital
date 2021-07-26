package site.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.domain.Estimation;
import site.hospital.domain.Hospital;
import site.hospital.repository.HospitalRepository;
import site.hospital.repository.estimation.EstimationRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EstimationService {
    private final EstimationRepository estimationRepository;
    private final HospitalRepository hospitalRepository;

    //병원 FK 없이 등록
    @Transactional
    public Long createNoHospitalEstimation(Estimation estimation){
        estimationRepository.save(estimation);

        return estimation.getId();
    }

    //병원 FK 있을 때, 등록.
    @Transactional
    public Long createEstimation(Long hospitalId, Estimation estimation){
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(()->new IllegalStateException("해당 id에 속하는 병원이 존재하지 않습니다."));
        estimation.changeHospital(hospital);
        estimationRepository.save(estimation);
        return estimation.getId();
    }

    @Transactional
    public Long createHospitalEstimation(Long hospitalId, Estimation estimation){

        Hospital hospital = hospitalRepository.findById(hospitalId).orElse(null);

        if(hospital==null){
            throw new IllegalStateException("해당 병원은 존재하지 않습니다..");
        }

        List<Estimation> estimationList = estimationRepository.searchEstimation(estimation.getHospitalName(),
                estimation.getCityName());

        // 병원 정보와 평가 정보의 병원이 일치하는지 확인
        if(estimationList.isEmpty()){
            throw new IllegalStateException("평가 정보와 병원 정보가 일치하지 않습니다.");
        }
        estimation.changeHospital(hospital);
        estimationRepository.save(estimation);

        return estimation.getId();
    }


    //평가 전체 보기(관리자)
    public List<Estimation> viewEstimation(){
        List<Estimation> estimationList = estimationRepository.findAll();
        return estimationList;
    }


}
