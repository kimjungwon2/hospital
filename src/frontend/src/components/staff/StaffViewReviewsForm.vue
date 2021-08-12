<template>
  <div>
    등록된 리뷰 갯수: {{reviews.totalElements}}<br>
    <form @submit.prevent="submitForm">
            <select name="searchCondition" v-model="searchCondition">
                  <option value="memberIdName">아이디</option>
                  <option value="nickName" >닉네임</option>
            </select>
        <input id="keyword" type="text" v-model="keyword"/><button type="submit">검색하기</button>
	</form>

    <ul v-for="review in reviews.content" :key="review.reviewId" >
      <div>
        <li @click="routeStaffViewReview(review.reviewId)">리뷰 번호: {{ review.reviewId }} </li>
        <li>작성 아이디: {{ review.memberIdName}}</li>
        <li>작성 닉네임: {{ review.nickName }}</li>
        <li>인증 상태: {{ review.reviewAuthentication}}</li>
        <li>좋아요 수: {{ review.reviewLike.length}}</li>
        <li>평균 평가: {{ review.reviewHospitals[0].averageRate}}</li>
      </div>
    </ul>

    <div>
      <button :disabled="pageNum === 0" @click.prevent="prevPage">
        이전
      </button>
      <span>{{ pageNum + 1 }} / {{ totalPageNum }} 페이지</span>
      <button :disabled="pageNum >= totalPageNum - 1" @click.prevent="nextPage">
        다음
      </button>
    </div>
  </div>
</template>

<script>
import { staffSearchReviewLists} from '@/api/staff';

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

</style>