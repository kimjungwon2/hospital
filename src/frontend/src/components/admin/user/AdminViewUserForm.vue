<template>
  <div>
    <br>
    번호 : {{member.id}}<br>
    아이디 : {{member.memberIdName}}<br>
    유저 이름 : {{member.userName}}<br>
    닉네임 : {{member.nickName}}<br>
    전화번호 : {{member.phoneNumber}}<br>
    권한 : {{member.memberStatus}}<br>
    <p v-if="member.memberStatus=='STAFF'">
      관리 병원 번호: {{member.hospitalNumber}}
    </p>
  </div>
  
</template>

<script>
import {adminViewMember} from '@/api/admin';

export default {

    data() {
        return {
            member: [],
        }
    },
    async created(){
        const memberId = this.$route.params.userId;
        const {data} = await adminViewMember(memberId);
        this.member = data;
    },
}
</script>

<style>

</style>