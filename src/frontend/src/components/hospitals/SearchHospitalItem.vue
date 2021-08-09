<template>
    <div class="container" @click="routeViewHospital">
        <div class="item item1">
          {{ contentItem.hospitalName}}
        </div>
        <div class="item item2">
          <b>{{ contentItem.businessCondition }}</b> 진료 과목: {{ contentItem.medicalSubjectInformation }}
        </div>
        <div class="item item3" v-if="contentItem.roadBaseAddress!==null">
          {{ contentItem.roadBaseAddress }}
        </div>
        <div class="item item4" v-if= "contentItem.postTagDtos !== null" >
            태그: {{tags}}
        </div>
        <div class="item item4" v-else>
          등록된 태그가 없습니다.
        </div>
        <div class="item item5" v-if= "contentItem.reviewHospitals !== null">
            리뷰 평가 : {{contentItem.reviewHospitals[0].averageRate}} & 등록된 리뷰 개수 : {{ contentItem.reviewHospitals[0].reviewCount }}
        </div>
        <div class="item item5" v-else>
          등록된 리뷰가 없습니다.
        </div>
    </div>
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
.container{
  width:100%;
  height:100px;
  
}

.item.item2 b{
  border: 1px solid black;
  border-radius: 4px;
  font-size:regular;
}

.item.item5 {
  padding: 0 0 10px 0;
  border-bottom:1px solid silver;
}

</style>