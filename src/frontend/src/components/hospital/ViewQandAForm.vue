<template>
  <section v-if="this.staffHosInfoId!==null" id="QandA">
    <button class="functionBox__writeQuestion" v-if="isLogin || isStaff || isAdmin" @click.prevent="routeRegisterQuestion">
      <font-awesome-icon icon="pen"/> 질문 작성
    </button>

    <div class="QandA" v-for="QandA in QandAs" :key="QandA.reviewId">
        <div class="question">
            <img src='@/assets/question.png' alt="question" class="question__image">
            <div class="question__speech-bubble">
              <p>{{QandA.content}}</p>
              <p class="nickName">{{QandA.nickName}}</p>
            </div>
        </div>
        <div class="answer" v-if="QandA.answerId!==null">
            <div class="answer__speech-bubble">
              <p>{{QandA.answerContent}}</p>
              <p class="nickName">병원 관리자</p>
            </div>
            <img src='@/assets/answer.png' alt="answer" class="answer__image">
        </div>
        <div class="answer" v-else>
            <div class="answer__speech-bubble"> 
              <h4>답변 대기중</h4>
              <p class="nickName">병원 관리자</p>
            </div>
            <img src='@/assets/answer.png' alt="answer" class="answer__image">
        </div>
    </div>
  </section>
  <section v-else>
    <h3>직원이 등록된 병원이 아니라 Q&A를 지원하지 않습니다.</h3>
  </section>
</template>

<script>
import { library } from '@fortawesome/fontawesome-svg-core';
import { viewHospitalQandA } from '@/api/hospital';
import { faPen } from '@fortawesome/free-solid-svg-icons';

library.add(faPen);

export default {
  props: {
    staffHosInfoId: {
      type: undefined,
      required: true,
    },
  },
  data() {
    return {
      QandAs: [],
    }
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
  methods:{
    routeRegisterQuestion(){
      const hospitalId = this.$route.params.id;
      this.$router.push('/user/hospital/'+hospitalId+'/register/question');
    }
  },
  async created(){
        const id = this.$route.params.id;
        const {data} = await viewHospitalQandA(id);
        this.QandAs = data;
  }
}
</script>

<style>
.QandA{
  position:relative;
  text-align:left;
  margin-top:20px;
  margin-bottom:20px;
  border-bottom: 1px solid #dee2e6!important;
  padding-bottom: 20px;
  left:12%;
  width:73%;
}

.question{
  display:flex;
  margin:32px 0;
}

.question__image{
  margin-right:20px;
  width:100px;
  height:100px;
}

.question__speech-bubble{
  padding:18px;
  background-color:#fbceb1;
  border-radius:20px;
  word-break:break-all;
}

.question__speech-bubble .nickName{
  color:palevioletred;
  text-align:right;
}

.functionBox__writeQuestion{
  background-color:#b0b8fb; 
  color: white; 
  font-size: 15px; 
  height: 32px; 
  line-height: 32px;
  margin-right:10px;
  border-radius:10px;
}


.answer{
  display:flex;
  margin-left:10px;
  width:100%;
  justify-content: flex-end;
}

.answer__speech-bubble{
  padding:18px;
  background-color:#fbceb1;
  border-radius:30px;
  word-break:break-all;
}

.answer__speech-bubble .nickName{
  color:palevioletred;
  text-align:right;
}

.answer__image{
  width:100px;
  height:100px;
  position:relative;
  top:24px;
  margin-left:20px;
}



</style>