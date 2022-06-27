<template>
  <div>회원 보기
    <template v-if="isView"><AdminViewUserForm></AdminViewUserForm></template>
    <template v-if="isEdit"><AdminEditUserForm></AdminEditUserForm></template>
    <div v-if="isView"><font-awesome-icon icon="edit" @click.prevent="editUser"/></div>
  </div>
</template>

<script>
import AdminViewUserForm from '@/components/admin/user/AdminViewUserForm.vue'
import AdminEditUserForm from '@/components/admin/user/AdminEditUserForm.vue'
import { library } from '@fortawesome/fontawesome-svg-core';
import { faEdit } from '@fortawesome/free-solid-svg-icons';

library.add(faEdit)

export default {
  data() {
    return {
        isEdit: false,
        isView: true,
        memberId: '',
    }
  },
  components:{
    AdminViewUserForm,
    AdminEditUserForm,
  },
  methods:{
      editUser(){
        this.isEdit = true;
        this.isView = false;
      },
  },
}
</script>

<style>

</style>