<template>
  <div>
    <form @submit.prevent="submitForm">
            <select name="searchCondition" v-model="searchCondition">
                  <option value="memberIdName">아이디</option>
                  <option value="hospitalName">병원 이름</option>
                  <option value="nickName" >닉네임</option>
            </select>
        <input id="keyword" type="text" v-model="keyword"/><button type="submit">검색하기</button>
	</form>

    <ul v-for="question in questions.content" :key="question.questionId" >
      <div>
        <li>질문 번호: {{ question.questionId }} </li>
        <li>작성 아이디: {{ question.memberIdName}}</li>
        <li>작성 닉네임: {{ question.nickName }}</li>
        <li>등록 병원: {{ question.hospitalName}}</li>
        <li>질문 내용: {{ question.content}}</li>
        <div v-if="question.answerId!==null">
            <li>답변 번호: {{ question.answerId}}</li>
            <li>답변 내용: {{ question.answerContent}}</li>
        </div>
        <div v-else>
            <h3>답변이 등록되지 않았습니다.</h3>
        </div>
        <div><font-awesome-icon icon="trash-alt" @click.prevent="deleteQuestion(question.questionId, question.answerId)"/></div>
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
import { adminSearchQuestionsList, adminDeleteQuestion} from '@/api/admin';

library.add(faTrashAlt)

export default {
   data() {
     return {
       //페이징 관련 데이터 
       pageNum: 0,
       totalPageNum:'',

       //질문 데이터
       questions: [],
       //검색 조건 default = memberIdName로 한다. 
       searchCondition:'memberIdName',
       //검색명
       keyword:'',
     }
   },
   methods:{
    //Q&A 삭제
    async deleteQuestion(questionId){
      if(confirm('정말로 Q&A를 삭제하시겠습니까?')){
            await adminDeleteQuestion(questionId);
            this.adminLoadQuestions();
      }
    },
    async adminLoadQuestions(){
        const condition ={};
        let key = this.searchCondition;
        let page = "page";
        condition[key] = this.keyword;
        condition[page] = this.pageNum;

        const {data} = await adminSearchQuestionsList(condition);
        this.questions = data;
    },
    
    //검색
    async submitForm(){
        this.pageNum = 0;
        const obj ={};
        let key = this.searchCondition;
        obj[key] = this.keyword;

        const {data} = await adminSearchQuestionsList(obj);
        this.questions = data;
        this.totalPageNum = this.questions.totalPages;
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

        const {data} = await adminSearchQuestionsList(obj);
        this.questions = data;
    },
    //다음 페이지
    async nextPage(){
        this.pageNum+=1;

        const obj ={};
        let key = this.searchCondition;
        let page = "page";
        obj[key] = this.keyword;
        obj[page] = this.pageNum;

        const {data} = await adminSearchQuestionsList(obj);
        this.questions = data;
    }
   },

   async created(){
       const condition ={
            page: 0,
       }
        const {data} = await adminSearchQuestionsList(condition);
        this.questions = data;
        this.totalPageNum = this.questions.totalPages;
    }
}
</script>

<style>

</style>