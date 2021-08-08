<template>
  <div>
      <ul v-for="contentItem in contentItems" :key="contentItem.questionId">
        <li @click="routeViewHospital(contentItem)">
            병원: {{ contentItem.hospitalName }} | 등록 닉네임: {{contentItem.nickName}}
        </li>
        <li>
            질문 내용: {{contentItem.content}}
        </li>
        <li v-if="contentItem.answerId !==null">
            답변 내용: {{contentItem.answerContent}}    
        </li>
        <li v-else>
            <h3>아직 답변이 등록되지 않았습니다.</h3>
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