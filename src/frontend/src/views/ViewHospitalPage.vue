<template>
  <!-- 이미지 부분-->
  <section id="viewHospital">
    <section class="viewHospital__images" v-if="hospitalImages.length==0">
    </section>
    <section class="viewHospital__images" v-else>
        <hooper :autoPlay="true" :playSpeed="2500" class="images_array">
            <slide class ="slide" v-for="image in hospitalImages" :key="image.hospitalImageId">
                  <img alt="hospitalImage" class="image__hospital" 
                            :src='`http://d123wf46onsgyf.cloudfront.net/w600/${image.imageKey}`'/>
            </slide>
            <hooper-pagination slot="hooper-addons"></hooper-pagination>
        </hooper>
    </section>

    <section class="viewHospital__title">
      <p class="title__city">{{cityName}}</p>
      <h1 class="title__hospital">{{hospitalTitle}} ({{businessCondition}})</h1>
    </section>

    <!-- 태그 -->
    <section class="viewHospital__tagBox" v-if = "tags.length==0">
      <div class="tagBox__notag">
        등록된 태그가 없습니다.
      </div>
    </section>
    <section class="viewHospital__tagBox" v-else >
      <div class="tagBox__tagName" v-for="tag in tags" :key="tag.tagName">
        #{{tag.tagName}}
      </div>
    </section>

    <div class="viewHospital__functionBox">
      <button class="functionBox__writeReview" v-if="!isLogin && !isStaff && !isAdmin" @click.prevent="routeLogin"><font-awesome-icon icon="pen"/> 리뷰 작성</button>
      <button class="functionBox__writeReview" v-else @click.prevent="routeRegisterReview">
        <font-awesome-icon icon="pen"/> 리뷰 작성
      </button>

      <button class="functionBox__bookmark" v-if="!isLogin && !isStaff && !isAdmin " @click.prevent="routeLogin">
        <font-awesome-icon :icon="['far', 'heart']" /> 즐겨찾기
      </button>
      <button class="functionBox__bookmark" v-else-if="bookmark===false" @click.prevent="registerBookmark">
        <font-awesome-icon :icon="['far', 'heart']" /> 즐겨찾기
      </button>
      <button class="functionBox__bookmark" v-else-if="bookmark===true" @click.prevent="registerBookmark">
        <font-awesome-icon :icon="['fas', 'heart']" /> 즐겨찾기
      </button>
    
    </div>

    <div class="viewHospital__categories">
        <button class="category__btn" @click.prevent="viewHospital"> 병원 정보 </button>
        <button class="category__btn" @click.prevent="viewDetailed" > 상세 정보 </button>
        <button class="category__btn" @click.prevent="viewReview" > 리뷰 
          <!-- 리뷰 카운트 -->
          <span v-if="countReview<=100" class="btn__reviewCount">{{countReview}}</span> 
          <span v-else class="btn__reviewOverCount"> {{countReview}} </span>
        </button>
        <button class="category__btn" @click.prevent="viewQandA" > Q&A </button>
    </div>

    <template v-if="isHospital" >
      <ViewHospitalForm 
      @child-event="getChild" @hospitalInfo="getHospitalInfo"></ViewHospitalForm>
    </template>
    <template v-if="isDetailed" ><ViewDetailedInfoForm :staffHosInfoId="staffHosInfoId"></ViewDetailedInfoForm></template>
    <template v-if="isReview" ><ViewHospitalReviewForm></ViewHospitalReviewForm></template>
    <template v-if="isQandA" ><ViewQandAForm :staffHosInfoId="staffHosInfoId"></ViewQandAForm></template>    
  </section>

</template>

<script>
import ViewHospitalForm from '@/components/hospital/ViewHospitalForm.vue';
import ViewDetailedInfoForm from '@/components/hospital/ViewDetailedInfoForm.vue';
import ViewHospitalReviewForm from '@/components/hospital/ViewHospitalReviewForm.vue';
import ViewQandAForm from '@/components/hospital/ViewQandAForm.vue';

import {Hooper,Slide,Pagination as HooperPagination} from 'hooper';
import 'hooper/dist/hooper.css';

import { library } from '@fortawesome/fontawesome-svg-core';
import { faPen, faHeart as faSolideHeart  } from '@fortawesome/free-solid-svg-icons';
import { faHeart as faRegularHeart } from '@fortawesome/free-regular-svg-icons';
import {isUserBookmark, userRegisterBookmark} from '@/api/user';

library.add(faSolideHeart, faRegularHeart,faPen);

export default {
  components:{
      ViewHospitalForm,
      ViewDetailedInfoForm,
      ViewHospitalReviewForm,
      ViewQandAForm,
      Hooper,Slide,HooperPagination
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

      //병원 정보
      hospitalTitle:'',
      countReview:'',
      cityName:'',
      businessCondition:'',
      tags:'',
      hospitalImages:'',
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
    getHospitalInfo(hospitalInfo){
      this.hospitalTitle = hospitalInfo.title;
      this.cityName = hospitalInfo.cityName;
      this.businessCondition =hospitalInfo.businessCondition;
      this.tags = hospitalInfo.tags;
      this.countReview = hospitalInfo.countReview;
      this.hospitalImages = hospitalInfo.hospitalImages;
    }
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
#viewHospital{
  text-align:center;
}

.viewHospital__images{
  margin-top:20px;
}

.viewHospital__images .image__hospital{
    width:600px;
    height:200px;
}

.viewHospital__images .images_array{
    text-align:center;
}

.viewHospital__title{
  position:relative;
  text-align:left;
  left:12%;
  width:73%;
}

.title__city{
  color: #1d458d;
  font-weight: 600;
  text-decoration: underline;
  margin-bottom:0;
}
.title__hospital{
  position:relative;
  right:4px;
  margin:1px;
}

.viewHospital__tagBox{
  margin-top:10px;
  position:relative;
  text-align:left;
  left:12%;
  width:500px;
  border: 2px solid #0067A3;
}

.tagBox__notag{
  text-align:center;
  font-size: 16px; 
  letter-spacing: -0.4px;
}

.tagBox__tagName{
  font-size: 14px; 
  letter-spacing: -0.4px;
  background-color:rgba(233, 233, 233, 0.55); 
  display: inline-block;
  margin-right:5px;
}

.viewHospital__functionBox{
  margin-top:10px;
  position:relative;
  text-align:left;
  left:12%;
  display:flex;
  width:500px;
}

.functionBox__writeReview{
  background-color:#005b96; 
  color: white; 
  font-size: 15px; 
  height: 32px; 
  line-height: 32px;
  margin-right:10px;
  border-radius:10px;
}

.functionBox__bookmark{
  background-color:white; 
  border:2px solid #D3D3D3;
  font-size: 15px; 
  height: 32px; 
  line-height: 32px;
  margin-right:10px;
  border-radius:10px;
}

.viewHospital__categories{
  margin-left:12%;
  margin-top:1%;
  margin-bottom:1%;
  position: relative;
  text-align: left;
}

.category__btn{
  border-top: 1px solid #dee2e6!important;
  border-bottom: 1px solid #dee2e6!important;
  margin:5px;
  width: 20%;
  font-size:14px;
  border-radius: 4px;
  padding: 8px 48px;
  size:35px;
  height:50px;
}

.category__btn.active,
.category__btn:hover{
  background-color:#006ab0;
  color:white;
}

.btn__reviewCount{
  background-color: orange;
  border-radius: 50%;
  color:white;
  width:20px;
  height:20px;
  line-height:20px;
  display:inline-block;
  position:relative;
  top:-20%;
  left: 4px;
}

.btn__reviewOverCount{
  background-color: orange;
  border-radius: 30%;
  color:white;
  height:20px;
  line-height:20px;
  display:inline-block;
  position:relative;
  top:-20%;
  left: 4px;
}

</style>