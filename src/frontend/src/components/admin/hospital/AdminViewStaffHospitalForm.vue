<template>
  <div v-if="staffHosInfoId===null">
      <template v-if="isCreate===false">
        등록된 추가 정보가 없습니다. 
        <br>추가 정보 등록하기<font-awesome-icon icon="edit" @click.prevent="createHospital"/>
      </template>
      <template v-if="isCreate"><AdminRegisterStaffHospitalForm></AdminRegisterStaffHospitalForm></template>
  </div>
  <div v-else>
      추가 정보 ID: {{staffHosInfoId}}<br>
      병원 소개: {{staffHosInfo.introduction}}<br>
      특이 사항: {{staffHosInfo.abnormality}}<br>
      <div v-if="doctors.length!==0">
         doctorId: {{doctors.doctorId}}<br>
         이름: {{doctors.name}}<br>
         경력: {{doctors.history}}<br>
      </div>
      <div v-else>
        <h4>의사가 등록되지 않았습니다.</h4>
      </div>
  </div>
</template>

<script>
import AdminRegisterStaffHospitalForm from '@/components/admin/hospital/AdminRegisterStaffHospitalForm.vue';
import { library } from '@fortawesome/fontawesome-svg-core';
import { faEdit } from '@fortawesome/free-solid-svg-icons';
import {adminViewStaffHospitalInfo} from '@/api/admin'

library.add(faEdit)

export default {
  components:{
    AdminRegisterStaffHospitalForm,
  },
   data() {
      return {
        isCreate:false,
        staffHosInfoId:'',
        staffHosInfo:[],
        doctors:[],
      }
  },
  methods:{
    createHospital(){
      this.isCreate=true;
    },
  },
  async created(){
    this.staffHosInfoId = this.$route.query.staffHosInfoId;
    if(this.staffHosInfoId!==null){
         const {data} = await adminViewStaffHospitalInfo(this.staffHosInfoId);
         this.staffHosInfo = data;
         this.doctors = data.doctors;
    }
  }

}
</script>

<style>

</style>