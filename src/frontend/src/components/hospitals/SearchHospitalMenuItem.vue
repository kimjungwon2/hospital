<template>
  <div>
    <ul>
        <li>병원 검색</li>
        <li @click.prevent="routerReview">리뷰 검색</li>
    </ul>
  </div>
</template>

<script>
export default {
    methods:{
        routerReview(){
            const searchName= this.$route.params.searchName;
            this.$router.push('/search/review/'+searchName);
        },
    },
}
</script>

<style>

</style>