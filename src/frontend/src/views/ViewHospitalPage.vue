<template>
  <div>병원 보기
    <ViewMapForm 
    :landLotBasedSystem="this.landLotBasedSystem"
    :latitude="this.latitude"
    :longitude="this.longitude">
    </ViewMapForm>
    <div>
      <button>리뷰작성</button>
      <button>즐겨찾기</button>
    </div>

    <div>
      <ul role="tablist">
        <li @click.prevent="viewHospital" role="presentation"> 병원 정보 </li>
        <li @click.prevent="viewDetailed" role="presentation"> 상세 정보 </li>
        <li @click.prevent="viewReview" role="presentation"> 리뷰 </li>
        <li @click.prevent="viewQandA" role="presentation"> Q&A </li>
      </ul>
    </div>

    <template v-if="isHospital" >
      <ViewHospitalForm 
      :staffHosInfoId="staffHosInfoId"
      :detailedHosId="detailedHosId"
      :landLotBasedSystem="landLotBasedSystem"
      :latitude="latitude"
      :longitude="longitude"
      @child-event="parents">
      </ViewHospitalForm>
    </template>
    <template v-if="isDetailed" ><ViewDetailedInfoForm :staffHosInfoId="staffHosInfoId"></ViewDetailedInfoForm></template>
    <template v-if="isReview" ><ViewHospitalReviewForm></ViewHospitalReviewForm></template>
    <template v-if="isQandA" ><ViewQandAForm></ViewQandAForm></template>    
  </div>

</template>

<script>
import ViewMapForm from '@/components/hospital/ViewMapForm.vue';
import ViewHospitalForm from '@/components/hospital/ViewHospitalForm.vue';
import ViewDetailedInfoForm from '@/components/hospital/ViewDetailedInfoForm.vue';
import ViewHospitalReviewForm from '@/components/hospital/ViewHospitalReviewForm.vue';
import ViewQandAForm from '@/components/hospital/ViewQandAForm.vue';

export default {
  data() {
    return {
      isHospital : true,
      isDetailed : false,
      isReview: false,
      isQandA: false,

      //자식 컴포넌트로 올라온 값들.
      staffHosInofoId:0,
      landLotBasedSystem:'',
      detailedHosInfo:0,
      latitude:0,
      longitude:0,
    }
  },
  methods:{
    viewHospital(){
      this.isHospital=true;
      this.isDetailed=false;
      this.isReview=false;
      this.isQandA=false;
    },
    viewDetailed(){
      this.isHospital=false;
      this.isDetailed=true;
      this.isReview=false;
      this.isQandA=false;
    },
    viewReview(){
      this.isHospital=false;
      this.isDetailed=false;
      this.isReview=true;
      this.isQandA=false;
    },
    viewQandA(){
      this.isHospital=false;
      this.isDetailed=false;
      this.isReview=false;
      this.isQandA=true;
    },
    parents(detailedHosInfo){
      this.staffHosInfoId = detailedHosInfo.staffHosInfoId;
      this.detailedHosId = detailedHosInfo.detailedHosId;
      this.longitude = detailedHosInfo.longitude;
      this.latitude = detailedHosInfo.latitude;
      this.landLotBasedSystem = detailedHosInfo.landLotBasedSystem;
    },
  },
  components:{
      ViewHospitalForm,
      ViewDetailedInfoForm,
      ViewHospitalReviewForm,
      ViewQandAForm,
      ViewMapForm,
  },
  created(){
      console.log(this.landLotBasedSystem);
  },
};
</script>

<style>
</style>