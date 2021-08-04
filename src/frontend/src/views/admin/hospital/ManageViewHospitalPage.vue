<template>
  <div>
    <div class="admin__hospital__categories">
        <button class="hospital__category__btn" @click.prevent="viewHospital"> 병원 정보 </button>
        <button class="hospital__category__btn" @click.prevent="viewStaffHospital"> 추가 병원 정보 </button>
    </div>

      <template v-if="isHospital"><AdminViewHospitalForm></AdminViewHospitalForm></template>
      <template v-if="isStaffHospital"><AdminViewStaffHospitalForm></AdminViewStaffHospitalForm></template>
  </div>
</template>

<script>
import AdminViewHospitalForm from '@/components/admin/hospital/AdminViewHospitalForm';
import AdminViewStaffHospitalForm from '@/components/admin/hospital/AdminViewStaffHospitalForm';

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
    AdminViewHospitalForm,
    AdminViewStaffHospitalForm,
  },
}
</script>

<style>

</style>