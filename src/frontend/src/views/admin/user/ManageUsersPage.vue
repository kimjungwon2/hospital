<template>
  <div>
      <AdminAddUserForm></AdminAddUserForm>
      <h1>유저 관리 페이지</h1>
      <AdminSearchUsersForm></AdminSearchUsersForm>
  </div>
</template>

<script>
import AdminSearchUsersForm from '@/components/admin/user/AdminSearchUsersForm.vue'
import AdminAddUserForm from '@/components/admin/user/AdminAddUserForm.vue'
export default {
  components:{
    AdminSearchUsersForm,
    AdminAddUserForm,
  },
}
</script>

<style>

</style>