<template>
  <div>
  <div v-if="this.staffHosInfoId !== null">
      <h3>소개문: {{staffInfo.introduction}}<br>
      영업 시간: {{staffInfo.consultationHour}}<br>
      특이사항: {{staffInfo.abnormality}}</h3>
      <div v-if="this.doctors.length!==0">        
        <p v-for="(doctor, i) in doctors" :key="doctor.doctorId">
            의사 ({{i+1}})<br>
            의사 번호: {{doctor.doctorId}}<br>
            이름: {{doctor.name}}<br>
            경력: {{doctor.history}}<br>
        </p>
      </div>
      <div v-else><b>등록된 의사가 없습니다.</b></div>    
  </div>
  <div v-else>
      등록된 병원 추가 정보가 없습니다.
  </div> 
  
  </div>

</template>

<script>
import { viewStaffHosInfo } from '@/api/hospital';

export default {
  data() {
    return {
      staffInfo:[],
      doctors:[],
    };
  },
  props: {
    staffHosInfoId: {
      type: undefined,
      required: true,
    },
  },
  async created(){
    if(this.staffHosInfoId !== null) {
      const{data} = await viewStaffHosInfo(this.staffHosInfoId);
      this.staffInfo = data;
      this.doctors = this.staffInfo.doctors;
    }
  }
}
</script>

<style>

</style>