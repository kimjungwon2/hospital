<template>
  <div>
    번호 : {{member.id}}
    아이디 : {{member.memberId}}
    유저 이름 : {{member.userName}}
    닉네임 : {{member.nickName}}
    전화번호 : {{member.phoneNumber}}
    권한 : {{member.memberStatus}}
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