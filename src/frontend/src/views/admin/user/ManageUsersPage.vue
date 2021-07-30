<template>
  <div>
      <h1>유저 관리 페이지</h1>
      <AdminViewUsersForm></AdminViewUsersForm>
      <AdminAddUserForm></AdminAddUserForm>
  </div>
</template>

<script>
import AdminViewUsersForm from '@/components/admin/user/AdminViewUsersForm.vue'
import AdminAddUserForm from '@/components/admin/user/AdminAddUserForm.vue'
export default {
  components:{
    AdminViewUsersForm,
    AdminAddUserForm,
  },
}
</script>

<style>

</style>