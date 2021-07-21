package site.hospital.repository.qandA;

import site.hospital.domain.QandA;

import java.util.List;

public interface QandARepositoryCustom {
    public List<QandA> searchHospitalQandA(Long hospitalId);
    public List<QandA> searchQandA(Long memberId, Long hospitalId);
}
