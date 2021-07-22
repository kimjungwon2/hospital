<template>
  <ul>
      <li @click="routeBookmark">즐겨찾기 병원</li>
      <li @click="routeAppointment">나의 예약 목록</li>
      <li @click="routeQuestion">내가 등록한 Q&A</li>
      <li @click="routeReview">내가 등록한 리뷰</li>
  </ul>
</template>

<script>
export default {
    methods:{
        routeBookmark(){
            const id = 1;
            this.$router.push(`/user/${id}/bookmarks`);
        },
        routeAppointment(){
            const id = 1;
            this.$router.push(`/user/${id}/appointments`);
        },
        routeQuestion(){
            const id = 1;
            this.$router.push(`/user/${id}/questions`);
        },
        routeReview(){
            const id = 1;
            this.$router.push(`/user/${id}/reviews`);
        },
    },

}
</script>

<style>

</style>