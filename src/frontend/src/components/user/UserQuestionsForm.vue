<template>
  <section id="userQuestionLists">
    <div class="userQuestionLists__title">
        <h1>질문 목록</h1>
    </div>

      <div class="userQuestionLists__item" v-for="contentItem in contentItems" :key="contentItem.questionId">
        <div class="item__hospitalName" @click="routeViewHospital(contentItem)">
            <h1>{{ contentItem.hospitalName }}</h1>
        </div>
        <div class="item__question">
            <img src='@/assets/question.png' alt="question" class="question__image">
            <div class="question__speech-bubble">
                <p>{{contentItem.content}}</p>
                <p class="nickName">{{contentItem.nickName}}</p>
            </div>
        </div>


        <div class="item_answer" v-if="contentItem.answerId !==null">
            <div class="answer__speech-bubble">
                {{contentItem.answerContent}}
                <p class="nickName">병원 관리자</p>
            </div>
            <img src='@/assets/answer.png' alt="answer" class="answer__image">    
        </div>
        <div class="item_answer" v-else>
            <div class="answer__speech-bubble">
                <h3>아직 답변이 등록되지 않았습니다.</h3>
                <p class="nickName">병원 관리자</p>
            </div>
            <img src='@/assets/answer.png' alt="answer" class="answer__image">
        </div>
       </div>

      <div class="userQuestionLists__title" v-if="contentItems.length===0">
        <br>
        <h3> 등록된 질문이 없습니다. </h3>
      </div>
  </section>
</template>

<script>
import {viewUserQuestions} from '@/api/user';

export default {
    
    data() {
        return {
            contentItems:[],
        };
    },
    methods:{
        async userQuestions(){
            const id = this.$route.params.id;
            const { data } = await viewUserQuestions(id);
            this.contentItems = data;
        },
        routeViewHospital(contentItem){
            const id = contentItem.hospitalId;
            this.$router.push(`/hospital/view/${id}`);
        },
    },
    created(){
        this.userQuestions();
    },
};
</script>

<style>
.userQuestionLists__title{
  text-align:center;
}

.userQuestionLists__item{
  position:relative;
  text-align:left;
  margin-top:20px;
  margin-bottom:20px;
  border-bottom: 1px solid #dee2e6!important;
  padding-bottom: 20px;
  left:12%;
  width:73%;
}

.userQuestionLists__item .item__question{
  display:flex;
  margin:32px 0;
}


.item__question .question__image{
  margin-right:20px;
  width:100px;
  height:100px;
}

.item_answer .answer__image{
  margin-right:20px;
  width:100px;
  height:100px;
}

.item__question .question__speech-bubble{
  padding:18px;
  background-color:#fbceb1;
  border-radius:20px;
  word-break:break-all;
}

.question__speech-bubble .nickName{
  color:palevioletred;
  text-align:right;
}

.item_answer .answer__speech-bubble{
  padding:18px;
  background-color:#fbceb1;
  border-radius:30px;
  word-break:break-all;
}

.item_answer{
  display:flex;
  margin-left:10px;
  width:100%;
  justify-content: flex-end;
}

.answer__speech-bubble .nickName{
  color:palevioletred;
  text-align:right;
}

</style>