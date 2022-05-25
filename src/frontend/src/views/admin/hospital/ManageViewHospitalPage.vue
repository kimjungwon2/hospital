<template>
  <div>
    <div class="admin__hospital__categories">
        <button class="hospital__category__btn" @click.prevent="viewHospital"> 병원 정보 </button>
        <button class="hospital__category__btn" @click.prevent="viewStaffHospital"> 추가 병원 정보 </button>
        <button class="hospital__category__btn" @click.prevent="viewThumbnailHospital">병원 섬네일 업로드</button>
        <button class="hospital__category__btn" @click.prevent="viewImageHospital">병원 이미지 업로드</button>
    </div>

      <template v-if="isHospital"><AdminViewHospitalForm></AdminViewHospitalForm></template>
      <template v-if="isStaffHospital"><AdminViewStaffHospitalForm></AdminViewStaffHospitalForm></template>
      <template v-if="isThumbnail"><AdminViewThumbnailForm></AdminViewThumbnailForm></template>
      <template v-if="isImageHospital"><AdminViewHospitalImageForm></AdminViewHospitalImageForm></template>
  </div>
</template>

<script>
import AdminViewHospitalForm from '@/components/admin/hospital/AdminViewHospitalForm';
import AdminViewStaffHospitalForm from '@/components/admin/hospital/AdminViewStaffHospitalForm';
import AdminViewThumbnailForm from '@/components/admin/hospital/AdminViewThumbnailForm';
import AdminViewHospitalImageForm from '@/components/admin/hospital/AdminViewHospitalImageForm';

export default {
  data() {
    return {
      isHospital : true,
      isStaffHospital : false,
      isThumbnail: false,
      isImageHospital : false,
    }
  },
  methods:{
    viewHospital(){
      this.isHospital=true;
      this.isStaffHospital = false;
      this.isThumbnail = false;
      this.isImageHospital = false;
    },
    viewStaffHospital(){
      this.isHospital=false;
      this.isStaffHospital=true;
      this.isThumbnail = false;
      this.isImageHospital = false;
    },
    viewThumbnailHospital(){
      this.isHospital=false;
      this.isStaffHospital=false;
      this.isThumbnail = true;
      this.isImageHospital = false;
    },
    viewImageHospital(){
      this.isHospital=false;
      this.isStaffHospital=false;
      this.isThumbnail = false;
      this.isImageHospital = true;
    }
  },
  components:{
    AdminViewHospitalForm,
    AdminViewStaffHospitalForm,
    AdminViewThumbnailForm,
    AdminViewHospitalImageForm,
  },
}
</script>

<style>

</style>