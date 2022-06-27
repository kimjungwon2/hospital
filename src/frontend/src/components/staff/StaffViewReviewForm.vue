<template>
  <div>
    번호 : {{review.reviewId}}<br>
    인증상태 : {{review.authenticationStatus}}<br>
    생성 날짜 : {{review.createdDate|formatDate}}<br>
    닉네임 : {{review.nickName}}<br>
    병원 이름 : {{reviewHospital.hospitalName}}<br>
    진료 과목 : {{reviewHospital.medicalSubjectInformation}}
    <div>
        <h1>리뷰 정보</h1>
        내용 : {{reviewHospital.content}}
        병명 : {{reviewHospital.disease}}
        추천 상태 : {{reviewHospital.recommendationStatus}}
        <h1>평가 점수</h1>
        가격 : {{evaluationCriteria.sumPrice}}
        친절함 : {{evaluationCriteria.kindness}}
        증상 완화 : {{evaluationCriteria.symptomRelief}}
        청결 : {{evaluationCriteria.cleanliness}}
        대기 시간 : {{evaluationCriteria.waitTime}}
        <h1>평균 점수</h1>
        평균 점수: {{evaluationCriteria.averageRate}}
    </div>
  </div>
  
</template>

<script>
import {staffViewReview } from '@/api/staff';

export default {
    data() {
        return {
            review: [],
            reviewHospital: [],
            evaluationCriteria:[],
        }
    },
    methods:{
    async loadReview(){
        const reviewId = this.$route.params.reviewId;
        const {data} = await staffViewReview(reviewId);
        this.review = data;
        this.reviewHospital = data.reviewHospitals[0];
        this.evaluationCriteria = this.reviewHospital.evaluationCriteria;
    }
    },
    async created(){
        this.loadReview();
    },
}
</script>

<style>

</style>