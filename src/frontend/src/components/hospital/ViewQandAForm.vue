<template>
  <div><h1>Q&A</h1>
    <ul v-for="QandA in QandAs" :key="QandA.memberId">
      <li>
        <div>
          닉네임: {{QandA.nickName}}
        </div>
        <div>
          내용: {{QandA.content}}
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
    }
}
</script>

<style>

</style>