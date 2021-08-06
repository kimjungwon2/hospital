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
      <h2>병원 태그</h2>
      <div v-if="tags.length===0">병원 태그가 없습니다.</div>
      <div v-else> 
          <span v-for="tag in tags" :key="tag.postTagId">#<b>{{tag.tagName}}</b> 
          <font-awesome-icon icon="trash-alt" @click.prevent="deleteTag(tag.postTagId)"/>   </span>
     </div>
      <h2>병원 리뷰</h2>
      <div v-if="reviews.length===0">병원 리뷰가 없습니다.</div>
      <div v-else> 
          <div v-for="review in reviews" :key="review.reviewId">
              <ul>
                  <li><b>리뷰 번호:{{review.reviewId}}</b></li>
                  <li>리뷰 병원번호:{{review.reviewHospitalId}}</li>
                  <li>리뷰 내용:{{review.content}}</li>
                  <li>질병:{{review.disease}}</li>
                  <li>추천유무:{{review.recommedationStatus}}</li>
                  <li>가격 점수:{{review.sumPrice}}</li>
                  <li>친절함:{{review.kindness}}</li>
                  <li>증상 완화:{{review.symptomRelief}}</li>
                  <li>청결 점수:{{review.cleanliness}}</li>
                  <li>대기시간:{{review.waitTime}}</li>
                  <li>종합 점수:{{review.averageRate}}</li>
              </ul> 
                <font-awesome-icon icon="trash-alt" @click.prevent="deleteReview(review.reviewId)"/>   
          </div>
      </div>
      <h2>병원 평가</h2>
      <div v-if="estimations.length===0">병원 평가가 없습니다.</div>
      <div v-else> </div>

  </div>
  
</template>

<script>
import {adminViewHospital,adminDeleteHospitalTag,adminDeleteReview} from '@/api/admin';
import { library } from '@fortawesome/fontawesome-svg-core';
import { faTrashAlt } from '@fortawesome/free-solid-svg-icons';

library.add(faTrashAlt);
export default {

    data() {
        return {
            hospital: [],
            tags:[],
            reviews:[],
            estimations:[],
            hospitalId:'',
        }
    },
    methods:{
        //태그 삭제
        async deleteTag(postTagId){
            if(confirm('정말로 해당 태그를 삭제하시겠습니까?')){
               await adminDeleteHospitalTag(postTagId);
               this.adminLoadHospital();
            }
        },
        async adminLoadHospital(){
            const detailedHosInfoId = this.$route.query.detailedHosInfoId;
            const staffHosInfoId = this.$route.query.staffHosInfoId;
            const {data} = await adminViewHospital(this.hospitalId, detailedHosInfoId, staffHosInfoId);
            this.tags=data.hospitalTags;
            this.reviews=data.reviewHospitals;
            this.estimations=data.estimations;  
        },
        async deleteReview(reviewId){
            if(confirm('정말로 해당 리뷰를 삭제하시겠습니까?')){
               await adminDeleteReview(reviewId);
               this.adminLoadHospital();
            }
        },
    },
    async created(){
        this.hospitalId = this.$route.query.hospitalId;
        const detailedHosInfoId = this.$route.query.detailedHosInfoId;
        const staffHosInfoId = this.$route.query.staffHosInfoId;
        const {data} = await adminViewHospital(this.hospitalId,detailedHosInfoId,staffHosInfoId);
        this.hospital = data;
        this.tags=data.hospitalTags;
        this.reviews=data.reviewHospitals;
        this.estimations=data.estimations;
    },
}
</script>

<style>

</style>