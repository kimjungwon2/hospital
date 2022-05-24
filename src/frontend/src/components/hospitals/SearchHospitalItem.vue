<template>
    <div class="searchHospital__hospital" @click="routeViewHospital">
        <!-- 이미지 부분-->
        <div class= "hospital__image" v-if="contentItem.imageKey!=null">
          <img alt="thumbnail" class="image__thumbnail" :src='`http://d123wf46onsgyf.cloudfront.net/w140/${contentItem.imageKey}`'/>
        </div>

        <div class= "hospital__image" v-else>
          <img alt="thumbnail" class="image__thumbnail" src='@/assets/noImage.gif'>
        </div>

        <div class="hospital__info"> 
            <h3>{{ contentItem.hospitalName}}</h3>
              영업 상태: <b>{{ contentItem.businessCondition }}</b> 
              <br><br>진료 과목: {{ contentItem.medicalSubjectInformation }}
              <p class="item item3" v-if="contentItem.roadBaseAddress!==null">
                주소: {{ contentItem.roadBaseAddress }}
              </p>
    
              <p class="item item4" v-if= "contentItem.postTagDtos !== null" >
                태그: {{tags}}
              </p>
    
              <p class="item item4" v-else>
                태그: 없음
              </p>
    
            <p class="item item5" v-if= "contentItem.reviewHospitals !== null">
                리뷰 평가 : {{contentItem.reviewHospitals[0].averageRate}} | 등록된 리뷰 개수 : {{ contentItem.reviewHospitals[0].reviewCount }}
            </p>
            <p class="item item5" v-else>
              리뷰: 없음
            </p>
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
.image__thumbnail{
  position:relative;
  top:20%;
  width:130px;
  height:130px;
  border-radius: 50%;
}

.searchHospital__hospital{
  display:flex;
  border: 2px solid #0067A3;
  border-radius: 10px;
  margin-bottom:12px;
}

.hospital__image{
  width:130px;
  line-height:130px;
}

.hospital__info{
  position:relative;
  margin-left:10px;
}

</style>