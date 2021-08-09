<template>
  <div>
    <div>
      <button v-if="!isLogin && !isStaff && !isAdmin" @click.prevent="routeLogin">리뷰 작성</button>
      <button v-else @click.prevent="routeRegisterReview">리뷰 작성</button>
      <font-awesome-icon v-if="!isLogin && !isStaff && !isAdmin " :icon="['far', 'heart']" @click.prevent="routeLogin"/>
      <font-awesome-icon v-else-if="bookmark===false" :icon="['far', 'heart']" @click.prevent="registerBookmark"/>
      <font-awesome-icon v-else-if="bookmark===true" :icon="['fas', 'heart']" @click.prevent="registerBookmark"/>
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
    <template v-if="isQandA" ><ViewQandAForm :staffHosInfoId="staffHosInfoId"></ViewQandAForm></template>    
  </div>

</template>

<script>
import ViewHospitalForm from '@/components/hospital/ViewHospitalForm.vue';
import ViewDetailedInfoForm from '@/components/hospital/ViewDetailedInfoForm.vue';
import ViewHospitalReviewForm from '@/components/hospital/ViewHospitalReviewForm.vue';
import ViewQandAForm from '@/components/hospital/ViewQandAForm.vue';
import { library } from '@fortawesome/fontawesome-svg-core';
import { faHeart as faSolideHeart  } from '@fortawesome/free-solid-svg-icons';
import { faHeart as faRegularHeart } from '@fortawesome/free-regular-svg-icons';
import {isUserBookmark, userRegisterBookmark} from '@/api/user';

library.add(faSolideHeart, faRegularHeart);

export default {
  components:{
      ViewHospitalForm,
      ViewDetailedInfoForm,
      ViewHospitalReviewForm,
      ViewQandAForm,
  },
  data() {
    return {
      hospitalId:'',
      isHospital : true,
      isDetailed : false,
      isReview: false,
      isQandA: false,
      bookmark: '',
      //자식 컴포넌트로 올라온 값들.
      detailed:{},
      staffHosInfoId:'',
    }
  },
  computed:{
    isLogin(){
      return this.$store.getters.isLogin;
    },
    isStaff(){
      return this.$store.getters.isStaff;
    },
    isAdmin(){
      return this.$store.getters.isAdmin;
    },
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
    async loadBookmark(){
      const memberId= this.$store.getters.getMemberId;
      const hospitalId = this.hospitalId;
      const isBookmark = await isUserBookmark(memberId,hospitalId);
      this.bookmark =isBookmark.data.isBookmark;
    },
    //북마크 등록.
    async registerBookmark(){
      const data={
        memberId:this.$store.getters.getMemberId,
        hospitalId:this.$route.params.id
      };
      await userRegisterBookmark(data);
      this.loadBookmark();
    },
    routeLogin(){
      this.$alert('로그인이 필요한 서비스입니다.');
      this.$router.push('/login');
    },
    routeRegisterReview(){
      const hospitalId=this.hospitalId;
      this.$router.push('/user/hospital/'+hospitalId+'/register/review');
    },
    getChild(staffHosInfoId){
      this.staffHosInfoId = staffHosInfoId;
    },
  },
  async created(){
    this.hospitalId = this.$route.params.id;

    if(this.isLogin || this.isStaff ||this.isAdmin){
      this.loadBookmark();
    }
  },
}
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