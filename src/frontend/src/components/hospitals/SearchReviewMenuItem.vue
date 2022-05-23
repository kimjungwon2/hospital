<template>
  <div class="searchHospital__menu">
    <button class="searchHospital__menu__item" @click.prevent="routerHospital">병원 검색</button>
    <button class="searchHospital__menu__item">리뷰 검색</button>
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
.searchHospital__menu{
  text-align:center;
  margin:40px;
}

.searchHospital__menu__item{
  margin:10px;
  border:1px solid;
  border-radius:4px;
  font-size:30px;
  padding: 8px 100px;
}

.searchHospital__menu__item.active,
.searchHospital__menu__item:hover{
  background-color:#0067A3;
  color:white;
}

</style>