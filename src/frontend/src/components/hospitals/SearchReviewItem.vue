<template>
  <div>
    <div class="container" @click="routeViewHospital(contentItem.reviewHospitals[0].hospitalId)">
        <div class="item item1">
          병원 이름: {{ contentItem.reviewHospitals[0].hospitalName}}
        </div>
        <div class="item item2" v-if="contentItem.reviewHospitals[0].authenticationStatus===null">
          인증 상태: {{ contentItem.reviewHospitals[0].authenticationStatus}}
        </div>
        <div class="item item2" v-else>
          인증되지 않은 리뷰.
        </div>
        <div class="item item3">
          진료 병명: {{ contentItem.reviewHospitals[0].disease }} | 리뷰 등록 날짜: {{ contentItem.createdDate|formatDate }}
        </div>
        <div class="item item4">
            등록자: {{ contentItem.nickName }}
        </div>
        <div class="item item4">
          리뷰 내용: {{ contentItem.reviewHospitals[0].content }}
        </div>
        <div class="item item5">
          <font-awesome-icon :icon="['far', 'heart']"/>{{contentItem.reviewLikes.length}} | 리뷰 평균 평점: {{ contentItem.reviewHospitals[0].averageRate }}
        </div>
    </div>
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

</style>