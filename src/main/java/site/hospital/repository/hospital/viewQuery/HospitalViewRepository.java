package site.hospital.repository.hospital.viewQuery;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import static site.hospital.domain.estimation.QEstimation.estimation;
import static site.hospital.domain.hospital.QHospital.hospital;
import static site.hospital.domain.QPostTag.postTag;
import static site.hospital.domain.detailedHosInformation.QDetailedHosInformation.detailedHosInformation;
import static site.hospital.domain.reviewHospital.QReviewHospital.reviewHospital;
import static site.hospital.domain.QTag.tag;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class HospitalViewRepository {

    private final JPAQueryFactory queryFactory;

    public HospitalViewRepository(EntityManager em){ this.queryFactory = new JPAQueryFactory(em);}

    public ViewHospitalDTO viewHospital(Long hospitalId){
        ViewHospitalDTO result = findHospital(hospitalId);

        //병원 아이디
        Long hosId = result.getHospitalId();

        //병원 평가 넣기
        List<HospitalEstimationDTO> hospitalEstimationDTOS = queryFactory
                .select(new QHospitalEstimationDTO(hospital.id, estimation.distinctionGrade, estimation.estimationList))
                .from(estimation)
                .join(estimation.hospital, hospital)
                .where(estimation.hospital.id.in(hosId))
                .fetch();

        result.setHospitalEstimations(hospitalEstimationDTOS);

        //리뷰 개수 넣기
        List<HospitalReviewDTO> hospitalReviewDTOS = queryFactory
                .select(new QHospitalReviewDTO(reviewHospital.hospital.id, reviewHospital.count()))
                .from(reviewHospital)
                .join(reviewHospital.hospital,hospital)
                .groupBy(reviewHospital.hospital.id)
                .where(reviewHospital.hospital.id.in(hosId))
                .fetch();

        result.setHospitalReviews(hospitalReviewDTOS);

        //태그 넣기
        List<HospitalTagDTO> hospitalTagDTOS = queryFactory
                .select(new QHospitalTagDTO(postTag.hospital.id, postTag.tag.name))
                .from(postTag)
                .join(postTag.tag, tag)
                .where(postTag.hospital.id.in(hosId))
                .fetch();

        result.setHospitalTags(hospitalTagDTOS);

        return result;
    }


    private ViewHospitalDTO findHospital(Long hospitalId){

        ViewHospitalDTO result = queryFactory
                .select(new QViewHospitalDTO(hospital.id, detailedHosInformation.id, hospital.staffHosInformation.id, hospital.licensingDate,hospital.hospitalName,
                        hospital.phoneNumber, hospital.distinguishedName, hospital.medicalSubjectInformation,
                        hospital.businessCondition,hospital.cityName,
                        detailedHosInformation.hospitalAddress.landLotBasedSystem,
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
                .leftJoin(hospital.detailedHosInformation,detailedHosInformation)
                .where(hospitalIdEq(hospitalId))
                .fetchOne();

        return result;
    }

    private BooleanExpression hospitalIdEq(Long id){
        return id != null? hospital.id.eq(id): null;
    }

}
