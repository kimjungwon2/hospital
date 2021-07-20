<template>
  <div>
      <ul>
          제목: {{hospitalName}} | 발급일: {{licensingDate | formatYear}}

          <div v-if= "countReviews !== 0">
              리뷰 개수:{{hospitalReviews[0].reviewCount}}
          </div>
          <div v-if= "countTags !== 0">
              태그: {{hospitalTags}}
          </div>
      </ul>
  </div>
</template>

<script>
import {viewHospital} from '@/api/hospital';

export default {
    data() {
        return {
            hospitalName:'',
            licensingDate:'',
            phoneNumber:'',
            distinguishedName:'',
            medicalSubjectInformation:'',
            businessCondition:'',
            cityName:'',
            landLotBaseSystem:'',
            roadBaseAddress:'',
            numberHealthcareProvider:'',
            numberWard:'',
            numberPatientRoom:'',
            x_coordination:'',
            y_coordination:'',
            latitude: '',
            longitude: '',
            
            hospitalReviews: [],
            countReviews:0,

            tags:[],
            countTags:0,
            hospitalTags:'',

            hospitalEstimations:[],
        };
    },
    methods:{
        createTags(){
            for(let tag in this.tags){
                this.hospitalTags += ` #${this.tags[tag].tagName}`;
            }
        }
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

        //값 배치
        this.hospitalName = data.hospitalName;
        this.licensingDate = data.licensingDate;
        this.phoneNumber = data.phoneNumber
        this.distinguishedName = data.distinguishedName
        this.medicalSubjectInformation = data.medicalSubjectInformation
        this.businessCondition = data.businessCondition
        this.cityName = data.cityName
        this.landLotBaseSystem = data.landLotBaseSystem
        this.roadBaseAddress = data.roadBaseAddress
        this.numberHealthcareProvider = data.numberHealthcareProvider
        this.numberWard = data.numberWard
        this.numberPatientRoom = data.numberPatientRoom
        this.x_coordination = data.x_coordination
        this.y_coordination = data.y_coordination
        this.latitude = data.latitude
        this.longitude = data.longitude

        //List object
        this.hospitalReviews = data.hospitalReviews;
        this.countReviews = this.hospitalReviews.length;

        this.tags = data.hospitalTags;
        this.countTags = this.tags.length;
        this.createTags();
    },
}
</script>

<style>

</style>