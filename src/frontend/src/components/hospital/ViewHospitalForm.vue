<template>
  <div>
      <ul>
          제목: {{hospitalName}} | 발급일: {{licensingDate | formatYear}}
          리뷰 개수: {{reviewCount}}
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
            
            reviewCount: '',
            tags:[],
            hospitalEstimations:[],
        };
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
        this.reviewCount = data.hospitalReviews[0].reviewCount;

        data.hospitalTags[0].tagName;
        

    },
}
</script>

<style>

</style>