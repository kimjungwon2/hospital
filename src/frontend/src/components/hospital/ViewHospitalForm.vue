<template>
  <section id="hospitalInformation">
      <ViewMapForm v-if= "this.hospital.detailedHosId !==null"
      :detailed="this.detailed"
      >
      </ViewMapForm>
      <section class="hospitalInformation__location">
        <div class="location__info">
            <font-awesome-icon icon="map-marker-alt"/>  (번지) {{ hospital.landLotBasedSystem}}
        </div>
        <div class="location__info">
            <font-awesome-icon icon="map-marker"/>  (도로명) {{ hospital.roadBaseAddress}}
        </div>
        <div class="location__info">
            <font-awesome-icon icon="phone-alt"/>  {{hospital.phoneNumber}}
        </div>
        <div class="location__info">
            <font-awesome-icon icon="mail-bulk"/>  {{hospital.zipCode}}
        </div>
      </section>

      <section class="hospitalInformation__info">
          <h1>병원정보</h1>
          <div class="info__hospital">
              <div class="hospital__class">병원 종류</div>
              <div class="hospital__content">{{hospital.distinguishedName}}</div>
          </div>
         <div class="info__hospital">
              <div class="hospital__class">과목</div>
              <div class="hospital__content">{{hospital.medicalSubjectInformation}}</div>
          </div>
          <div class="info__hospital">
              <div class="hospital__class">개업일</div>
              <div class="hospital__content">{{licensingDate | formatYear}}</div>
          </div>
          <div v-if="hospital.detailedHosInfoId!==null">
                <div class="info__hospital">
                    <div class="hospital__class">종업원 수</div>
                    <div class="hospital__content">{{ hospital.numberHealthcareProvider}}</div>
                </div>
                <div class="info__hospital">
                    <div class="hospital__class">병실 수</div>
                    <div class="hospital__content">{{ hospital.numberWard}}</div>
                </div>
                <div class="info__hospital">
                    <div class="hospital__class">환자실</div>
                    <div class="hospital__content">{{ hospital.numberPatientRoom}}</div>
                </div>
          </div>
      </section>

      <section class="hospitalInformation__estimation" v-if="estimations.length===0">
          <h1>병원평가</h1>
          병원 평가가 없습니다.
      </section>

      <section class="hospitalInformation__estimation" v-else>
        <h1>병원평가</h1>
        <p class="estimation__explanation">
            <font-awesome-icon icon="exclamation-circle" /> 건강보험심사평가원에서 제공하는 경기도 내 병원평가 정보입니다. 
        </p>
        <div class="estimation__grade" v-for="estimation in estimations" :key="estimation.estimationId"> 
              <div class="grade__item" v-if="estimation.estimationList=='주사제'">
                  <div class="item__sort">
                    <font-awesome-icon icon="syringe"/>  {{estimation.estimationList}} 
                    <span class="sort__explanation"><font-awesome-icon icon="question-circle" /></span>
                    <div class="sort__arrox-box">등급 숫자가 작을수록 주사제 처방을 적게하는 의료기관입니다.</div>
                  </div>
                  <div class="item__grade">
                      {{estimation.distinctionGrade}}
                    <span class="sort__explanation"><font-awesome-icon icon="question-circle" /></span>
                        <div class="sort__arrox-box">
                            1등급: 백분위 순위 20이하<br>
                            2등급: 백분위 순위 20초과～40이하<br>
                            3등급: 백분위 순위 40초과～60이하<br>
                            4등급: 백분위 순위 60초과～80이하<br>
                            5등급: 백분위 순위 80초과～100<br>
                            등급제외: 기간내 폐업, 진료월이 6개월미만 등
                        </div>
                  </div>
              </div>
              <div class="grade__item" v-if="estimation.estimationList=='항생제'">
                  <div class="item__sort">
                    <font-awesome-icon icon="tablets"/>   {{estimation.estimationList}}
                    <span class="sort__explanation"><font-awesome-icon icon="question-circle" /></span>
                    <p class="sort__arrox-box">등급 숫자가 작을수록 항생제 처방을 적게 하는 의료기관입니다.</p>
                  </div>
                  <div class="item__grade">
                      {{estimation.distinctionGrade}}
                    <span class="sort__explanation"><font-awesome-icon icon="question-circle" /></span>
                        <div class="sort__arrox-box">
                            1등급: 백분위 순위 20이하<br>
                            2등급: 백분위 순위 20초과～40이하<br>
                            3등급: 백분위 순위 40초과～60이하<br>
                            4등급: 백분위 순위 60초과～80이하<br>
                            5등급: 백분위 순위 80초과～100<br>
                            등급제외: 기간내 폐업, 진료월이 6개월미만 등
                        </div>
                  </div>
              </div>
              <div class="grade__item" v-if="estimation.estimationList=='처방약품비'">
                  <div class="item__sort">
                    <font-awesome-icon icon="hand-holding-medical"/>   {{estimation.estimationList}}
                    <span class="sort__explanation"><font-awesome-icon icon="question-circle" /></span>
                    <p class="sort__arrox-box">등급 숫자가 작을수록 동일평가군 대비 투약일당(환자당)약품비가 낮은 의료기관입니다.</p>
                  </div>
                  <div class="item__grade">
                      {{estimation.distinctionGrade}}
                      <span class="sort__explanation"><font-awesome-icon icon="question-circle" /></span>
                        <div class="sort__arrox-box">
                            1등급: 백분위 순위 20이하<br>
                            2등급: 백분위 순위 20초과～40이하<br>
                            3등급: 백분위 순위 40초과～60이하<br>
                            4등급: 백분위 순위 60초과～80이하<br>
                            5등급: 백분위 순위 80초과～100<br>
                            등급제외: 기간내 폐업, 진료월이 6개월미만 등
                        </div>
                  </div>
              </div>
              <div class="grade__item" v-if="estimation.estimationList=='약품목수'">
                  <div class="item__sort">
                    <font-awesome-icon icon="pills"/>   {{estimation.estimationList}}
                    <span class="sort__explanation"><font-awesome-icon icon="question-circle" /></span>
                    <p class="sort__arrox-box">등급 숫자가 작을수록 처방전당 약품목수가 적은 의료기관입니다.</p>
                  </div>
              <div class="item__grade">
                      {{estimation.distinctionGrade}}
                      <span class="sort__explanation"><font-awesome-icon icon="question-circle" /></span>
                        <div class="sort__arrox-box">
                            1등급: 백분위 순위 20이하<br>
                            2등급: 백분위 순위 20초과～40이하<br>
                            3등급: 백분위 순위 40초과～60이하<br>
                            4등급: 백분위 순위 60초과～80이하<br>
                            5등급: 백분위 순위 80초과～100<br>
                            등급제외: 기간내 폐업, 진료월이 6개월미만 등
                        </div>
                  </div>
              </div>
        </div>

      </section>       
  </section>
</template>

<script>
import {viewHospital} from '@/api/hospital';
import ViewMapForm from '@/components/hospital/ViewMapForm.vue';
import { library } from '@fortawesome/fontawesome-svg-core';
import { faMapMarkerAlt,faMapMarker,faPhoneAlt,
        faSyringe,faTablets,faHandHoldingMedical,faPills,
        faExclamationCircle,faQuestionCircle,faMailBulk } from '@fortawesome/free-solid-svg-icons';

library.add(faMapMarkerAlt,faMapMarker,faPhoneAlt,faSyringe,
        faTablets,faHandHoldingMedical,faPills,faExclamationCircle,faQuestionCircle,faMailBulk);

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

        //병원 상세 정보.
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
            hospitalImages:this.hospital.hospitalImages,
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

.info__hospital{
    margin-left:20px;
    margin-right:20px;
    display:flex;
    border-bottom:1px solid #d3d3d3;
}

.hospital__class{
    width:15%;
    text-align:center;
    padding:5px;
}

.hospital__content{
    margin-left:10px;
    padding:5px;
    width:80%;
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
    margin-bottom:30px;
}

.estimation__explanation{
    text-align:left;
    font-size: 8px;
    color: #797979;
    margin-bottom:10px;
}

.estimation__criteria{
    text-align:right;
    font-size: 8px;
    height: 60px;
    color: #797979;
}

.grade__item{
    display:flex;
}

.sort__explanation{
    position:relative;
    bottom:12px;
    right:7px;
    font-size:8px;
    cursor: pointer;
}

.sort__arrox-box{
    display:none;
    position:relative;
    left:60px;
    bottom:60px;
   -webkit-border-radius: 8px;
   -moz-border-radius: 8px;
   border-radius: 8px;
   background: #005b96;
   color: white;
   font-size: 10px;
   text-align:center;
   width:300px;
}

.sort__explanation:hover + .sort__arrox-box{
    display:block;
}

.item__sort{
    width:130px;
}



</style>