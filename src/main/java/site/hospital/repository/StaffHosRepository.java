package site.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.domain.StaffHosInformation;

public interface StaffHosRepository extends JpaRepository<StaffHosInformation,Long> {
}
