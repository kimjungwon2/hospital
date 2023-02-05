<template>
  <section id="myReview" v-if="contentItems.length!==0">
    <div class="myReview__title">
        <h1>리뷰 목록</h1>
    </div>

      <div class="myReview__content" v-for="contentItem in contentItems" :key ="contentItem.reviewId">
          <div class="content__leftBox">
              <div @click="routeViewHospital(contentItem)" class="leftBox__hospitalName">
                병원: {{contentItem.reviewHospitals[0].hospitalName}}
              </div>
              <div class="leftBox__sumRating">
                 <span class="sumRating__score"><img src='@/assets/star-resize.png' alt="rating-star" class="score__star">
                    <span class="score__acquire">{{contentItem.reviewHospitals[0].averageRate}}</span>
                    <span class="score__total"> / 10 </span>
                 </span> 
                 <div class="sumRating__status" v-if="contentItem.reviewHospitals[0].recommendationStatus=='RECOMMENDATION'">
                     <img src='@/assets/smile.png' alt="smile" class="status__smile">
                 </div>
                 <div class="sumRating__status" v-if="contentItem.reviewHospitals[0].recommendationStatus=='DECOMMENDATION'">
                     <img src='@/assets/angry.png' alt="angry" class="status__angry">
                 </div>
              </div>
              <div class="leftBox__rating">
                <div class="rating__item">
                    <div class="item__title">가격</div>
                    <div class="item__number">
                        <span v-if="contentItem.reviewHospitals[0].sumPrice==10">
                            <img v-for="count in 5" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].sumPrice==9">
                            <img v-for="count in 4" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].sumPrice==8">
                            <img v-for="count in 4" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].sumPrice==7">
                            <img v-for="count in 3" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].sumPrice==6">
                            <img v-for="count in 3" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 2" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].sumPrice==5">
                            <img v-for="count in 2" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 2" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].sumPrice==4">
                            <img v-for="count in 2" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 3" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].sumPrice==3">
                            <img src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 3" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].sumPrice==2">
                            <img src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 4" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].sumPrice==1">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 4" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                    </div>
                </div>
                <div class="rating__item">
                    <div class="item__title">친절함</div>
                    <div class="item__number">
                    <span v-if="contentItem.reviewHospitals[0].kindness==10">
                            <img v-for="count in 5" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].kindness==9">
                            <img v-for="count in 4" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].kindness==8">
                            <img v-for="count in 4" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].kindness==7">
                            <img v-for="count in 3" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].kindness==6">
                            <img v-for="count in 3" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 2" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].kindness==5">
                            <img v-for="count in 2" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 2" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].kindness==4">
                            <img v-for="count in 2" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 3" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].kindness==3">
                            <img src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 3" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].kindness==2">
                            <img src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 4" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].kindness==1">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 4" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                    </div>
                </div>
                <div class="rating__item">
                    <div class="item__title">증상완화</div>
                    <div class="item__number">
                        <span v-if="contentItem.reviewHospitals[0].symptomRelief==10">
                            <img v-for="count in 5" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].symptomRelief==9">
                            <img v-for="count in 4" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].symptomRelief==8">
                            <img v-for="count in 4" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].symptomRelief==7">
                            <img v-for="count in 3" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].symptomRelief==6">
                            <img v-for="count in 3" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 2" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].symptomRelief==5">
                            <img v-for="count in 2" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 2" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].symptomRelief==4">
                            <img v-for="count in 2" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 3" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].symptomRelief==3">
                            <img src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 3" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].symptomRelief==2">
                            <img src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 4" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].symptomRelief==1">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 4" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                    </div>
                </div>
                <div class="rating__item">
                    <div class="item__title">청결</div>
                    <div class="item__number">
                        <span v-if="contentItem.reviewHospitals[0].cleanliness==10">
                            <img v-for="count in 5" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].cleanliness==9">
                            <img v-for="count in 4" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].cleanliness==8">
                            <img v-for="count in 4" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].cleanliness==7">
                            <img v-for="count in 3" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].cleanliness==6">
                            <img v-for="count in 3" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 2" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].cleanliness==5">
                            <img v-for="count in 2" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 2" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].cleanliness==4">
                            <img v-for="count in 2" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 3" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].cleanliness==3">
                            <img src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 3" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].cleanliness==2">
                            <img src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 4" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].cleanliness==1">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 4" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                    </div>
                </div>
                <div class="rating__item">
                    <div class="item__title">대기시간</div>
                    <div class="item__number">
                        <span v-if="contentItem.reviewHospitals[0].waitTime==10">
                            <img v-for="count in 5" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].waitTime==9">
                            <img v-for="count in 4" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].waitTime==8">
                            <img v-for="count in 4" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].waitTime==7">
                            <img v-for="count in 3" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].waitTime==6">
                            <img v-for="count in 3" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 2" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].waitTime==5">
                            <img v-for="count in 2" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 2" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].waitTime==4">
                            <img v-for="count in 2" :key="count" src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 3" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].waitTime==3">
                            <img src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 3" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].waitTime==2">
                            <img src='@/assets/꽉찬별.png' alt="star" class="number__star">
                            <img v-for="count in 4" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                        <span v-else-if="contentItem.reviewHospitals[0].waitTime==1">
                            <img src='@/assets/반쪽별.png' alt="star" class="number__halfStar">
                            <img v-for="count in 4" :key="count" src='@/assets/빈별.png' alt="star" class="number__voidStar">
                        </span>
                    </div>
                </div>
              </div>
          </div>

          <div class="content__rightBox">
              <div class="rightBox__topContent">
                <span class="topContent__Noreceipt" v-if="(contentItem.authenticationStatus=='NONE') || (contentItem.authenticationStatus===null)">
                    미인증 리뷰
                </span>
                <span class="topContent__Noreceipt" v-else-if="contentItem.authenticationStatus=='WAITING'">
                    인증 대기
                </span>
                <span class="topContent__receipt" v-else-if="contentItem.authenticationStatus=='CERTIFIED'">
                    영수증 인증
                </span>

                <div class="topContent__date">작성일: {{contentItem.createdDate|formatDate}}</div>
              </div>

              <div class="rightBox__title">
                  <span class="title__disease">받은 치료: {{contentItem.reviewHospitals[0].disease}}</span>
              </div>

              <div class="rightBox__content">
                  {{contentItem.reviewHospitals[0].content}}
              </div>

          </div>
      </div>
  </section>
  <section id="myReview" v-else>
    <div class="myReview__title">
        <h1>리뷰 목록</h1><br>
        <h3>등록된 리뷰가 없습니다.</h3>
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

#myReview .myReview__title{
    text-align:center;
}

.myReview__content{
    margin-top:20px;
    display:flex;
    margin-bottom:20px;
    border-bottom: 1px solid #dee2e6!important;
    padding-bottom: 20px;
}

.myReview__content .leftBox__hospitalName{
    text-align:center;
    letter-spacing: -0.9px;
    color: #000000;  
    font-size: 14px;
    font-weight: bold;
    padding-bottom: 3px;
    border-bottom: 1px solid #dee2e6!important;
}

.myReview__content .leftBox__sumRating{
    display:flex;
    padding-bottom: 3px;
    border-bottom: 1px solid #dee2e6!important;
    width:100%;
    margin:auto;
}

.myReview__content .sumrating__score{
    width:50%;
}

.myReview__content .sumRating__status{
    text-align:right;
    width:50%;
}

.myReview__content .score__acquire{
    font-size: 18px;
    font-weight: bold;
    letter-spacing: -0.9px;
    color: #1d458d;
}

.myReview__content .score__total{
    letter-spacing: -0.7px;
    color: #9b9b9b;
    font-size: 14px;
}

.myReview__content .rating__item{
    padding-top:5px;
    display:flex;
    width:100%;
    border-bottom: 1px solid #dee2e6!important;
}

.myReview__content .item__title{
    width:50%;
}

.myReview__content .item__number{
    text-align:right;
    width:50%;
}

.myReview__content .rightBox__topContent{
    display:inline-block;
    width:100%;
}

.myReview__content .rightBox__title{
    display:inline-block;
    width:100%;
    margin-bottom:2%;
}

.myReview__content .title__disease{
    font-weight: bold;
    letter-spacing: -0.8px;
}

.myReview__content .rightBox__contet{
    font-size: 14px;
    font-weight: normal;
    letter-spacing: -0.6px;
}

.myReview__content .content__leftBox{
    width:25%;
    margin-right:10px;
}

.myReview__content .content__rightBox{
    width:75%;
}

#myReview .topContent__Noreceipt{
    text-align:left;
    border: 2px solid #1d458d; 
    font-size: 12px; 
    padding-top:2px; 
    padding-bottom: 1px;
    border-radius:4px;
}

#myReview .topContent__receipt{
    text-align:left;
    background-color: #1d458d; 
    color:#FFFFFF;
    font-size: 12px; 
    padding-top:2px; 
    padding-bottom: 1px;
    border-radius:4px;
}

</style>