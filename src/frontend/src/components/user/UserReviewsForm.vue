<template>
  <div>
      <ul v-for="contentItem in contentItems" :key="contentItem.reviewId">
        <li @click="routeViewHospital(contentItem)">
            {{ contentItem.reviewHospitals[0].hospitalName }}
        </li>
       </ul>
  </div>
</template>

<script>
import {viewUserReviews} from '@/api/user';

export default {
    
    data() {
        return {
            contentItems:[],
        };
    },
    methods:{
        async userReviews(){
            const id = this.$route.params.id;
            const { data } = await viewUserReviews(id);
            this.contentItems = data;
        },

        routeViewHospital(contentItem){
            const id = contentItem.reviewHospitals[0].hospitalId;
            this.$router.push(`/hospital/view/${id}`);
        },
    },
    created(){
        this.userReviews();
    },

};
</script>

<style>

</style>