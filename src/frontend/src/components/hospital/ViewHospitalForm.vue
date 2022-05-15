<template>
  <div>
      <ViewMapForm v-if= "this.hospital.detailedHosId !==null"
      :detailed="this.detailed"
      >
      </ViewMapForm>
      <h1>병원 정보</h1>

            제목: {{hospital.hospitalName}} | 개업일: {{licensingDate | formatYear}} | 영업 상태 : {{hospital.businessCondition}} |
            도시 이름: {{hospital.cityName}}

              <div v-if= "countReview !== 0">
                  리뷰 개수: {{countReview}}
               </div>

               <div v-if= "countTags !== 0">
                 태그: {{hospitalTags}}
               </div>

            <li>전화번호: {{hospital.phoneNumber}}</li>
            <li>병원 종류: {{hospital.distinguishedName}}</li>
            <li>과목: {{hospital.medicalSubjectInformation}}</li>
            

        <div v-if="hospital.detailedHosInfoId!==null">
           <h4>상세 정보</h4>
           <li>종업원 수: {{ hospital.numberHealthcareProvider}}</li>
           <li>병실 수: {{ hospital.numberWard}}</li>
           <li>환자실: {{ hospital.numberPatientRoom}}</li>
            <h4>주소</h4>
           <li>주소(번지): {{ hospital.landLotBasedSystem}}</li>
           <li>도로명 주소: {{ hospital.roadBaseAddress}}</li>
           <li>우편 번호: {{ hospital.zipCode}}</li>
      </div>

    <h2>병원 평가</h2>
      <div v-if="hospitalEstimations.length===0">병원 평가가 없습니다.<br>
      </div>
      <div v-else>
          <span v-for="estimation in estimations" :key="estimation.estimationId"> 
              평가 리스트:<b>{{estimation.estimationList}}</b> 평가 등급:{{estimation.distinctionGrade}}
              <font-awesome-icon icon="trash-alt" @click.prevent="deleteEstimation(estimation.estimationId)"/><br>
          </span>
      </div>

  </div>
</template>

<script>
import {viewHospital} from '@/api/hospital';
import ViewMapForm from '@/components/hospital/ViewMapForm.vue';

export default {
    data() {
        return {
            hospital:[],
            licensingDate:'',

            countReview:0,

            tags:[],
            countTags:0,
            hospitalTags:'',

            hospitalEstimations:[],
            countEstimations:0,
            
            //부모에게 전달할 추가 병원 정보 ID
            detailed:{},
            staffHosInfoId:'',
        };
    },
    components:{
        ViewMapForm,
    },
    methods:{
        createTags(){
            for(let tag in this.tags){
                this.hospitalTags += `#${this.tags[tag].tagName}`;
            }
        },
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

        //List object
        this.countReview = this.hospital.hospitalReviewCount;


        //태그 생성
        this.tags = data.hospitalTags;
        this.countTags = this.tags.length;
        this.createTags();

        this.detailed = {
            landLotBasedSystem: this.hospital.landLotBasedSystem,
            latitude: this.hospital.latitude,
            longitude: this.hospital.longitude
        };

        //스태프 정보 부모 컴포넌트에 전달
        this.staffHosInfoId = this.hospital.staffHosInfoId;
        this.$emit("child-event", this.staffHosInfoId);
    },
}
</script>

<style>

</style>