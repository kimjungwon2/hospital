<template>
  <section id="hospitalInformation">
      <ViewMapForm v-if= "this.hospital.detailedHosId !==null"
      :detailed="this.detailed"
      >
      </ViewMapForm>
      <div class="hospitalInformation__location">
        <div class="location__info">
            <font-awesome-icon icon="map-marker-alt"/>  (번지) {{ hospital.landLotBasedSystem}}
        </div>
        <div class="location__info">
            <font-awesome-icon icon="map-marker"/>  (도로명) {{ hospital.roadBaseAddress}}
        </div>
        <div class="location__info">
            <font-awesome-icon icon="phone-alt"/>  {{hospital.phoneNumber}}
        </div>
      </div>

      <div class="hospitalInformation__info">
          <h1>병원정보</h1>
          병원 종류: {{hospital.distinguishedName}}<br>
          과목: {{hospital.medicalSubjectInformation}}<br>
          개업일: {{licensingDate | formatYear}}<br>
          
          <!-- 상세 정보-->
          <div v-if="hospital.detailedHosInfoId!==null">
            우편 번호: {{hospital.zipCode}}<br>
            종업원 수: {{ hospital.numberHealthcareProvider}}<br>
            병실 수: {{ hospital.numberWard}} <br>
            환자실: {{ hospital.numberPatientRoom}}
          </div>
      </div>

      <div class="hospitalInformation__estimation" v-if="estimations.length===0">
          <h1>병원평가</h1>
          병원 평가가 없습니다.
      </div>

      <div class="hospitalInformation__estimation" v-else>
          <h1>병원평가</h1>
        <span v-for="estimation in estimations" :key="estimation.estimationId"> 
              <b>{{estimation.estimationList}}</b> :{{estimation.distinctionGrade}}<br>
        </span>
      </div>
            
  </section>
</template>

<script>
import {viewHospital} from '@/api/hospital';
import ViewMapForm from '@/components/hospital/ViewMapForm.vue';
import { library } from '@fortawesome/fontawesome-svg-core';
import { faMapMarkerAlt,faMapMarker,faPhoneAlt } from '@fortawesome/free-solid-svg-icons';

library.add(faMapMarkerAlt,faMapMarker,faPhoneAlt);

export default {
    data() {
        return {
            hospital:[],
            licensingDate:'',
            tags:[],

            estimations:[],
            
            //부모에게 전달할 추가 병원 정보 ID
            detailed:{},
            staffHosInfoId:'',
        };
    },
    components:{
        ViewMapForm,
    },
    filters:{
        formatYear(value){
            let Year = value.substring(0,4);
            let Month = value.substring(4,6);
            let Date = value.substring(6,8);
            
            return `${Year}-${Month}-${Date}`; 
        },
    },
    async created(){    
        const id = this.$route.params.id;
        const {data} = await viewHospital(id);

        this.hospital = data;
        this.licensingDate = data.licensingDate;

        //병원 평가
        this.estimations = this.hospital.hospitalEstimations;

        this.detailed = {
            landLotBasedSystem: this.hospital.landLotBasedSystem,
            latitude: this.hospital.latitude,
            longitude: this.hospital.longitude
        };

        //스태프 정보 부모 컴포넌트에 전달
        this.staffHosInfoId = this.hospital.staffHosInfoId;

        //병원 정보 부모 컴포넌트에 전달
        const hospitalInfo = {
            title:this.hospital.hospitalName,
            cityName:this.hospital.cityName,
            businessCondition:this.hospital.businessCondition,
            tags:this.hospital.hospitalTags,
            countReview:this.hospital.hospitalReviewCount,
        }
        
        this.$emit("child-event", this.staffHosInfoId);
        this.$emit("hospitalInfo",hospitalInfo);
    },
}
</script>

<style>
.hospitalInformation__location{
  position:relative;
  text-align:left;
  left:12%;
  width:73%;
  margin-top:10px;
  border-top: 1px solid #dee2e6!important;
  border-bottom: 1px solid #dee2e6!important;
}

.location__info{
    font-size: 14px;
    font-weight: 400;
}

.hospitalInformation__info{
    position:relative;
    text-align:left;
    left:12%;
    width:73%;
    font-size: 16px;
    font-weight: 500;
}

.hospitalInformation__estimation{
    margin-top:10px;
    border-top: 1px solid #dee2e6!important;
    position:relative;
    text-align:left;
    left:12%;
    width:73%;
    font-size: 16px;
    font-weight: 500;
}

</style>