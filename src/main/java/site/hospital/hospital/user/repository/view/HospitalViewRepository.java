package site.hospital.hospital.user.repository.view;

import static site.hospital.estimation.user.domain.QEstimation.estimation;
import static site.hospital.hospital.user.domain.QHospital.hospital;
import static site.hospital.hospital.user.domain.QHospitalImage.hospitalImage;
import static site.hospital.hospital.user.domain.detailedinfo.QDetailedHosInformation.detailedHosInformation;
import static site.hospital.review.user.domain.reviewHospital.QReviewHospital.reviewHospital;
import static site.hospital.tag.manager.domain.QPostTag.postTag;
import static site.hospital.tag.manager.domain.QTag.tag;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class HospitalViewRepository {

    private final JPAQueryFactory queryFactory;

    public HospitalViewRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public HospitalViewSelectQuery viewHospital(Long hospitalId) {
        HospitalViewSelectQuery hospitalResult = findHospital(hospitalId);

        if (hospitalResult == null) {
            return null;
        }

        Long hospitalResultId = hospitalResult.getHospitalId();

        insertEstimations(hospitalResult, hospitalResultId);
        insertReviewCount(hospitalResult, hospitalResultId);
        insertTags(hospitalResult, hospitalResultId);
        insertHospitalImages(hospitalResult, hospitalResultId);

        return hospitalResult;
    }

    private void insertHospitalImages(HospitalViewSelectQuery hospitalResult, Long hospitalResultId) {
        List<HospitalViewImagesDTO> hospitalViewImagesDTOS = queryFactory
                .select(new QHospitalViewImagesDTO(
                        hospital.id,
                        hospitalImage.id,
                        hospitalImage.imageKey))
                .from(hospitalImage)
                .join(hospitalImage.hospital, hospital)
                .where(hospitalImage.hospital.id.in(hospitalResultId))
                .fetch();

        hospitalResult.setHospitalImages(hospitalViewImagesDTOS);
    }

    private void insertTags(HospitalViewSelectQuery hospitalResult, Long hospitalResultId) {
        List<HospitalViewTagDTO> hospitalViewTagDTOS = queryFactory
                .select(new QHospitalViewTagDTO(postTag.hospital.id, tag.name))
                .from(postTag)
                .join(postTag.tag, tag)
                .where(postTag.hospital.id.in(hospitalResultId))
                .fetch();

        hospitalResult.setHospitalTags(hospitalViewTagDTOS);
    }

    private void insertReviewCount(HospitalViewSelectQuery hospitalResult, Long hospitalResultId) {
        Long reviewCount = queryFactory
                .select(new QHospitalViewReviewDTO(reviewHospital.hospital.id))
                .from(reviewHospital)
                .join(reviewHospital.hospital, hospital)
                .where(reviewHospital.hospital.id.in(hospitalResultId))
                .fetchCount();

        hospitalResult.setHospitalReviewCount(reviewCount);
    }

    private void insertEstimations(HospitalViewSelectQuery hospitalResult, Long hospitalResultId) {
        List<HospitalViewEstimationDTO> hospitalEstimationDTOS = queryFactory
                .select(new QHospitalViewEstimationDTO(
                        hospital.id,
                        estimation.distinctionGrade,
                        estimation.estimationList))
                .from(estimation)
                .join(estimation.hospital, hospital)
                .where(estimation.hospital.id.in(hospitalResultId))
                .fetch();

        hospitalResult.setHospitalEstimations(hospitalEstimationDTOS);
    }

    private HospitalViewSelectQuery findHospital(Long hospitalId) {

        HospitalViewSelectQuery result = queryFactory
                .select(new QHospitalViewSelectQuery(
                        hospital.id,
                        detailedHosInformation.id,
                        hospital.staffHosInformation.id,
                        hospital.hospitalThumbnail.id,
                        hospital.licensingDate,
                        hospital.hospitalName,
                        hospital.phoneNumber,
                        hospital.distinguishedName,
                        hospital.medicalSubjectInformation,
                        hospital.businessCondition,
                        hospital.cityName,
                        detailedHosInformation.hospitalAddress.landLotBasedSystem,
                        detailedHosInformation.hospitalAddress.zipCode,
                        detailedHosInformation.hospitalAddress.roadBaseAddress,
                        detailedHosInformation.numberHealthcareProvider,
                        detailedHosInformation.numberWard,
                        detailedHosInformation.numberPatientRoom,
                        detailedHosInformation.hospitalLocation.x_coordination,
                        detailedHosInformation.hospitalLocation.y_coordination,
                        detailedHosInformation.hospitalLocation.latitude,
                        detailedHosInformation.hospitalLocation.longitude
                ))
                .from(hospital)
                .leftJoin(hospital.detailedHosInformation, detailedHosInformation)
                .where(hospitalIdEq(hospitalId))
                .fetchOne();

        return result;
    }

    private BooleanExpression hospitalIdEq(Long id) {
        return id != null ? hospital.id.eq(id) : null;
    }

}
