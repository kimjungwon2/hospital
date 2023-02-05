<template>
  <section id="staffViewReviews">
    <section class="staffViewReviews__count">
      <h1>등록된 리뷰 수: {{reviews.totalElements}}</h1><br><br>
    </section>

    <section class="staffViewReviews__search">
      <form @submit.prevent="submitForm">
              <select name="searchCondition" v-model="searchCondition">
                    <option value="memberIdName">아이디</option>
                    <option value="nickName" >닉네임</option>
              </select>
          <input id="keyword" type="text" v-model="keyword"/>
          <button type="submit">
            <font-awesome-icon icon="search"/>
          </button>
          <br><br>
	    </form>
    </section>

    <section class="staffViewReviews__review">
      <div class="review__item" v-for="review in reviews.content" :key="review.reviewId" @click="routeStaffViewReview(review.reviewId)">
          리뷰 번호: {{ review.reviewId }} <br>
          작성 아이디: {{ review.memberIdName}}<br>
          작성 닉네임: {{ review.nickName }}<br>
          평균 평가: {{ review.reviewHospitals[0].averageRate}}<br>
          인증 상태: {{ review.reviewAuthentication}}<br>
          좋아요 수: {{ review.reviewLike.length}}
      </div>
    </section>

    <section class="staffViewReviews__page" v-if="totalPageNum!==0">
      <button :disabled="pageNum === 0" @click.prevent="prevPage">
        이전
      </button>
      <span>{{ pageNum + 1 }} / {{ totalPageNum }} 페이지</span>
      <button :disabled="pageNum >= totalPageNum - 1" @click.prevent="nextPage">
        다음
      </button>
    </section>
  </section>
</template>

<script>
import { library } from '@fortawesome/fontawesome-svg-core';
import { faSearch } from '@fortawesome/free-solid-svg-icons';
import { staffSearchReviewLists} from '@/api/staff';
library.add(faSearch)

export default {
   data() {
     return {
       //데이터  
       pageNum: 0,
       totalPageNum:'',

       //리뷰 데이터
       reviews: [],
       //검색 조건 default = memberIdName로 한다. 
       searchCondition:'memberIdName',
       //검색명
       keyword:'',
     }
   },
   methods:{
    routeStaffViewReview(reviewId){
         this.$router.push('/staff/review/view/'+reviewId)
    },

    //검색
    async submitForm(){
        this.pageNum = 0;
        const obj ={};
        let key = this.searchCondition;
        obj[key] = this.keyword;

        const {data} = await staffSearchReviewLists(obj);
        this.reviews = data;
        this.totalPageNum = this.reviews.totalPages;
    },
    //이전 페이지
    async prevPage(){
        this.pageNum-=1;
        
        const obj ={};
        let key = this.searchCondition;
        let page = "page";
        //검색
        obj[key] = this.keyword;
        //페이지 번호
        obj[page] = this.pageNum;

        const {data} = await staffSearchReviewLists(obj);
        this.reviews = data;
    },
    //다음 페이지
    async nextPage(){
        this.pageNum+=1;

        const obj ={};
        let key = this.searchCondition;
        let page = "page";
        obj[key] = this.keyword;
        obj[page] = this.pageNum;

        const {data} = await staffSearchReviewLists(obj);
        this.reviews = data;
    }
   },

   async created(){
       const condition ={
            page: 0,
       }
        const {data} = await staffSearchReviewLists(condition);
        this.reviews = data;
        this.totalPageNum = this.reviews.totalPages;
    }
}
</script>

<style>
#staffViewReviews{
  position:relative;
  text-align:left;
  left:12%;
  width:73%;    
}

#staffViewReviews .staffViewReviews__count{
  text-align: center;
}

#staffViewReviews .staffViewReviews__search{
  text-align: center;
}

.staffViewReviews__page{
  text-align: center;
}

.staffViewReviews__review .review__item{
  margin-bottom: 20px;
  position:relative;
  text-align:left;
  left:12%;
  width:73%;
  border: 2px solid #0067a3;
  border-radius:10px;
}

</style>