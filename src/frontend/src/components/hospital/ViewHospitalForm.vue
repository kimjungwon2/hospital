<template>
  <div>
      <ViewMapForm v-if= "this.hospital.detailedHosId !==null"
      :detailed="this.detailed"
      >
      </ViewMapForm>
      <h1>병원 정보</h1>

            제목: {{hospital.hospitalName}} | 발급일: {{licensingDate | formatYear}}

              <div v-if= "countReview !== 0">
                  리뷰 개수: {{countReview}}
               </div>

               <div v-if= "countTags !== 0">
                 태그: {{hospitalTags}}
               </div>
  </div>
</template>

<script>
import {viewHospital} from '@/api/hospital';
import ViewMapForm from '@/components/hospital/ViewMapForm.vue';

export default {
    data() {
        return {
            hospital:[],
            licensingDate:'',

            countReview:0,

            tags:[],
            countTags:0,
            hospitalTags:'',

            hospitalEstimations:[],
            countEstimations:0,
            
            //부모에게 전달할 추가 병원 정보 ID
            detailed:{},
            staffHosInfoId:'',
        };
    },
    components:{
        ViewMapForm,
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
        this.countReview = this.hospital.hospitalReviewCount;

        //태그 생성
        this.tags = data.hospitalTags;
        this.countTags = this.tags.length;
        this.createTags();

        this.detailed = {
            landLotBasedSystem: this.hospital.landLotBasedSystem,
            latitude: this.hospital.latitude,
            longitude: this.hospital.longitude
        };

        //스태프 정보 부모 컴포넌트에 전달
        this.staffHosInfoId = this.hospital.staffHosInfoId;
        this.$emit("child-event", this.staffHosInfoId);
    },
}
</script>

<style>

</style>