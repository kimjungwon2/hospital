<template>
  <div v-if="staffHosInfoId===null">
      <template v-if="isCreate===false">
        등록된 추가 정보가 없습니다. 
        <br>추가 정보 등록하기<font-awesome-icon icon="edit" @click.prevent="createHospital"/>
      </template>
      <template v-if="isCreate"><AdminRegisterStaffHospitalForm @event="getChild"></AdminRegisterStaffHospitalForm></template>
  </div>
  <div v-else>
      병원 정보:{{staffHosInfoId}}
  </div>
</template>

<script>
import AdminRegisterStaffHospitalForm from '@/components/admin/hospital/AdminRegisterStaffHospitalForm.vue';
import { library } from '@fortawesome/fontawesome-svg-core';
import { faEdit } from '@fortawesome/free-solid-svg-icons';

library.add(faEdit)

export default {
  components:{
    AdminRegisterStaffHospitalForm,
  },
   data() {
      return {
        isCreate:false,
        staffHosInfoId:'',
      }
  },
  methods:{
    createHospital(){
      this.isCreate=true;
    },
    getChild(isCreate){
      this.isCreate = isCreate;
    }
  },
  created(){
    this.staffHosInfoId = this.$route.query.staffHosInfoId;
  }

}
</script>

<style>

</style>