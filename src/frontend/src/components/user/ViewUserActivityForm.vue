<template>
  <ul>
      <li @click="routeBookmark">즐겨찾기 병원</li>
      <li @click="routeQuestion">내가 등록한 Q&A</li>
      <li @click="routeReview">내가 등록한 리뷰</li>
  </ul>
</template>

<script>
export default {
    methods:{
        routeBookmark(){
            const id = this.memberId;
            this.$router.push(`/user/${id}/bookmarks`);
        },
        routeQuestion(){
            const id = this.memberId;
            this.$router.push(`/user/${id}/questions`);
        },
        routeReview(){
            const id = this.memberId;
            this.$router.push(`/user/${id}/reviews`);
        },
    },
    data() {
        return {
            memberId: this.$store.getters.getMemberId
        }
    },
}
</script>

<style>

</style>