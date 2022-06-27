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

    <h1>등록된 영수증</h1>
    <div v-if="imageId===null">
        등록된 영수증이 없습니다.
    </div>
    <div v-else>
        <img :src='`http://d123wf46onsgyf.cloudfront.net/w600/${imageKey}`'/>
    </div>


    <div v-if="status!='CERTIFIED'">
        <font-awesome-icon icon="check-circle" @click.prevent="approveReview(review.reviewId)"/>
    </div>

  </div>
  
</template>

<script>
import { library } from '@fortawesome/fontawesome-svg-core';
import { faCheckCircle } from '@fortawesome/free-solid-svg-icons';
import { adminViewReview, adminApproveReview} from '@/api/admin';

library.add(faCheckCircle)

export default {

    data() {
        return {
            review: [],
            reviewHospital: [],
            evaluationCriteria:[],
            status:'',
            imageId:'',
            imageKey:'',
        }
    },
    methods:{
    //권한 주기
    async approveReview(reviewId){
          if(confirm('영수증 인증을 승인하시겠습니까??')){
              const data = {
                reviewAuthentication: "CERTIFIED",
             };
                await adminApproveReview(reviewId, data);
                this.$store.commit('minusReviewCount',this.count);
                this.loadReview();
          }
    },
    async loadReview(){
        const reviewId = this.$route.params.reviewId;
        const {data} = await adminViewReview(reviewId);

        this.review = data;
        this.imageId = data.imageId;
        this.imageKey = data.imageKey;
        this.reviewHospital = data.reviewHospitals[0];
        this.status = data.authenticationStatus;
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