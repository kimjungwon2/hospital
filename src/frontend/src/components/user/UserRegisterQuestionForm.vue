<template>
  <div>
    <form @submit.prevent="submitForm">
        <div>
            <label for="content">질문 내용</label>
            <input id="content" type="text" required v-model="content">
        </div>
        <div class="button">
            <button type="submit">질문 등록</button>
        </div>
    </form>
      
  </div>
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

</style>