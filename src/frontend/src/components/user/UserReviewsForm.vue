<template>
  
</template>

<script>
import {viewUserReviews} from '@/api/user';

export default {
    
    data() {
        return {
            contentItems:[],
        };
    },
    methods:{
        async userReviews(){
            const id = this.$route.params.id;
            const { data } = await viewUserReviews(id);
            this.contentItems = data;
            console.log(this.contentItems);
        },
    },
    created(){
        this.userReviews();
    },
};
</script>

<style>

</style>