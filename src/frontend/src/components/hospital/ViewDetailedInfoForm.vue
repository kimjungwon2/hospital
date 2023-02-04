<template>

  <section id="detailedInformation" v-if="this.staffHosInfoId !== null">

    <section class = "detailedInformation__info">
      <h1>소개문</h1>
      <div class="info__introduction">
          {{staffInfo.introduction}}
          <br><br>
      </div>
      <h1>영업 시간</h1>
      <div class="info__consultationHour">
          {{staffInfo.consultationHour}}
          <br><br>
      </div>
      <h1>특이사항</h1>
      <div class="info__abnormality">
          {{staffInfo.abnormality}}
          <br><br>
      </div>
    </section> 

  <section class = "detailedInformation__doctor" v-if="this.doctors.length!==0">
    <h1>의사</h1>        
        <div class="doctor__content" v-for="(doctor, i) in doctors" :key="doctor.doctorId">
            의사 ({{i+1}})<br><br>
            이름: {{doctor.name}}<br>
            경력: {{doctor.history}}<br><br>
        </div>
    </section>
  <section class = "detailedInformation__doctor" v-else>
      <h1>의사</h1>    
      <b>등록된 의사가 없습니다.</b>
  </section>

  </section>

  <section class="detailedInformation" v-else>
      <h3>직원이 등록된 병원이 아니라 상세 정보를 지원하지 않습니다.</h3>
  </section> 

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
.detailedInformation__info{
  position:relative;
  text-align:left;
  left:12%;
  width:73%;
  margin-top:10px;
  border-top: 1px solid #dee2e6!important;
}

.detailedInformation__doctor{
    margin-top:10px;
    border-top: 1px solid #dee2e6!important;
    position:relative;
    text-align:left;
    left:12%;
    width:73%;
    font-size: 16px;
    font-weight: 500;
    margin-bottom:30px;
}

#detailedInformation .info__introduction{
  border-bottom: 1px dotted #e2e2e2!important;
}

#detailedInformation .info__consultationHour{
    border-bottom: 1px dotted #e2e2e2!important;
}

.detailedInformation__doctor .doctor__content{
    margin-top:10px;
    border-bottom: 1px dotted #e2e2e2!important;  
}


</style>