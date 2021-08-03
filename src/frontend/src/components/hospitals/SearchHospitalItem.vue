<template>
    <li>
        <h3 @click="routeViewHospital">
          {{ contentItem.hospitalName}}
        </h3>
        <div>
          {{ contentItem.businessCondition }} 진료과목: {{ contentItem.medicalSubjectInformation }}
        </div>
        <div>
          {{ contentItem.roadBaseAddress }}
        </div>
        <div v-if= "contentItem.postTagDtos !== null" >
            태그: {{tags}}
        </div>
        <div v-if= "contentItem.reviewHospitals !== null">
            리뷰 평가 : {{contentItem.reviewHospitals[0].averageRate}} & 등록된 리뷰 개수 : {{ contentItem.reviewHospitals[0].reviewCount }}
        </div>
    </li>
  
</template>

<script>

export default {
  props: {
    contentItem: {
      type:Object,
      required: true,
    },
  },
  data(){
    return{
      tags:'',
    };
  },
  methods:{
    routeViewHospital(){
        const id = this.contentItem.hospitalId;
        this.$router.push(`/hospital/view/${id}`);
    },
    createTags(){
        for(let tag in this.contentItem.postTagDtos){
          this.tags += `#${this.contentItem.postTagDtos[tag].tagName}`;
       }
    },
  },
  created(){
    this.createTags();
  }
};
</script>

<style>

</style>