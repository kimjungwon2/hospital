<template>
  <div>
      <h1>병원 리뷰</h1>
      <ul v-for="(review, i) in reviews" :key ="review.reviewId" >
          <li>
              <div>리뷰번호: {{i+1}}</div>
              <div>
                  닉네임:{{review.nickName}} <span v-if="review.authenticationStatus===null">인증 상태: NONE</span><span v-else>인증 상태: {{review.authenticationStatus}}</span>
              </div>
              <div>
                  작성일: {{review.createdDate|formatDate}} 
              </div>
              <div>
                    <font-awesome-icon v-if="!isLogin && !isAdmin" :icon="['far', 'heart']" @click.prevent="routeLogin"/>
                    <font-awesome-icon v-else-if="reviewLike[i]===false" :icon="['far', 'heart']" @click.prevent="like(review.reviewId,i)"/>
                    <font-awesome-icon v-else-if="reviewLike[i]===true" :icon="['fas', 'heart']" @click.prevent="like(review.reviewId,i)"/>
                    좋아요 수: {{review.reviewLikes.length}}
              </div>
              <div>
                  병명: {{review.reviewHospitals[0].disease}}
              </div>
              <div>
                  내용: {{review.reviewHospitals[0].content}}
              </div>
              <div>
                  가격: {{review.reviewHospitals[0].sumPrice}}
              </div>
              <div>
                  친절함: {{review.reviewHospitals[0].kindness}}
              </div>
              <div>
                  증상완화: {{review.reviewHospitals[0].symptomRelief}}
              </div>
              <div>
                  청결: {{review.reviewHospitals[0].cleanliness}}
              </div>
             <div>
                  대기 시간: {{review.reviewHospitals[0].waitTime}}
              </div>
              <div>
                  종합 점수: {{review.reviewHospitals[0].averageRate}}
              </div>
          </li>
      </ul>
  </div>
</template>

<script>
import { viewHospitalReview} from '@/api/hospital';
import { isLikeReview, likeReview } from '@/api/user';
import { library } from '@fortawesome/fontawesome-svg-core';
import { faHeart as faSolideHeart  } from '@fortawesome/free-solid-svg-icons';
import { faHeart as faRegularHeart } from '@fortawesome/free-regular-svg-icons';

library.add(faSolideHeart, faRegularHeart);

export default {
    data() {
        return {
            reviews:[],
            evaluationCritera:[],
            reviewLike:[],
        };
    },
    computed:{
        isLogin(){
          return this.$store.getters.isLogin;
        },
        isAdmin(){
          return this.$store.getters.isAdmin;
        },
    },
    watch:{

    },
    methods:{
        routeLogin(){
          this.$alert('로그인이 필요한 서비스입니다.');
          this.$router.push('/login');
        },
        async like(reviewId,i){
            const data={
              memberId:this.$store.getters.getMemberId,
              reviewId:reviewId
            };
            await likeReview(data);
            this.loadReviewLike(reviewId,i);
        },
        //리뷰 좋아요 여부 확인
        async loadReviewLike(reviewId, i){
            const memberId= this.$store.getters.getMemberId;
            const isReviewLike = await isLikeReview(memberId, reviewId);
            this.$set(this.reviewLike, i, isReviewLike.data.isReviewLike);

            //값 반영.
            const id = this.$route.params.id;
            const {data} = await viewHospitalReview(id);
            this.reviews = data;
        },

        //리뷰 좋아요 모두 확인
        async loadReviewAllLike(length){
            const memberId= this.$store.getters.getMemberId;

            for(let i=0; i<length; i++){
                const isReviewLike = await isLikeReview(memberId, this.reviews[i].reviewId);
                this.$set(this.reviewLike, i, isReviewLike.data.isReviewLike);
            }
        },
    },
    async created(){
        const id = this.$route.params.id;
        const {data} = await viewHospitalReview(id);
        this.reviews = data;

        if(this.isLogin || this.isAdmin){
            await this.loadReviewAllLike(this.reviews.length);
        }
    }
}
</script>

<style>

</style>