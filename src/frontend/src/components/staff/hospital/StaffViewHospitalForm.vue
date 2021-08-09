<template>
  <div>
      <StaffModifyHospitalModal :hospital="hospital" v-if ="isModifyHospital===true" 
      @hospitalCancel="cancelModifyHospital" @hospitalLoad="staffLoadHospital"/>

      <StaffCreateDetailedHosModal v-if="isCreateDetailed==true"
      @createDetailedCancel="cancelCreateDetailed" @detailedHospitalLoad="staffCreateDetailedLoadHospital"/>

      <StaffCreateTagModal :hospitalTags="hospitalTags" v-if ="isCreateTags===true" 
      @tagCancel="cancelTagModal" @tagLoad="staffLoadHospital"/>

      <br>병원 수정<font-awesome-icon icon="plus-square" @click.prevent="modifyHospital"/>
      <h1>병원 정보</h1>
      <li>병원 번호: {{hospital.hospitalId}}</li>
      <li>병원 이름: {{hospital.hospitalName}}</li>
      <li>개업일: {{hospital.licensingDate}}</li>
      <li>전화번호: {{hospital.phoneNumber}}</li>
      <li>병원 종류: {{hospital.distinguishedName}}</li>
      <li>과목: {{hospital.medicalSubjectInformation}}</li>
      <li>영업 상태: {{hospital.businessCondition}}</li>
      <li>도시 이름: {{hospital.cityName}}</li>
      <h2>상세 정보</h2>
      <div v-if="hospital.detailedHosInfoId!==null">
           <li>상세 정보: {{ hospital.detailedHosInfoId}}</li>
           <li>종업원 수: {{ hospital.numberHealthcareProvider}}</li>
           <li>병실 수: {{ hospital.numberWard}}</li>
           <li>환자실: {{ hospital.numberPatientRoom}}</li>
            <h4>주소</h4>
           <li>주소(번지): {{ hospital.landLotBasedSystem}}</li>
           <li>도로명 주소: {{ hospital.roadBaseAddress}}</li>
           <li>우편 번호: {{ hospital.zipCode}}</li>
           <h4>위치 좌표</h4>
           <li>x 좌표: {{ hospital.x_coordination}}</li>
           <li>y 좌표: {{ hospital.y_coordination}}</li>
           <li>위도: {{ hospital.latitude}}</li>
           <li>경도: {{ hospital.longitude}}</li>
           상세 정보 삭제<font-awesome-icon icon="trash-alt" @click.prevent="deleteDetailedHosInfo(hospital.detailedHosInfoId)"/>
      </div>
      <div v-else>상세 정보가 없습니다. 
        <button @click.prevent="createDetailedHospital">(상세정보 추가하기)</button>
      </div>

      <h2>병원 태그</h2>
      <div v-if="hospitalTags.length===0 || createTags===false">병원 태그가 없습니다.<br> 
      태그 추가<font-awesome-icon icon="plus-square" @click.prevent="createTags"/></div>
      <div v-else> 
          <span v-for="tag in hospitalTags" :key="tag.postTagId"> #<b>{{tag.tagName}}</b> 
          <font-awesome-icon icon="trash-alt" @click.prevent="deleteTag(tag.postTagId)"/>   </span>
          <br>태그 추가<font-awesome-icon icon="plus-square" @click.prevent="createTags"/>
     </div>

      <h2>병원 평가</h2>
      <div v-if="estimations.length===0">등록된 병원 평가가 없습니다.<br>
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
import {staffViewHospital,staffDeleteHospitalTag,staffDeleteDetailedHosInfo} from '@/api/staff';
import StaffCreateTagModal from '@/components/staff/hospital/StaffCreateTagModal.vue';
import StaffModifyHospitalModal from '@/components/staff/hospital/StaffModifyHospitalModal.vue';
import StaffCreateDetailedHosModal from '@/components/staff/hospital/StaffCreateDetailedHosModal.vue';
import { library } from '@fortawesome/fontawesome-svg-core';
import { faTrashAlt } from '@fortawesome/free-solid-svg-icons';
import { faPlusSquare } from '@fortawesome/free-solid-svg-icons';

library.add(faTrashAlt);
library.add(faPlusSquare);
export default {
    components:{
        StaffCreateTagModal,
        StaffModifyHospitalModal,
        StaffCreateDetailedHosModal,
    },
    data() {
        return {
            hospital: [],

            hospitalTags:[],
            estimations:[],

            hospitalId:'',
            isCreateDetailed:false,
            isCreateTags:false,
            isCreateEstimatons:false,
            isModifyEstimations:false,
            isModifyHospital:false,
        }
    },
    methods:{
        //상세 정보 삭제
        async deleteDetailedHosInfo(detailedHosInfoId){
            if(confirm('정말로 상세 정보를 삭제하시겠습니까?')){
               await staffDeleteDetailedHosInfo(detailedHosInfoId);
               this.staffDeleteDetailedLoadHospital();
            }
        },
        //태그 삭제
        async deleteTag(postTagId){
            if(confirm('정말로 해당 태그를 삭제하시겠습니까?')){
               await staffDeleteHospitalTag(postTagId);
               this.staffLoadHospital();
            }
        },
        //병원 정보 불러오기(삭제한 상세 정보)
        async staffDeleteDetailedLoadHospital(){
            const detailedHosInfoId = '';
            const staffHosInfoId = this.$route.query.staffHosInfoId;
            this.$router.push({name:'staffHospitalView',
            query: {hospitalId:this.hospitalId, detailedHosInfoId:detailedHosInfoId, staffHosInfoId:staffHosInfoId}
            }); 
            const {data} = await staffViewHospital(this.hospitalId, detailedHosInfoId, staffHosInfoId);
            this.hospital = data;
        },
        //병원 정보 불러오기(생성한 상세 정보)
        async staffCreateDetailedLoadHospital(detailedHosId){
            const detailedHosInfoId = detailedHosId;
            const staffHosInfoId = this.$route.query.staffHosInfoId;
            this.$router.push({name:'staffHospitalView',
            query: {hospitalId:this.hospitalId, detailedHosInfoId:detailedHosInfoId, staffHosInfoId:staffHosInfoId}
            }); 
            
            const {data} = await staffViewHospital(this.hospitalId, detailedHosInfoId, staffHosInfoId);
            this.hospital = data;
        },
        //병원 정보 불러오기
        async staffLoadHospital(){
            const detailedHosInfoId = this.$route.query.detailedHosInfoId;
            const staffHosInfoId = this.$route.query.staffHosInfoId;
            const {data} = await staffViewHospital(this.hospitalId, detailedHosInfoId, staffHosInfoId);
            this.hospitalTags=data.hospitalTags;
            this.estimations=data.estimations;  
        },
        //모달창.
        modifyHospital(){
            this.isModifyHospital = true;
        },
        cancelModifyHospital(){
            this.isModifyHospital = false;
        },
        //Detailed 정보 생성
        createDetailedHospital(){
            this.isCreateDetailed = true;
        },
        cancelCreateDetailed(){
            this.isCreateDetailed = false;
        },

        //태그 생성
        createTags(){
            this.isCreateTags = true;
        },
        //태그 모달창 닫기
        cancelTagModal(){
            this.isCreateTags= false;
        },

    },
    async created(){
        this.hospitalId = this.$route.query.hospitalId;
        const detailedHosInfoId = this.$route.query.detailedHosInfoId;
        const staffHosInfoId = this.$route.query.staffHosInfoId;
        const {data} = await staffViewHospital(this.hospitalId,detailedHosInfoId,staffHosInfoId);
        this.hospital = data;
        this.hospitalTags=data.hospitalTags;
        this.estimations=data.estimations;

    },
}
</script>

<style>

</style>