<template>
    <div class="container" @click="routeViewHospital">
        <!-- 이미지 부분-->
        <span class= "inner-image" v-if="contentItem.imageKey!=null">
          <img :src='`http://d123wf46onsgyf.cloudfront.net/w140/${contentItem.imageKey}`'/>
        </span>

        <span class= "inner-image" v-else>
          <img src='@/assets/noImage.gif'>
        </span>


        <span class="inner-info"> 
            <h3>{{ contentItem.hospitalName}}</h3>
              영업 상태: <b>{{ contentItem.businessCondition }}</b> 
              <br>진료 과목: {{ contentItem.medicalSubjectInformation }}
              <p class="item item3" v-if="contentItem.roadBaseAddress!==null">
                주소: {{ contentItem.roadBaseAddress }}
              </p>
    
              <p class="item item4" v-if= "contentItem.postTagDtos !== null" >
                태그: {{tags}}
              </p>
    
              <p class="item item4" v-else>
                등록된 태그가 없습니다.
              </p>
    
            <p class="item item5" v-if= "contentItem.reviewHospitals !== null">
                리뷰 평가 : {{contentItem.reviewHospitals[0].averageRate}} & 등록된 리뷰 개수 : {{ contentItem.reviewHospitals[0].reviewCount }}
            </p>
            <p class="item item5" v-else>
              등록된 리뷰가 없습니다.
            </p>
        </span>

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
img{
  width:130px;
  height:130px;
}

.container{
  height:250px;
  display:block;
  border: 5px solid black;
}

.inner-image{
  position:relative;
  height:50%;
  height:130px;
  width:130px;
}

.inner-info{
  position:relative;
  bottom:140px;
  left:135px;
  height:100%;
}

</style>