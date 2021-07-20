<template>
  <div>
      <h1>병원 리뷰</h1>
      <ul v-for="review in reviews" :key ="review.reviewId" >
          <li>
              <div>
                  닉네임:{{review.nickName}} |       인증 상태: {{review.authenticationStatus}}
              </div>
              <div>
                  병명: {{review.reviewHospitals[0].disease}}
              </div>
              <div>
                  내용: {{review.reviewHospitals[0].content}}
              </div>
              <div>
                  좋아요 수: {{review.reviewHospitals[0].likeNumber}}
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
        };
    },
    async created(){
        const id = this.$route.params.id;
        const {data} = await viewHospitalReview(id);
        this.reviews=data;
    }
}
</script>

<style>

</style>