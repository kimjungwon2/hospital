<template>
  <div>
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
      </div>
      <div v-else>상세 정보가 없습니다. 
        <button>(상세정보 추가하기)</button>
      </div>
      <h3>추가 정보</h3>
      <div v-if="hospital.staffHosInfoId!==null">
         추가 정보 번호: {{ hospital.staffHosInfoId}}
      </div>
      <div v-else> 추가 정보가 등록되지 않았습니다.</div>
      <h2>병원 태그</h2>
      <div v-if="hospital.hospitalTags===null">병원 태그가 없습니다.</div>
      <div v-else> </div>
      <h2>병원 리뷰</h2>
      <div v-if="hospital.reviewHospitals===null">병원 리뷰가 없습니다.</div>
      <div v-else> </div>
      <h2>병원 평가</h2>
      <div v-if="hospital.estimations===null">병원 평가가 없습니다.</div>
      <div v-else> </div>

  </div>
  
</template>

<script>
import {adminViewHospital} from '@/api/admin';

export default {

    data() {
        return {
            hospital: [],
        }
    },
    async created(){
        const hospitalId = this.$route.query.hospitalId;
        const detailedHosInfoId = this.$route.query.detailedHosInfoId;
        const staffHosInfoId = this.$route.query.staffHosInfoId;
        const {data} = await adminViewHospital(hospitalId,detailedHosInfoId,staffHosInfoId);
        this.hospital = data;
    },
}
</script>

<style>

</style>