<template>
  <h1>hi</h1>
</template>

<script>
import {viewUserBookmarks} from '@/api/user';

export default {
    
    data() {
        return {
            contentItems:[],
        };
    },
    methods:{
        async userBookmarks(){
            const id = this.$route.params.id;
            const { data } = await viewUserBookmarks(id);
            this.contentItems = data;
            console.log(this.contentItems);
        },
    },
    created(){
        this.userBookmarks();
    },
};
</script>

<style>

</style>