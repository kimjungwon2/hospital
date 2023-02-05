<template>
  <section id="questionAndAnswer">
    <section class="questionAndAnswer__search">
      <br>
      <form @submit.prevent="submitForm">
              <select name="searchCondition" v-model="searchCondition">
                    <option value="nickName">닉네임</option>
                    <option value="memberIdName" >회원 아이디</option>
              </select>
          <input id="keyword" type="text" required  v-model="keyword"/>
          <button type="submit">
            <font-awesome-icon icon="search"/>
          </button>
          <br><br>
      </form>
    </section>

    <section class="questionAndAnswer__questions" v-for="question in questions.content" :key="question.questionId" >
        <div class="questions__item">
          <img src='@/assets/question.png' alt="question" class="question__image">
          <div class="question__speech-bubble">
            {{ question.content}}

            <div class="speech-bubble__user">
              (아이디) {{ question.memberIdName}}<br>
              (닉네임) {{ question.nickName }}
            </div>
          </div>
        </div>

        <div class="questions__answer" v-if="question.answerId===null">
          <div class="answer__noAnswer">
            <h3>답변이 작성되지 않았습니다.</h3>
            <button id="answer__register" @click.prevent="writeAnswer(question.questionId)">작성하기</button>
          </div>
          
          <form v-if="addAnswer[question.questionId]" @submit.prevent="submitAnswer(question.questionId)">  
            <div class="answer__write">
                <label for="answer"><h3>답변 작성</h3></label>
                <textarea id="answer" type="text" placeholder="답변을 작성해주세요." required v-model="answerContent [question.questionId]"/>
            </div>
            <div class="answer__register__button">
                <button id="answer__register" type="submit">등록</button>
            </div>
          </form>
        </div>

        <div class="questions__existAnswer" v-else>
          <div class="existAnswer__speech-bubble">
            {{ question.answerContent}}
            <p class="nickName">병원 관리자</p>
          </div>
          <img src='@/assets/answer.png' alt="answer" class="answer__image">  
        </div>
        
    </section>

    <section class="questionAndAnswer__page" v-if="totalPageNum!==0">
      <br><br>
      <button :disabled="pageNum === 0" @click.prevent="prevPage">
        이전
      </button>
      <span v-if="questions.totalElements!==0">{{ pageNum + 1 }} / {{ totalPageNum }} 페이지</span>
      <span v-else> 0 / {{ totalPageNum }} 페이지</span>
      <button :disabled="pageNum >= totalPageNum - 1" @click.prevent="nextPage">
        다음
      </button>
    </section>
  </section>
</template>

<script>
import { library } from '@fortawesome/fontawesome-svg-core';
import { faSearch } from '@fortawesome/free-solid-svg-icons';
import { staffSearchQuestionLists, staffRegisterAnswer} from '@/api/staff';
library.add(faSearch)

export default {
   data() {
     return {
       //데이터  
       pageNum: 0,
       totalPageNum:'',

       //답변 내용
       answerContent:[],
       //답변 추가 유무 확인
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

        const {data} = await staffSearchQuestionLists(obj);
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

        const {data} = await staffSearchQuestionLists(obj);
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

        const {data} = await staffSearchQuestionLists(obj);
        this.questions = data;
        this.loadIsAnswerData();
    },
    writeAnswer(questionId){
      this.$set(this.addAnswer, questionId, true);
    },
    loadIsAnswerData(){
      for(let i in this.questions.content){
        this.addAnswer[this.questions.content[i].questionId] = false;
      }
    },
    async loadQuestionData(){
        const obj ={};
        let page = "page";
        obj[page] = this.pageNum;

        const {data} = await staffSearchQuestionLists(obj);
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
      this.$store.commit('minusAnswerCount',this.count);
    }

   },

   async created(){
       const condition ={
            page: 0,
       }
        const {data} = await staffSearchQuestionLists(condition);
        this.questions = data;
        this.totalPageNum = this.questions.totalPages;
        this.loadIsAnswerData();
    }
}
</script>

<style>
#questionAndAnswer{
  position:relative;
  text-align:left;
  left:12%;
  width:73%;  
}

#questionAndAnswer .question__speech-bubble{
  padding:18px;
  background-color:#fbceb1;
  border-radius:20px;
  word-break:break-all;
}

#questionAndAnswer .speech-bubble__user{
  color:#0067a3;
  text-align:right;
}

#questionAndAnswer .questions__item{
  display:flex;
  margin:32px 0;
}


#questionAndAnswer .questions__answer .answer__noAnswer{
  text-align:right;
}


#questionAndAnswer .questions__answer #answer__register{
  background-color:#0067a3; 
  color: white; 
  font-size: 15px; 
  height: 32px; 
  line-height: 32px;
  margin-right:10px;
  border-radius:10px;
}

#questionAndAnswer .questions__answer .answer__register__button{
  text-align: right;
}

#questionAndAnswer .questions__answer .answer__write #answer{
    width:100%;
    height:100px;
    border-radius: 10px;
}

#questionAndAnswer .answer__register__button #answer__register{
  background-color:#99cc99; 
  color: white; 
  font-size: 15px; 
  height: 32px; 
  line-height: 32px;
  margin-right:10px;
  border-radius:10px;  
}

#questionAndAnswer .questionAndAnswer__search{
  text-align: center;
}

.questionAndAnswer__page{
  text-align: center;
}

#questionAndAnswer .questions__existAnswer{
  display:flex;
  margin-left:10px;
  width:100%;
  justify-content: flex-end;
}

#questionAndAnswer .existAnswer__speech-bubble{
  padding:18px;
  background-color:#fbceb1;
  border-radius:30px;
  word-break:break-all;
}

#questionAndAnswer .existAnswer__speech-bubble .nickName{
  color:palevioletred;
  text-align:right;
}

</style>