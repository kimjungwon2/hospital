<template>
  <div v-if="staffHosInfoId===null">
        등록된 추가 정보가 없습니다. 
        <br>추가 정보 등록하기<font-awesome-icon icon="edit" @click.prevent="createHospital"/>
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
import { library } from '@fortawesome/fontawesome-svg-core';
import { faEdit } from '@fortawesome/free-solid-svg-icons';
import { faTrashAlt } from '@fortawesome/free-solid-svg-icons';

import {staffViewStaffHospitalInfo,staffDeleteStaffHosInfo} from '@/api/staff';

library.add(faEdit)
library.add(faTrashAlt)

export default {
   data() {
      return {
        hospitalId:'',
        staffHosInfoId:'',
        staffHosInfo:[],
        doctors:[],
      }
  },
  methods:{
    createHospital(){
      this.$router.push('/staff/register/'+this.hospitalId+'/staffHosInfo');
    },
    async deleteStaffHospitalInfo(){
      if(confirm('정말로 추가 정보를 삭제하시겠습니까?')){
          await staffDeleteStaffHosInfo(this.$store.getters.getMemberId,this.staffHosInfoId);
          this.loadDeleteStaffHosInfo();
        }
    },
    editStaffHospitalInfo(){
      this.$router.push('/admin/staffHospital/edit/'+this.staffHosInfoId);
    },
    loadDeleteStaffHosInfo(){
         this.staffHosInfoId = null;
         this.staffHosInfo = null;
         this.doctors = null;
         this.$router.push({name:'staffViewStaffViewHospital',
                query: {hospitaId:this.hospitalId, staffHosInfoId:this.staffHosInfoId}
        }); 
    }
  },
  async created(){
    this.staffHosInfoId = this.$route.query.staffHosInfoId;
    this.hospitalId = this.$route.query.hospitalId;

    if(this.staffHosInfoId!==null){
         const {data} = await staffViewStaffHospitalInfo(this.staffHosInfoId);
         this.staffHosInfo = data;
         this.doctors = data.doctors;
    }

  }

}
</script>

<style>

</style>