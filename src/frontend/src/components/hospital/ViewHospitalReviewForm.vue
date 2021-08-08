<template>
  <div>
      <h1>병원 리뷰</h1>
      <ul v-for="review in reviews" :key ="review.reviewId" >
          <li>
              <div>
                  닉네임:{{review.nickName}} <span v-if="review.authenticationStatus===null">인증 상태: NONE</span><span v-else>인증 상태: {{review.authenticationStatus}}</span>
              </div>
              <div>
                  병명: {{review.reviewHospitals[0].disease}}
              </div>
              <div>
                  내용: {{review.reviewHospitals[0].content}}
              </div>
              <div>
                  병명: {{review.reviewHospitals[0].sumPrice}}
              </div>
              <div>
                  내용: {{review.reviewHospitals[0].kindness}}
              </div>
              <div>
                  병명: {{review.reviewHospitals[0].symptomRelief}}
              </div>
              <div>
                  내용: {{review.reviewHospitals[0].cleanliness}}
              </div>
             <div>
                  병명: {{review.reviewHospitals[0].waitTime}}
              </div>
              <div>
                  내용: {{review.reviewHospitals[0].averageRate}}
              </div>
              <div>
                  작성일: {{review.createdDate|formatDate}} 
              </div>
          </li>
      </ul>
  </div>
</template>

<script>
import { viewHospitalReview } from '@/api/hospital';

export default {
    data() {
        return {
            reviews:[],
            evaluationCritera:[],
        };
    },
    async created(){
        const id = this.$route.params.id;
        const {data} = await viewHospitalReview(id);
        this.reviews = data;
    }
}
</script>

<style>

</style>