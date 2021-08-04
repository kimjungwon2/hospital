<template>
  <div>
    <div>
      <button>리뷰작성{{staffHosInfoId}}</button>
      <button>즐겨찾기</button>
    </div>

    <div class="hospital__categories">
        <button class="category__btn" @click.prevent="viewHospital"> 병원 정보 </button>
        <button class="category__btn" @click.prevent="viewDetailed" > 상세 정보 </button>
        <button class="category__btn" @click.prevent="viewReview" > 리뷰 </button>
        <button class="category__btn" @click.prevent="viewQandA" > Q&A </button>
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
      staffHosInfoId:'',
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
    getChild(staffHosInfoId){
      this.staffHosInfoId = staffHosInfoId;
    },
  },
  components:{
      ViewHospitalForm,
      ViewDetailedInfoForm,
      ViewHospitalReviewForm,
      ViewQandAForm,
  },
};
</script>

<style>
.hospital__categories{
  margin : 50px;
  position: relative;
  text-align: center;
}

.category__btn{
  width: 20%;
  border: 2px solid black;
  font-size:14px;
  border-radius: 4px;
  padding: 8px 48px;
  size:35px;
}

.category__btn.active,
.category__btn:hover{
  background-color:bisque;
}

</style>