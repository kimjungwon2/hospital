<template>
  <div>병원 보기
    <div>
      <button>리뷰작성{{latitude}}</button>
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
      @child-event="getChild"></ViewHospitalForm>
    </template>
    <template v-if="isDetailed" ><ViewDetailedInfoForm :staffHosInfoId="staffHosInfoId"></ViewDetailedInfoForm></template>
    <template v-if="isReview" ><ViewHospitalReviewForm></ViewHospitalReviewForm></template>
    <template v-if="isQandA" ><ViewQandAForm></ViewQandAForm></template>    
  </div>

</template>

<script>
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
      detailed:{},
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
    getChild(detailed){
      this.detailed = detailed;
    },
  },
  components:{
      ViewHospitalForm,
      ViewDetailedInfoForm,
      ViewHospitalReviewForm,
      ViewQandAForm,
      ViewMapForm,
  },
};
</script>

<style>
</style>