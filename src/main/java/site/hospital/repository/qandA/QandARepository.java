package site.hospital.repository.qandA;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.domain.QandA;

public interface QandARepository extends JpaRepository<QandA,Long>, QandARepositoryCustom {
}
