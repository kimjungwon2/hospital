<template>
  <div>
    <div class="staff__hospital__categories">
        <button class="hospital__category__btn"> 병원 정보 </button>
        <button class="hospital__category__btn" @click.prevent="viewStaffHospital"> 추가 병원 정보 </button>
        <button class="hospital__category__btn" @click.prevent="viewThumbnail"> 병원 섬네일 </button>
    </div>
      <StaffModifyHospitalModal :hospitalId="hospital.hospitalId" :hospital="hospital" v-if ="isModifyHospital===true" 
      @hospitalCancel="cancelModifyHospital" @hospitalLoad="staffLoadHospital"/>

      <StaffCreateDetailedHosModal :hospitalId="hospital.hospitalId" v-if="isCreateDetailed==true"
      @createDetailedCancel="cancelCreateDetailed" @detailedHospitalLoad="staffCreateDetailedLoadHospital"/>

      <StaffCreateTagModal :hospitalId="hospital.hospitalId" :hospitalTags="hospitalTags" v-if ="isCreateTags===true" 
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
              <br>
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

            userId:'',
            memberId:'',
            isCreateDetailed:false,
            isCreateTags:false,
            isCreateEstimatons:false,
            isModifyEstimations:false,
            isModifyHospital:false,
        }
    },
    methods:{
        //병원 추가 정보 보기
        viewStaffHospital(){
            this.$router.push({name:'StaffViewHospital',
                query: {hospitalId:this.hospital.hospitalId,
                    staffHosInfoId:this.hospital.staffHosInfoId,
                    thumbnailId:this.hospital.thumbnailId}
            }); 
        },
        //병원 섬네일 보기
        viewThumbnail(){
            this.$router.push({name:'staffThumbnail',
                query: {hospitalId:this.hospital.hospitalId,
                    staffHosInfoId:this.hospital.staffHosInfoId,
                    thumbnailId:this.hospital.thumbnailId}
            }); 
        },
        //상세 정보 삭제
        async deleteDetailedHosInfo(detailedHosInfoId){
            if(confirm('정말로 상세 정보를 삭제하시겠습니까?')){
               await staffDeleteDetailedHosInfo(this.memberId, detailedHosInfoId);
               this.staffDeleteDetailedLoadHospital();
            }
        },
        //태그 삭제
        async deleteTag(postTagId){
            if(confirm('정말로 해당 태그를 삭제하시겠습니까?')){
               await staffDeleteHospitalTag(this.memberId,postTagId);
               this.staffLoadHospital();
            }
        },
        //병원 정보 불러오기(삭제한 상세 정보)
        async staffDeleteDetailedLoadHospital(){
            const {data} = await staffViewHospital(this.userId);
            this.hospital = data;
            this.hospitalTags=data.hospitalTags;
            this.estimations=data.estimations;
        },
        //병원 정보 불러오기(생성한 상세 정보)
        async staffCreateDetailedLoadHospital(detailedHosId){
            const {data} = await staffViewHospital();
            this.hospital = data;
            this.hospitalTags=data.hospitalTags;
            this.estimations=data.estimations;
        },
        //병원 정보 불러오기
        async staffLoadHospital(){
            const {data} = await staffViewHospital();
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
        const {data} = await staffViewHospital();
        this.hospital = data;

        this.memberId = this.$store.getters.getMemberId;
        this.hospitalTags=data.hospitalTags;
        this.estimations=data.estimations;
    },
}
</script>

<style>

</style>