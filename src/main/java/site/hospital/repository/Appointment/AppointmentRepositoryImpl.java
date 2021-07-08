package site.hospital.repository.Appointment;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import site.hospital.domain.Appointment;

import javax.persistence.EntityManager;
import java.util.List;

import static site.hospital.domain.QAppointment.appointment;
import static site.hospital.domain.member.QMember.member;
import static site.hospital.domain.QHospital.hospital;



public class AppointmentRepositoryImpl implements AppointmentRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public AppointmentRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public List<Appointment> searchAppointment(Long memberId, Long hospitalId){
        List<Appointment> result = queryFactory
                .select(appointment)
                .from(appointment)
                .join(appointment.member, member).fetchJoin()
                .join(appointment.hospital,hospital).fetchJoin()
                .where(memberIdEq(memberId),hospitalIdEq(hospitalId))
                .fetch();

        return result;
    }

    private BooleanExpression memberIdEq(Long id){
        return id != null? appointment.member.id.eq(id): null;
    }
    private BooleanExpression hospitalIdEq(Long id){
        return id != null? appointment.hospital.id.eq(id): null;
    }

}
