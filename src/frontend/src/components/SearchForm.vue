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
            this.$router.push('/search');
        },
    },
};
</script>

<style>

</style>