package site.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.domain.Estimation;
import site.hospital.domain.Hospital;
import site.hospital.repository.estimation.EstimationRepository;
import site.hospital.repository.hospital.HospitalRepository;

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
    public Long createHospitalEstimation(Long hospitalId, Estimation estimation){

        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(()->new IllegalStateException("해당 병원은 존재하지 않습니다."));;

        List<Estimation> estimationList = estimationRepository.searchEstimation(hospital,
                estimation.getEstimationList());

        // 이미 중복된 평가 리스트인지 확인한다.
        if(!estimationList.isEmpty()){
            throw new IllegalStateException("이미 등록된 평가 리스트입니다.");
        }

        estimation.changeHospital(hospital);
        estimationRepository.save(estimation);

        return estimation.getId();
    }

    //평가 삭제하기.
    @Transactional
    public void adminDeleteEstimation(Long estimationId){
        Estimation estimation = estimationRepository.findById(estimationId)
                .orElseThrow(()->new IllegalStateException("해당 id에 속하는 평가가 존재하지 않습니다."));

        estimationRepository.deleteById(estimationId);
    }



    //평가 전체 보기(관리자)
    public List<Estimation> viewEstimation(){
        List<Estimation> estimationList = estimationRepository.findAll();
        return estimationList;
    }
}
