<template>
  <div>
    <div class="admin__hospital__categories">
        <button class="hospital__category__btn" @click.prevent="viewHospital"> 병원 정보 </button>
        <button class="hospital__category__btn" @click.prevent="viewStaffHospital"> 추가 병원 정보 </button>
        <button class="hospital__category__btn" @click.prevent="viewImageHospital">병원 이미지 업로드</button>
    </div>

      <template v-if="isHospital"><AdminViewHospitalForm></AdminViewHospitalForm></template>
      <template v-if="isStaffHospital"><AdminViewStaffHospitalForm></AdminViewStaffHospitalForm></template>
      <template v-if="isImageHospital"><AdminViewImageHospitalForm></AdminViewImageHospitalForm></template>
  </div>
</template>

<script>
import AdminViewHospitalForm from '@/components/admin/hospital/AdminViewHospitalForm';
import AdminViewStaffHospitalForm from '@/components/admin/hospital/AdminViewStaffHospitalForm';
import AdminViewImageHospitalForm from '@/components/admin/hospital/AdminViewImageHospitalForm';

export default {
  data() {
    return {
      isHospital : true,
      isStaffHospital : false,
      isImageHospital : false,
    }
  },
  methods:{
    viewHospital(){
      this.isHospital=true;
      this.isStaffHospital = false;
      this.isImageHospital = false;
    },
    viewStaffHospital(){
      this.isHospital=false;
      this.isStaffHospital=true;
      this.isImageHospital = false;
    },
    viewImageHospital(){
      this.isHospital=false;
      this.isStaffHospital=false;
      this.isImageHospital = true;
    }
  },
  components:{
    AdminViewHospitalForm,
    AdminViewStaffHospitalForm,
    AdminViewImageHospitalForm,
  },
}
</script>

<style>

</style>