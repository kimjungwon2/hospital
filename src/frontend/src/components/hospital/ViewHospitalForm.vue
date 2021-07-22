<template>
  <div>
      <h1>병원 정보</h1>

            제목: {{hospital.hospitalName}} | 발급일: {{licensingDate | formatYear}}

              <div v-if= "countReviews !== 0">
                  리뷰 개수: {{hospitalReviews[0].reviewCount}}
               </div>

               <div v-if= "countTags !== 0">
                 태그: {{hospitalTags}}
               </div>
  </div>
</template>

<script>
import {viewHospital} from '@/api/hospital';

export default {
    data() {
        return {
            hospital:[],
            licensingDate:'',
            
            hospitalReviews: [],
            countReviews:0,

            tags:[],
            countTags:0,
            hospitalTags:'',

            hospitalEstimations:[],
            countEstimations:0,
            
            //부모에게 전달할 추가 병원 정보 ID
            staffHosInfoId:'',
            detailedHosInfo:{},
        };
    },
    methods:{
        createTags(){
            for(let tag in this.tags){
                this.hospitalTags += `#${this.tags[tag].tagName}`;
            }
        },
    },
    filters:{
        formatYear(value){
            let Year = value.substring(0,4);
            let Month = value.substring(4,6);
            let Date = value.substring(6,8);
            
            return `${Year}-${Month}-${Date}`; 
        },
    },
    async created(){    
        const id = this.$route.params.id;
        const {data} = await viewHospital(id);

        this.hospital = data;
        this.licensingDate = data.licensingDate;

        //List object
        this.hospitalReviews = data.hospitalReviews;
        this.countReviews = this.hospitalReviews.length;

        //태그 생성
        this.tags = data.hospitalTags;
        this.countTags = this.tags.length;
        this.createTags();

        //스태프 정보 부모 컴포넌트에 전달.
        this.staffHosInfoId = this.hospital.staffHosInfoId;

        this.detailedHosInfo.staffHosInfoId = this.hospital.staffHosInfoId;
        this.detailedHosInfo.landLotBasedSystem = this.hospital.landLotBasedSystem;
        this.detailedHosInfo.detailedHosId = this.hospital.detailedHosId;
        this.detailedHosInfo.latitude = this.hospital.latitude;
        this.detailedHosInfo.longitude = this.hospital.longitude;

        this.$emit("child-event",this.detailedHosInfo);
    },
}
</script>

<style>

</style>