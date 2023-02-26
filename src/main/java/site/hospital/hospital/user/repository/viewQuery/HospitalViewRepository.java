package site.hospital.hospital.user.repository.viewQuery;

import static site.hospital.hospital.user.domain.QHospitalImage.hospitalImage;
import static site.hospital.tag.manager.domain.QPostTag.postTag;
import static site.hospital.tag.manager.domain.QTag.tag;
import static site.hospital.hospital.user.domain.detailedHosInformation.QDetailedHosInformation.detailedHosInformation;
import static site.hospital.estimation.user.domain.QEstimation.estimation;
import static site.hospital.hospital.user.domain.QHospital.hospital;
import static site.hospital.review.user.domain.reviewHospital.QReviewHospital.reviewHospital;

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

    public ViewHospitalDTO viewHospital(Long hospitalId) {
        ViewHospitalDTO result = findHospital(hospitalId);

        if (result == null) {
            return null;
        }

        //병원 아이디
        Long hosId = result.getHospitalId();

        //병원 평가 넣기
        List<HospitalEstimationDTO> hospitalEstimationDTOS = queryFactory
                .select(new QHospitalEstimationDTO(hospital.id, estimation.distinctionGrade,
                        estimation.estimationList))
                .from(estimation)
                .join(estimation.hospital, hospital)
                .where(estimation.hospital.id.in(hosId))
                .fetch();

        result.setHospitalEstimations(hospitalEstimationDTOS);

        //리뷰 개수 넣기
        Long reviewCount = queryFactory
                .select(new QHospitalReviewDTO(reviewHospital.hospital.id))
                .from(reviewHospital)
                .join(reviewHospital.hospital, hospital)
                .where(reviewHospital.hospital.id.in(hosId))
                .fetchCount();

        result.setHospitalReviewCount(reviewCount);

        //태그 넣기
        List<HospitalTagDTO> hospitalTagDTOS = queryFactory
                .select(new QHospitalTagDTO(postTag.hospital.id, tag.name))
                .from(postTag)
                .join(postTag.tag, tag)
                .where(postTag.hospital.id.in(hosId))
                .fetch();
        result.setHospitalTags(hospitalTagDTOS);

        //병원 내부 이미지 넣기
        List<HospitalImageDTO> hospitalImageDTOS = queryFactory
                .select(new QHospitalImageDTO(hospital.id, hospitalImage.id,
                        hospitalImage.imageKey))
                .from(hospitalImage)
                .join(hospitalImage.hospital, hospital)
                .where(hospitalImage.hospital.id.in(hosId))
                .fetch();

        result.setHospitalImages(hospitalImageDTOS);

        return result;
    }


    private ViewHospitalDTO findHospital(Long hospitalId) {

        ViewHospitalDTO result = queryFactory
                .select(new QViewHospitalDTO(hospital.id, detailedHosInformation.id,
                        hospital.staffHosInformation.id, hospital.hospitalThumbnail.id,
                        hospital.licensingDate, hospital.hospitalName,
                        hospital.phoneNumber, hospital.distinguishedName,
                        hospital.medicalSubjectInformation,
                        hospital.businessCondition, hospital.cityName,
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
