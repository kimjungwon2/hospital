<template>
  <div>
    <ul v-for="review in reviews.content" :key="review.reviewId" >
      <div>
        <li @click="routeAdminViewReview(review.reviewId)">리뷰 번호: {{ review.reviewId }} </li>
        <li>작성 아이디: {{ review.memberIdName}}</li>
        <li>작성 닉네임: {{ review.nickName }}</li>
        <li>인증 상태: {{ review.reviewAuthentication}}</li>
        <li>등록 병원: {{ review.reviewHospitals[0].hospitalName}}</li>
        <li>평균 평가: {{ review.reviewHospitals[0].averageRate}}</li>
        <div><font-awesome-icon icon="trash-alt" @click.prevent="deleteReview(review.reviewId)"/></div>
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
import { library } from '@fortawesome/fontawesome-svg-core';
import { faTrashAlt } from '@fortawesome/free-solid-svg-icons';
import { adminSearchUnapprovedReviewLists, adminDeleteReview} from '@/api/admin';

library.add(faTrashAlt)

export default {
   data() {
     return {
       //데이터  
       pageNum: 0,
       totalPageNum:'',

       //리뷰 데이터
       reviews: [],
       //검색명
       keyword:'',
     }
   },
   methods:{
    routeAdminViewReview(reviewId){
      this.$router.push('/admin/review/view/'+reviewId)
    },
    //리뷰 삭제
    async deleteReview(reviewId){
      if(confirm('정말로 리뷰를 삭제하시겠습니까?')){
            await adminDeleteReview(reviewId);
            this.adminLoadReviews();
      }
    },
    async adminLoadReviews(){
        const condition ={};
        let page = "page";
        condition[page] = this.pageNum;

        const {data} = await adminSearchUnapprovedReviewLists(condition);
        this.reviews = data;
    },
    
    //이전 페이지
    async prevPage(){
        this.pageNum-=1;
        
        const obj ={};
        let page = "page";
        //페이지 번호
        obj[page] = this.pageNum;

        const {data} = await adminSearchUnapprovedReviewLists(obj);
        this.reviews = data;
    },
    //다음 페이지
    async nextPage(){
        this.pageNum+=1;

        const obj ={};
        let page = "page";
        obj[page] = this.pageNum;

        const {data} = await adminSearchUnapprovedReviewLists(obj);
        this.reviews = data;
    }
   },

   async created(){
       const condition ={
            page: 0,
       }
        const {data} = await adminSearchUnapprovedReviewLists(condition);
        this.reviews = data;
        this.totalPageNum = this.reviews.totalPages;
    }
}
</script>

<style>

</style>