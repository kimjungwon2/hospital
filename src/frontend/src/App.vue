<template>
  <div id="app">
    <AppHeader></AppHeader>
    <router-view></router-view>
  </div>
</template>

<script>
import AppHeader from '@/components/common/AppHeader.vue';
export default {
  name:'App',
  components:{
    AppHeader,
  },
};
</script>

<style>
@import './css/base.css';
</style>