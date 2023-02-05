<template>
  <section id="staffViewReviewItem">
    <div class="staffViewReviewItem__title">
        <h1>병원: {{reviewHospital.hospitalName}}</h1>
        <h4>{{review.reviewId}}번 리뷰</h4><br><br>
    </div>
    <div class="myReview__content">
          <div class="content__leftBox">
              <div class="leftBox__hospitalName">
                닉네임 : {{review.nickName}}
              </div>
              <div class="leftBox__sumRating">
                 <span class="sumRating__score"><img src='@/assets/star-resize.png' alt="rating-star" class="score__star">
                    <span class="score__acquire">{{evaluationCriteria.averageRate}}</span>
                    <span class="score__total"> / 10 </span>
                 </span> 
                 <div class="sumRating__status" v-if="reviewHospital.recommendationStatus=='RECOMMENDATION'">
                     <img src='@/assets/smile.png' alt="smile" class="status__smile">
                 </div>
                 <div class="sumRating__status" v-if="reviewHospital.recommendationStatus=='DECOMMENDATION'">
                     <img src='@/assets/angry.png' alt="angry" class="status__angry">
                 </div>
              </div>
              <div class="leftBox__rating">
                <div class="rating__item">
                    <div class="item__title">가격</div>
                    <div class="item__number">
                        <span v-if="evaluationCriteria.sumPrice==10">
                            <img v-for="count in 5" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                        </span>
                        <span v-else-if="evaluationCriteria.sumPrice==9">
                            <img v-for="count in 4" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                        </span>
                        <span v-else-if="evaluationCriteria.sumPrice==8">
                            <img v-for="count in 4" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="evaluationCriteria.sumPrice==7">
                            <img v-for="count in 3" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="evaluationCriteria.sumPrice==6">
                            <img v-for="count in 3" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 2" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="evaluationCriteria.sumPrice==5">
                            <img v-for="count in 2" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 2" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="evaluationCriteria.sumPrice==4">
                            <img v-for="count in 2" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 3" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="evaluationCriteria.sumPrice==3">
                            <img src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 3" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="evaluationCriteria.sumPrice==2">
                            <img src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 4" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="evaluationCriteria.sumPrice==1">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 4" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                    </div>
                </div>
                <div class="rating__item">
                    <div class="item__title">친절함</div>
                    <div class="item__number">
                    <span v-if="evaluationCriteria.kindness==10">
                            <img v-for="count in 5" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                        </span>
                        <span v-else-if="evaluationCriteria.kindness==9">
                            <img v-for="count in 4" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                        </span>
                        <span v-else-if="evaluationCriteria.kindness==8">
                            <img v-for="count in 4" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="evaluationCriteria.kindness==7">
                            <img v-for="count in 3" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="evaluationCriteria.kindness==6">
                            <img v-for="count in 3" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 2" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="evaluationCriteria.kindness==5">
                            <img v-for="count in 2" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 2" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="evaluationCriteria.kindness==4">
                            <img v-for="count in 2" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 3" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="evaluationCriteria.kindness==3">
                            <img src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 3" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="evaluationCriteria.kindness==2">
                            <img src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 4" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="evaluationCriteria.kindness==1">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 4" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                    </div>
                </div>
                <div class="rating__item">
                    <div class="item__title">증상완화</div>
                    <div class="item__number">
                        <span v-if="evaluationCriteria.symptomRelief==10">
                            <img v-for="count in 5" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                        </span>
                        <span v-else-if="evaluationCriteria.symptomRelief==9">
                            <img v-for="count in 4" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                        </span>
                        <span v-else-if="evaluationCriteria.symptomRelief==8">
                            <img v-for="count in 4" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="evaluationCriteria.symptomRelief==7">
                            <img v-for="count in 3" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="evaluationCriteria.symptomRelief==6">
                            <img v-for="count in 3" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 2" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="evaluationCriteria.symptomRelief==5">
                            <img v-for="count in 2" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 2" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="evaluationCriteria.symptomRelief==4">
                            <img v-for="count in 2" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 3" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="evaluationCriteria.symptomRelief==3">
                            <img src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 3" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="evaluationCriteria.symptomRelief==2">
                            <img src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 4" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="evaluationCriteria.symptomRelief==1">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 4" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                    </div>
                </div>
                <div class="rating__item">
                    <div class="item__title">청결</div>
                    <div class="item__number">
                        <span v-if="evaluationCriteria.cleanliness==10">
                            <img v-for="count in 5" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                        </span>
                        <span v-else-if="evaluationCriteria.cleanliness==9">
                            <img v-for="count in 4" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                        </span>
                        <span v-else-if="evaluationCriteria.cleanliness==8">
                            <img v-for="count in 4" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="evaluationCriteria.cleanliness==7">
                            <img v-for="count in 3" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="evaluationCriteria.cleanliness==6">
                            <img v-for="count in 3" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 2" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="evaluationCriteria.cleanliness==5">
                            <img v-for="count in 2" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 2" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="evaluationCriteria.cleanliness==4">
                            <img v-for="count in 2" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 3" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="evaluationCriteria.cleanliness==3">
                            <img src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 3" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="evaluationCriteria.cleanliness==2">
                            <img src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 4" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="evaluationCriteria.cleanliness==1">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 4" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                    </div>
                </div>
                <div class="rating__item">
                    <div class="item__title">대기시간</div>
                    <div class="item__number">
                        <span v-if="evaluationCriteria.waitTime==10">
                            <img v-for="count in 5" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                        </span>
                        <span v-else-if="evaluationCriteria.waitTime==9">
                            <img v-for="count in 4" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                        </span>
                        <span v-else-if="evaluationCriteria.waitTime==8">
                            <img v-for="count in 4" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="evaluationCriteria.waitTime==7">
                            <img v-for="count in 3" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="evaluationCriteria.waitTime==6">
                            <img v-for="count in 3" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 2" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="evaluationCriteria.waitTime==5">
                            <img v-for="count in 2" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 2" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="evaluationCriteria.waitTime==4">
                            <img v-for="count in 2" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 3" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="evaluationCriteria.waitTime==3">
                            <img src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 3" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="evaluationCriteria.waitTime==2">
                            <img src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 4" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="reviewHospital.waitTime==1">
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
                  <span class="title__disease">받은 치료: {{reviewHospital.disease}}</span>
              </div>

              <div class="rightBox__content">
                  {{reviewHospital.content}}
              </div>
          </div>
    </div>
  </section>
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
#staffViewReviewItem{
  position:relative;
  text-align:left;
  left:12%;
  width:73%;  
}

#staffViewReviewItem .staffViewReviewItem__title{
    text-align:center;
}

#staffViewReviewItem .myReview__content{
    margin-top:50px;
    display:flex;
    margin-bottom:20px;
    border-bottom: 1px solid #dee2e6!important;
    padding-bottom: 20px;
}

#staffViewReviewItem .myReview__content .leftBox__hospitalName{
    text-align:center;
    letter-spacing: -0.9px;
    color: #000000;  
    font-size: 14px;
    font-weight: bold;
    padding-bottom: 3px;
    border-bottom: 1px solid #dee2e6!important;
}

#staffViewReviewItem .myReview__content .leftBox__sumRating{
    display:flex;
    padding-bottom: 3px;
    border-bottom: 1px solid #dee2e6!important;
    width:100%;
    margin:auto;
}

#staffViewReviewItem .myReview__content .sumrating__score{
    width:50%;
}

#staffViewReviewItem .myReview__content .sumRating__status{
    text-align:right;
    width:50%;
}

#staffViewReviewItem .myReview__content .score__acquire{
    font-size: 18px;
    font-weight: bold;
    letter-spacing: -0.9px;
    color: #1d458d;
}

#staffViewReviewItem .myReview__content .score__total{
    letter-spacing: -0.7px;
    color: #9b9b9b;
    font-size: 14px;
}

#staffViewReviewItem .myReview__content .rating__item{
    padding-top:5px;
    display:flex;
    width:100%;
    border-bottom: 1px solid #dee2e6!important;
}

#staffViewReviewItem .myReview__content .item__title{
    width:50%;
}

#staffViewReviewItem .myReview__content .item__number{
    text-align:right;
    width:50%;
}

#staffViewReviewItem .myReview__content .rightBox__topContent{
    display:inline-block;
    width:100%;
}

#staffViewReviewItem .myReview__content .rightBox__title{
    display:inline-block;
    width:100%;
    margin-bottom:2%;
}

#staffViewReviewItem .myReview__content .title__disease{
    font-weight: bold;
    letter-spacing: -0.8px;
}

#staffViewReviewItem .myReview__content .rightBox__contet{
    font-size: 14px;
    font-weight: normal;
    letter-spacing: -0.6px;
}

#staffViewReviewItem .myReview__content .content__leftBox{
    width:25%;
    margin-right:10px;
}

#staffViewReviewItem .myReview__content .content__rightBox{
    width:75%;
}

#staffViewReviewItem .topContent__Noreceipt{
    text-align:left;
    border: 2px solid #1d458d; 
    font-size: 12px; 
    padding-top:2px; 
    padding-bottom: 1px;
    border-radius:4px;
}

#staffViewReviewItem .topContent__receipt{
    text-align:left;
    background-color: #1d458d; 
    color:#FFFFFF;
    font-size: 12px; 
    padding-top:2px; 
    padding-bottom: 1px;
    border-radius:4px;
}

</style>