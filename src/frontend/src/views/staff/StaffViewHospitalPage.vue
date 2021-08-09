<template>
  <div>
    <div class="admin__hospital__categories">
        <button class="hospital__category__btn" @click.prevent="viewHospital"> 병원 정보 </button>
        <button class="hospital__category__btn" @click.prevent="viewStaffHospital"> 추가 병원 정보 </button>
    </div>

      <template v-if="isHospital"><StaffViewHospitalForm></StaffViewHospitalForm></template>
      <template v-if="isStaffHospital"><StaffViewStaffHosInfoForm></StaffViewStaffHosInfoForm></template>
  </div>
</template>

<script>
import StaffViewHospitalForm from '@/components/staff/hospital/StaffViewHospitalForm';
import StaffViewStaffHosInfoForm from '@/components/staff/hospital/StaffViewStaffHosInfoForm';

export default {
  data() {
    return {
      isHospital : true,
      isStaffHospital : false,
    }
  },
  methods:{
    viewHospital(){
      this.isHospital=true;
      this.isStaffHospital = false;
    },
    viewStaffHospital(){
      this.isHospital=false;
      this.isStaffHospital=true;
    }
  },
  components:{
    StaffViewHospitalForm,
    StaffViewStaffHosInfoForm,
  },
}
</script>

<style>

</style>