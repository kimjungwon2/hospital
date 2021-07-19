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
            this.$router.push(`/search/view/${this.searchName}`);
        },
    },
};
</script>

<style>

</style>