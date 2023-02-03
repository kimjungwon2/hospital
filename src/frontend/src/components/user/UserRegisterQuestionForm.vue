<template>
  <section id="registerQuestion">
    <form @submit.prevent="submitForm">
      <div class="question__content">    
        <div class="content__title">
            <label for="content">질문 내용</label>
        </div>
        <div class="write__content__content">
            <textarea id="content" name ="content" required v-model="content"></textarea>
        </div>
        <div class="register__button">
          <button id="question__button" type="submit">질문 등록</button>
        </div>
      </div>
    </form>
      
  </section>
</template>

<script>
import {registerUserQuestion} from '@/api/user';

export default {
  data() {
    return {
        hospitalId: '',
        memberId:'',
        content:'',
    }
  },
  methods:{
    async submitForm(){
            const reviewData ={
                    hospitalId: this.hospitalId,
                    memberId:this.memberId,
                    content:this.content,
            }

            await registerUserQuestion(reviewData);
            this.$alert('질문이 등록되었습니다.');
            this.$router.push('/hospital/view/'+this.hospitalId);
      },
  },
  created(){
        this.hospitalId = this.$route.params.hospitalId;
        this.memberId = this.$store.getters.getMemberId;
  },
}
</script>

<style>
#registerQuestion{
  position:relative;
  text-align:left;
  left:12%;
  width:73%;
  margin-top:20px;
}

.question__content{
  position:relative;
  bottom:-200px;
}

.question__content .content__title{
    position:relative;
    left:8px;
    margin-bottom:5px;
    font-weight:bold;
}

.write__content__content #content{
    width:100%;
    height:200px;
    border-radius: 10px;
}

.register__button{
  text-align:right;
}

.register__button #question__button{
    background-color:#b0b8fb; 
    color: white; 
    border-radius:10px;
    height:30px;
}

</style>