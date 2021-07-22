<template>
  <div>
      <h1>예약 목록</h1>
      <UserAppointmentsForm></UserAppointmentsForm>
  </div>
</template>

<script>
import UserAppointmentsForm from '@/components/user/UserAppointmentForm.vue'


export default {
    components:{
        UserAppointmentsForm,
    },
};
</script>

<style>

</style>