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
      영업 시간: {{staffHosInfo.consultationHour}}<br>
      특이 사항: {{staffHosInfo.abnormality}}<br><br>
      <div v-if="doctors.length!==0" >
        <p v-for="(doctor, i) in doctors" :key="doctor.doctorId">
            의사 ({{i+1}})<br>
            의사 번호: {{doctor.doctorId}}<br>
            이름: {{doctor.name}}<br>
            경력: {{doctor.history}}<br>
        </p>
      </div>
      <div v-else>
        <h4>의사가 등록되지 않았습니다.</h4>
      </div>
      <br>
      수정하기<font-awesome-icon icon="edit" @click.prevent="editStaffHospitalInfo"/><br>
      삭제하기<font-awesome-icon icon="trash-alt" @click.prevent="deleteStaffHospitalInfo"/>
  </div>
</template>

<script>
import AdminRegisterStaffHospitalForm from '@/components/admin/hospital/AdminRegisterStaffHospitalForm.vue';
import { library } from '@fortawesome/fontawesome-svg-core';
import { faEdit } from '@fortawesome/free-solid-svg-icons';
import { faTrashAlt } from '@fortawesome/free-solid-svg-icons';

import {adminViewStaffHospitalInfo,adminDeleteStaffHosInfo} from '@/api/admin'

library.add(faEdit)
library.add(faTrashAlt)

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
    async deleteStaffHospitalInfo(){
      if(confirm('정말로 추가 정보를 삭제하시겠습니까?')){
          await adminDeleteStaffHosInfo(this.staffHosInfoId);
          this.isCreate=false;
          this.$router.push('/admin/hospitals');
        }
    },
    editStaffHospitalInfo(){
      this.$router.push('/admin/staffHospital/edit/'+this.staffHosInfoId);
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