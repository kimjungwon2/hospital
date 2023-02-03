<template>
  <section id="myReview">
      <div class="myReview__content" v-for="contentItem in contentItems" :key="contentItem.reviewId">
        
        <div @click="routeViewHospital(contentItem)">
            {{ contentItem.reviewHospitals[0].hospitalName }} | 
            <span v-if="contentItem.authenticationStatus !==null">인증 상태: {{contentItem.authenticationStatus}}</span>
            <span v-else>인증 NO</span>
        </div>
        <div>
            질병: {{ contentItem.reviewHospitals[0].disease }} | 등록일: {{contentItem.createdDate|formatDate}}
        </div>
        <div>
            리뷰 내용: {{ contentItem.reviewHospitals[0].content }}
        </div>
        <h4>평가 내역</h4>
        <div>
            추천/비추천: {{ contentItem.reviewHospitals[0].recommendationStatus }}
        </div>
        <div>
            청결: {{ contentItem.reviewHospitals[0].cleanliness }}
        </div>
        <div>
            친절함: {{ contentItem.reviewHospitals[0].kindness }}
        </div>
        <div>
            가격: {{ contentItem.reviewHospitals[0].sumPrice }}
        </div>
        <div>
            증상 완화: {{ contentItem.reviewHospitals[0].symptomRelief }}
        </div>
        <div>
            대기 시간: {{ contentItem.reviewHospitals[0].waitTime }}
        </div>
        <div>
            평균 점수: {{ contentItem.reviewHospitals[0].averageRate }}
        </div>
       </div>
  </section>
</template>

<script>
import {viewUserReviews} from '@/api/user';

export default {
    
    data() {
        return {
            contentItems:[],
        };
    },
    methods:{
        async userReviews(){
            const id = this.$route.params.id;
            const { data } = await viewUserReviews(id);
            this.contentItems = data;
        },
        routeViewHospital(contentItem){
            const id = contentItem.reviewHospitals[0].hospitalId;
            this.$router.push(`/hospital/view/${id}`);
        },
    },
    created(){
        this.userReviews();
    },

};
</script>

<style>
#myReview{
  position:relative;
  text-align:left;
  left:12%;
  width:73%;
}

.myReview__content{
    margin-top:20px;
    display:flex;
    margin-bottom:20px;
    border-bottom: 1px solid #dee2e6!important;
    padding-bottom: 20px;
}

</style>