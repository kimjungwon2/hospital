<template>
  
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
            console.log(this.contentItems);
        },
    },
    created(){
        this.userQuestions();
    },
};
</script>

<style>

</style>