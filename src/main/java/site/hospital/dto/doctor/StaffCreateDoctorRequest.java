package site.hospital.dto.doctor;


import lombok.Data;

@Data
public class StaffCreateDoctorRequest {

   private Long staffHosId;
   private String name;
   private String history;
   private Long memberId;
}
