<template>
    <li>
        <div @click="routeViewHospital">
          {{ contentItem.hospitalName}}
        </div>
        <div>
          {{ contentItem.businessCondition }}
        </div>
        <div>
          {{ contentItem.medicalSubjectInformation }}
        </div>
        <div>
          {{ contentItem.roadBaseAddress }}
        </div>
        <div v-if= "contentItem.postTagDtos !== null" >
            태그: <p v-for="tag in contentItem.postTagDtos" :key="tag.tagId">
                {{tag.tagName}}
            </p>
        </div>
        <div v-if= "contentItem.reviewHospitals !== null">
            리뷰 평가 : {{contentItem.reviewHospitals[0].averageRate}} & 등록된 리뷰 개수 : {{ contentItem.reviewHospitals[0].reviewCount }}
        </div>
    </li>
  
</template>

<script>
import {viewHospital} from '@/api/hospital';

export default {
  props: {
    contentItem: {
      type:Object,
      required: true,
    },
  },
  methods:{
    async viewHospital(){
      const {data} = await viewHospital(this.contentItem.hospitalId);
      console.log(data);
    },
    routeViewHospital(){
        const id = this.contentItem.hospitalId;
        this.$router.push(`/hospital/view/${id}`);
    },
  },
};
</script>

<style>

</style>