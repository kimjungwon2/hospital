<template>
  <div class="searchReview__hospital" @click="routeViewHospital(contentItem.reviewHospitals[0].hospitalId)">
      <p class="hospital__review">
        병원 이름: {{ contentItem.reviewHospitals[0].hospitalName}} <br>등록자: {{ contentItem.nickName }}  | 작성 날짜: {{ contentItem.createdDate|formatDate }}
      </p>
      <p class="hospital__review" v-if="contentItem.authenticationStatus!==null">
        인증 상태: {{ contentItem.authenticationStatus}}
      </p>
      <p class="hospital__review" v-else>
        인증 상태: 미인증.
      </p>
      <p class="hospital__review">
        진료 병명: {{ contentItem.reviewHospitals[0].disease }}<br>
        리뷰 내용: {{ contentItem.reviewHospitals[0].content }}
      </p>

      <p class="hospital__review" v-if = "contentItem.reviewLikes !=null">
        <font-awesome-icon :icon="['far', 'heart']"/>{{contentItem.reviewLikes.length}} | 리뷰 평균 평점: {{ contentItem.reviewHospitals[0].averageRate }}
      </p>
      <p class="hospital__review" v-else>
        리뷰 평균 평점: {{ contentItem.reviewHospitals[0].averageRate }} | <font-awesome-icon :icon="['far', 'heart']"/>{{0}} 
      </p>
  </div>
</template>

<script>
import { library } from '@fortawesome/fontawesome-svg-core';
import { faHeart as faRegularHeart } from '@fortawesome/free-regular-svg-icons';

library.add(faRegularHeart);

export default {
  props: {
    contentItem: {
      type:Object,
      required: true,
    },
  },
  methods:{
    routeViewHospital(hospitalId){
        this.$router.push(`/hospital/view/${hospitalId}`);
    },
  },
}
</script>

<style>
.searchReview__hospital{
  border: 2px solid #0067A3;
  border-radius: 10px;
  margin-bottom:12px;
  text-align:center;
}

.hospital__review{
  margin:auto;
  margin-left:10px;
}

</style>