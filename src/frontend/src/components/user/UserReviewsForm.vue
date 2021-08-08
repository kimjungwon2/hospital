<template>
  <div>
      <ul v-for="contentItem in contentItems" :key="contentItem.reviewId">
        <li @click="routeViewHospital(contentItem)">
            {{ contentItem.reviewHospitals[0].hospitalName }} | 
            <span v-if="contentItem.authenticationStatus !==null">인증 상태: {{contentItem.authenticationStatus}}</span>
            <span v-else>인증 NO</span>
        </li>
        <li>
            질병: {{ contentItem.reviewHospitals[0].disease }} | 등록일: {{contentItem.createdDate|formatDate}}
        </li>
        <li>
            리뷰 내용: {{ contentItem.reviewHospitals[0].content }}
        </li>
        <h4>평가 내역</h4>
        <li>
            추천/비추천: {{ contentItem.reviewHospitals[0].recommendationStatus }}
        </li>
        <li>
            청결: {{ contentItem.reviewHospitals[0].cleanliness }}
        </li>
        <li>
            친절함: {{ contentItem.reviewHospitals[0].kindness }}
        </li>
        <li>
            가격: {{ contentItem.reviewHospitals[0].sumPrice }}
        </li>
        <li>
            증상 완화: {{ contentItem.reviewHospitals[0].symptomRelief }}
        </li>
        <li>
            대기 시간: {{ contentItem.reviewHospitals[0].waitTime }}
        </li>
        <li>
            평균 점수: {{ contentItem.reviewHospitals[0].averageRate }}
        </li>
       </ul>
  </div>
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

</style>