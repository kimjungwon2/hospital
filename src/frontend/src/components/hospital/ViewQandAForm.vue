<template>
  <div><h1>Q&A</h1>
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
          <h1>아직 답변이 없습니다.</h1>
        </div>
      </li>
    </ul>
  </div>

</template>

<script>
import { viewHospitalQandA } from '@/api/hospital';

export default {
  data() {
    return {
      QandAs: [],
    }
  },
    async created(){
        const id = this.$route.params.id;
        const {data} = await viewHospitalQandA(id);
        this.QandAs = data;
        console.log(data);
    }
}
</script>

<style>

</style>