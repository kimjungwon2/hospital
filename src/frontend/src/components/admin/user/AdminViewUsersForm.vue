<template>

<div>
    <ul v-for="user in users.content" :key="user.memberId" >
      <div>
        <li @click="routeAdminViewUser(user.memberId)">멤버 번호: {{ user.memberId }} </li>
        <li>병원 아이디: {{ user.memberIdName}}</li>
        <li>유저 이름: {{ user.userName }}</li>
        <li>유저 상태: {{ user.memberStatus}}</li>
        <li v-if="isStaff(user.memberStatus)"> 병원 관리 번호: {{ user.hospitalNumber}}</li>
        <div v-if="isNormal(user.memberStatus)"><font-awesome-icon icon="hospital-user" @click="authorityMember(user.memberId)"/></div>
        <div><font-awesome-icon icon="trash-alt" @click="deleteMember(user.memberId)"/></div>
      </div>

  </ul>
</div>

</template>

<script>
import { library } from '@fortawesome/fontawesome-svg-core';
import { faTrashAlt } from '@fortawesome/free-solid-svg-icons';
import { faHospitalUser } from '@fortawesome/free-solid-svg-icons';
import { adminViewMemberLists, adminAuthorizeMember, adminDeleteMember } from '@/api/admin';

library.add(faTrashAlt)
library.add(faHospitalUser)

export default {
   data() {
     return {
       users: []
     }
   },
   methods:{
    isStaff(data){
      if(data === "STAFF") return true;
      else return false;
    },
    isNormal(data){
      if(data === "NORMAL") return true;
      else return false;
    },
    routeAdminViewUser(memberId){
      this.$router.push('/admin/user/view/'+memberId)
    },
    async deleteMember(memberId){
      if(confirm('정말로 멤버를 삭제하시겠습니까?')){
            await adminDeleteMember(memberId);
            this.adminLoadMembers();
      }
    },
    async authorityMember(memberId){
      if(confirm('STAFF 권한을 주시겠습니까?')){
            const data = {
              memberStatus:"STAFF",
            };

            await adminAuthorizeMember(memberId, data);
            this.adminLoadMembers();
      }
    },
    async adminLoadMembers(){
        const {data} = await adminViewMemberLists();
        this.users = data;
    },
   },
   async created(){
        const {data} = await adminViewMemberLists();
        this.users = data;
    }
}
</script>

<style>

</style>