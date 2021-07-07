package site.hospital.repository.Appointment.simplequery;

import lombok.Data;

@Data
public class AppointCustomerSearchResult {
    private Long memberId;
    private Long hospitalId;
    private String memberName;
    private String hospitalName;

}
