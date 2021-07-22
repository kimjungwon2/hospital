<template>
  <div>
      <ul v-for="contentItem in contentItems" :key="contentItem.questionId">
        <li @click="routeViewHospital(contentItem)">
            {{ contentItem.hospitalName }}
        </li>
       </ul>
  </div>
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

</style>