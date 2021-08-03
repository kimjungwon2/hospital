<template>
  <div>
      <h1 class="mainFont">경기도 내 모든 병원</h1>
      <SearchForm></SearchForm>
  </div>
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
.mainFont {
    position: relative;
    text-align: center;
    width: 600px;
    margin: 0 auto;
}
</style>