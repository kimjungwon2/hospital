<template>

  <section id="main">
      <img src='@/assets/병원 로고.png' alt="gyeonggi do logo" class="main__logo"><br>
      <h1 class="main__title">경기도 내 모든 병원</h1><br>
      <SearchForm></SearchForm>
  </section>

</template>

<script>
import SearchForm from '@/components/SearchForm.vue';
export default {
  components:{
    SearchForm,
  },
};
</script>

<style>
#main {
  padding:40px;
  text-align:center;
  top:50px;
}

.main__title {
    position: relative;
    text-align: center;
    width: 600px;
    margin: 0 auto;
}
</style>