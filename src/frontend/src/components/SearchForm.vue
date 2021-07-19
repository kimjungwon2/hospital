<template>
      <form @submit.prevent="searchForm">
        <div>
            <label for="searchHospital"></label>
            <input id="searchHospital" type="text" maxlength = 2000 v-model="searchName">
        </div>
        <button type="submit">Search</button>
    </form>
</template>

<script>
import { saveSearchNameToCookie } from '@/utils/cookies';

export default {
    data(){
        return{
            searchName:'',
        };
    },
    methods: {
        async searchForm(){
            const data ={
                searchName: this.searchName,
            };
            this.$store.commit('setSearchName', data);
            saveSearchNameToCookie(data.searchName);
            this.$router.push('/search');
        },
    },
};
</script>

<style>

</style>