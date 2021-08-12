<template>
  <div>
    <div v-if="this.staffHosInfoId!==null">
        <h1>Q&A</h1>
        <ul v-for="QandA in QandAs" :key="QandA.reviewId">
          <li>
            <div>
              <h1>Q)</h1>
              닉네임: {{QandA.nickName}}
            </div>
            <div>
              내용: {{QandA.content}}
            </div>
            <div v-if="QandA.answerId!==null">
              <h1>A)</h1>
              답변: {{QandA.answerContent}}
            </div>
            <div v-else>
              <h2>아직 답변이 없습니다.</h2>
            </div>
          </li>
        </ul>
        <button v-if="isLogin || isStaff || isAdmin" @click.prevent="routeRegisterQuestion">질문 작성</button>
    </div>

    <div v-else>
        <h3>직원이 등록된 병원이 아니라 Q&A를 지원하지 않습니다.</h3>
    </div>
  </div>
</template>

<script>
import { viewHospitalQandA } from '@/api/hospital';

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

</style>