<template>
  <div>
    <ul>
        <li @click.prevent="routerHospital">병원 검색</li>
        <li>리뷰 검색</li>
    </ul>
  </div>
</template>

<script>
export default {
    methods:{
        routerHospital(){
            const searchName= this.$route.params.searchName;
            this.$router.push('/search/view/'+searchName);
        },
    },
}
</script>

<style>

</style>