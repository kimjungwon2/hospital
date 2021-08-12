<template>
  <div>
    미 답변 질문 수: {{questions.totalElements}}<br>
    <form @submit.prevent="submitForm">
            <select name="searchCondition" v-model="searchCondition">
                  <option value="nickName">닉네임</option>
                  <option value="memberIdName" >회원 아이디</option>
            </select>
        <input id="keyword" type="text" required  v-model="keyword"/><button type="submit">검색하기</button>
	</form>

    <ul v-for="question in questions.content" :key="question.questionId" >
      <div>
        <li>아이디: {{ question.memberIdName}}</li>
        <li>닉네임: {{ question.nickName }}</li>
        <li>질문 내용: {{ question.content}}</li>
      </div>
      <div v-if="question.answerId===null">
        <h3>답변이 작성되지 않았습니다.</h3>
        <div @click.prevent="writeAnswer(question.questionId)">작성하기</div>

          <form v-if="addAnswer[question.questionId]" @submit.prevent="submitAnswer(question.questionId)">  
            <div class="input-box">
                <label for="answer">답변 작성: </label>
                <input id="answer" type="text" placeholder="답변을 적어주세요." required v-model="answerContent[question.questionId]">
            </div>
            <div class="button">
                <button type="submit">등록</button>
            </div>
          </form>
      </div>
      <div v-else>
        <li>답변 내용: {{ question.answerContent}}</li>
      </div>
    </ul>

    <div>
      <button :disabled="pageNum === 0" @click.prevent="prevPage">
        이전
      </button>
      <span v-if="questions.totalElements!==0">{{ pageNum + 1 }} / {{ totalPageNum }} 페이지</span>
      <span v-else> 0 / {{ totalPageNum }} 페이지</span>
      <button :disabled="pageNum >= totalPageNum - 1" @click.prevent="nextPage">
        다음
      </button>
    </div>
  </div>
</template>

<script>
import { staffSearchNoAnswerQuestions,staffRegisterAnswer} from '@/api/staff';


export default {
   data() {
     return {
       //데이터  
       pageNum: 0,
       totalPageNum:'',

      //답변 내용
       answerContent:[],
      //답변 작성 여부 확인
       addAnswer:[],

       //질문 데이터
       questions: [],
       //검색 조건 default = memberIdName로 한다. 
       searchCondition:'memberIdName',
       //검색명
       keyword:'',
     }
   },
   methods:{
    //검색
    async submitForm(){
        this.pageNum = 0;
        const obj ={};
        let key = this.searchCondition;
        obj[key] = this.keyword;

        const {data} = await staffSearchNoAnswerQuestions(obj);
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

        const {data} = await staffSearchNoAnswerQuestions(obj);
        this.questions = data;
        this.loadIsAnswerData();
    },
    //다음 페이지
    async nextPage(){
        this.pageNum+=1;

        const obj ={};
        let key = this.searchCondition;
        let page = "page";
        obj[key] = this.keyword;
        obj[page] = this.pageNum;

        const {data} = await staffSearchNoAnswerQuestions(obj);
        this.questions = data;
        this.loadIsAnswerData();
    },
    writeAnswer(questionId){
      this.$set(this.addAnswer,questionId,true);
    },
    loadIsAnswerData(){
      for(let i in this.questions.content){
        if(this.questions.content[i].answerId ===null){
          this.addAnswer[this.questions.content[i].questionId] = false;
        }
      }
    },
    async loadQuestionData(){
        const obj ={};
        let page = "page";
        obj[page] = this.pageNum;

        const {data} = await staffSearchNoAnswerQuestions(obj);
        this.questions = data;
    },
    async submitAnswer(questionId){
      const data ={
        memberId:this.$store.getters.getMemberId,
        questionId:questionId,
        answerContent:this.answerContent[questionId],
      }
      await staffRegisterAnswer(data);
      this.loadQuestionData();
    }
   },

   async created(){
       const condition ={
            page: 0,
       }
        const {data} = await staffSearchNoAnswerQuestions(condition);
        this.questions = data;
        this.totalPageNum = this.questions.totalPages;
        this.loadIsAnswerData();
    }

}
</script>

<style>

</style>