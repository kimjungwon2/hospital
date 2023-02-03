<template>
  <section id="reviewContents" v-if="reviews.length!==0">
      <div class="reviewContents__content" v-for="(review, i) in reviews" :key ="review.reviewId">
          <div class="content__leftBox">
              <div class="leftBox__nickName">
                닉네임: {{review.nickName}}
              </div>
              <div class="leftBox__sumRating">
                 <span class="sumRating__score"><img src='@/assets/star-resize.png' alt="rating-star" class="score__star">
                    <span class="score__acquire">{{review.reviewHospitals[0].averageRate}}</span>
                    <span class="score__total"> / 10 </span>
                 </span> 
                 <div class="sumRating__status" v-if="review.reviewHospitals[0].recommendationStatus=='RECOMMENDATION'">
                     <img src='@/assets/smile.png' alt="smile" class="status__smile">
                 </div>
                 <div class="sumRating__status" v-if="review.reviewHospitals[0].recommendationStatus=='DECOMMENDATION'">
                     <img src='@/assets/angry.png' alt="angry" class="status__angry">
                 </div>
              </div>
              <div class="leftBox__rating">
                <div class="rating__item">
                    <div class="item__title">가격</div>
                    <div class="item__number">
                        <span v-if="review.reviewHospitals[0].sumPrice==10">
                            <img v-for="count in 5" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].sumPrice==9">
                            <img v-for="count in 4" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].sumPrice==8">
                            <img v-for="count in 4" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].sumPrice==7">
                            <img v-for="count in 3" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].sumPrice==6">
                            <img v-for="count in 3" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 2" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].sumPrice==5">
                            <img v-for="count in 2" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 2" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].sumPrice==4">
                            <img v-for="count in 2" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 3" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].sumPrice==3">
                            <img src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 3" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].sumPrice==2">
                            <img src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 4" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].sumPrice==1">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 4" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                    </div>
                </div>
                <div class="rating__item">
                    <div class="item__title">친절함</div>
                    <div class="item__number">
                    <span v-if="review.reviewHospitals[0].kindness==10">
                            <img v-for="count in 5" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].kindness==9">
                            <img v-for="count in 4" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].kindness==8">
                            <img v-for="count in 4" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].kindness==7">
                            <img v-for="count in 3" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].kindness==6">
                            <img v-for="count in 3" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 2" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].kindness==5">
                            <img v-for="count in 2" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 2" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].kindness==4">
                            <img v-for="count in 2" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 3" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].kindness==3">
                            <img src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 3" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].kindness==2">
                            <img src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 4" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].kindness==1">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 4" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                    </div>
                </div>
                <div class="rating__item">
                    <div class="item__title">증상완화</div>
                    <div class="item__number">
                        <span v-if="review.reviewHospitals[0].symptomRelief==10">
                            <img v-for="count in 5" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].symptomRelief==9">
                            <img v-for="count in 4" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].symptomRelief==8">
                            <img v-for="count in 4" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].symptomRelief==7">
                            <img v-for="count in 3" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].symptomRelief==6">
                            <img v-for="count in 3" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 2" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].symptomRelief==5">
                            <img v-for="count in 2" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 2" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].symptomRelief==4">
                            <img v-for="count in 2" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 3" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].symptomRelief==3">
                            <img src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 3" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].symptomRelief==2">
                            <img src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 4" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].symptomRelief==1">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 4" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                    </div>
                </div>
                <div class="rating__item">
                    <div class="item__title">청결</div>
                    <div class="item__number">
                        <span v-if="review.reviewHospitals[0].cleanliness==10">
                            <img v-for="count in 5" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].cleanliness==9">
                            <img v-for="count in 4" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].cleanliness==8">
                            <img v-for="count in 4" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].cleanliness==7">
                            <img v-for="count in 3" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].cleanliness==6">
                            <img v-for="count in 3" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 2" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].cleanliness==5">
                            <img v-for="count in 2" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 2" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].cleanliness==4">
                            <img v-for="count in 2" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 3" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].cleanliness==3">
                            <img src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 3" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].cleanliness==2">
                            <img src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 4" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].cleanliness==1">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 4" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                    </div>
                </div>
                <div class="rating__item">
                    <div class="item__title">대기시간</div>
                    <div class="item__number">
                        <span v-if="review.reviewHospitals[0].waitTime==10">
                            <img v-for="count in 5" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].waitTime==9">
                            <img v-for="count in 4" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].waitTime==8">
                            <img v-for="count in 4" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].waitTime==7">
                            <img v-for="count in 3" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].waitTime==6">
                            <img v-for="count in 3" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 2" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].waitTime==5">
                            <img v-for="count in 2" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 2" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].waitTime==4">
                            <img v-for="count in 2" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 3" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].waitTime==3">
                            <img src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 3" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].waitTime==2">
                            <img src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 4" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="review.reviewHospitals[0].waitTime==1">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 4" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                    </div>
                </div>
              </div>
          </div>

          <div class="content__rightBox">
              <div class="rightBox__topContent">
                <span class="topContent__Noreceipt" v-if="(review.authenticationStatus=='NONE') || (review.authenticationStatus===null)">
                    미인증 리뷰
                </span>
                <span class="topContent__Noreceipt" v-else-if="review.authenticationStatus=='WAITING'">
                    인증 대기
                </span>
                <span class="topContent__receipt" v-else-if="review.authenticationStatus=='CERTIFIED'">
                    영수증 인증
                </span>

                <div class="topContent__date">작성일: {{review.createdDate|formatDate}}</div>
              </div>

              <div class="rightBox__title">
                  <span class="title__disease">받은 치료: {{review.reviewHospitals[0].disease}}</span>
                  <div class="title__like">
                    <font-awesome-icon v-if="!isLogin && !isStaff &&!isAdmin" :icon="['far', 'heart']" @click.prevent="routeLogin"/>
                    <font-awesome-icon v-else-if="reviewLike[i]===false" :icon="['far', 'heart']" @click.prevent="like(review.reviewId,i)"/>
                    <font-awesome-icon v-else-if="reviewLike[i]===true" :icon="['fas', 'heart']" @click.prevent="like(review.reviewId,i)"/>
                    {{review.reviewLikes.length}}
                  </div>  
              </div>

              <div class="rightBox__content">
                  {{review.reviewHospitals[0].content}}
              </div>

          </div>
      </div>
  </section>
  <section v-else>
      <h1> 등록된 리뷰가 없습니다. </h1>
  </section>
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
        isStaff(){
          return this.$store.getters.isStaff;
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

        if(this.isLogin || this.isStaff || this.isAdmin){
            await this.loadReviewAllLike(this.reviews.length);
        }
    }
}
</script>

<style>
#reviewContents{
  position:relative;
  text-align:left;
  left:12%;
  width:73%;
}

.reviewContents__content{
    margin-top:20px;
    display:flex;
    margin-bottom:20px;
    border-bottom: 1px solid #dee2e6!important;
    padding-bottom: 20px;
}

.leftBox__nickName{
    text-align:center;
    letter-spacing: -0.9px;
    color: #000000;  
    font-size: 14px;
    font-weight: bold;
    padding-bottom: 3px;
    border-bottom: 1px solid #dee2e6!important;
}

.leftBox__sumRating{
    display:flex;
    padding-bottom: 3px;
    border-bottom: 1px solid #dee2e6!important;
    width:100%;
    margin:auto;
}

.sumrating__score{
    width:50%;
}

.sumRating__status{
    text-align:right;
    width:50%;
}

.score__acquire{
    font-size: 18px;
    font-weight: bold;
    letter-spacing: -0.9px;
    color: #1d458d;
}

.score__total{
    letter-spacing: -0.7px;
    color: #9b9b9b;
    font-size: 14px;
}

.rating__item{
    padding-top:5px;
    display:flex;
    width:100%;
    border-bottom: 1px solid #dee2e6!important;
}

.item__title{
    width:50%;
}

.item__number{
    text-align:right;
    width:50%;
}

.rightBox__topContent{
    display:inline-block;
    width:100%;
}

.rightBox__title{
    display:inline-block;
    width:100%;
    margin-bottom:2%;
}

.title__disease{
    font-weight: bold;
    letter-spacing: -0.8px;
}

.rightBox__contet{
    font-size: 14px;
    font-weight: normal;
    letter-spacing: -0.6px;
}

.content__leftBox{
    width:25%;
    margin-right:10px;
}

.content__rightBox{
    width:75%;
}

.topContent__receipt{
    text-align:left;
    background-color: #1d458d; 
    color:#FFFFFF;
    font-size: 12px; 
    padding-top:2px; 
    padding-bottom: 1px;
    border-radius:4px;
}

.topContent__Noreceipt{
    text-align:left;
    border: 2px solid #1d458d; 
    font-size: 12px; 
    padding-top:2px; 
    padding-bottom: 1px;
    border-radius:4px;
}

.topContent__date{
    float:right;
    font-size: 11px;
    color: #9b9b9b;
}

.title__like{
    float:right;
}
</style>