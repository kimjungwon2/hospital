<template>
  <div>
    <form @submit.prevent="submitForm">
            <select name="searchCondition" v-model="searchCondition">
                  <option value="allSearch" selected>모두 검색</option>
                  <option value="memberId">멤버 번호</option>
                  <option value="memberIdName" >아이디</option>
                  <option value="userName">이름</option>
                  <option value="nickName">닉네임</option>
                  <option value="phoneNumber">전화번호</option>
                  <option value="authorizationStatus">권한 등급</option>
                  <option value="hospitalNumber">병원 번호</option>
            </select>
        <input id="keyword" type="text" v-model="keyword"/><button type="submit">검색하기</button>
	</form>

    <ul v-for="user in users.content" :key="user.memberId" >
      <div>
        <li @click="routeAdminViewUser(user.memberId)">멤버 번호: {{ user.memberId }} </li>
        <li>병원 아이디: {{ user.memberIdName}}</li>
        <li>유저 이름: {{ user.userName }}</li>
        <li>유저 상태: {{ user.memberStatus}}</li>
        <li v-if="isStaff(user.memberStatus)"> 병원 관리 번호: {{ user.hospitalNumber}}</li>
        <div><font-awesome-icon icon="trash-alt" @click.prevent="deleteMember(user.memberId)"/></div>
      </div>
    </ul>

    <div>
      <button :disabled="pageNum === 0" @click.prevent="prevPage">
        이전
      </button>
      <span>{{ pageNum + 1 }} / {{ totalPageNum }} 페이지</span>
      <button :disabled="pageNum >= totalPageNum - 1" @click.prevent="nextPage">
        다음
      </button>
    </div>
  </div>
</template>

<script>
import { library } from '@fortawesome/fontawesome-svg-core';
import { faTrashAlt } from '@fortawesome/free-solid-svg-icons';

import { adminSearchMemberLists, adminDeleteMember } from '@/api/admin';

library.add(faTrashAlt)

export default {
   data() {
     return {
       //데이터  
       pageNum: 0,
       totalPageNum:'',

       //유저 데이터
       users: [],
       //검색 조건 default = allSearch로 한다. 
       searchCondition:'allSearch',
       //검색명
       keyword:'',
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
    //멤버 삭제
    async deleteMember(memberId){
      if(confirm('정말로 멤버를 삭제하시겠습니까?')){
            await adminDeleteMember(memberId);
            this.adminLoadMembers();
      }
    },
    
    async adminLoadMembers(){
        const condition ={};
        let key = this.searchCondition;
        let page = "page";
        condition[key] = this.keyword;
        condition[page] = this.pageNum;

        const {data} = await adminSearchMemberLists(condition);
        this.users = data;
    },
    
    //검색
    async submitForm(){
        this.pageNum = 0;
        const obj ={};
        let key = this.searchCondition;
        obj[key] = this.keyword;

        const {data} = await adminSearchMemberLists(obj);
        this.users = data;
        this.totalPageNum = this.users.totalPages;
    },
    //이전 페이지
    async prevPage(){
        this.pageNum-=1;
        
        const obj ={};
        let key = this.searchCondition;
        let page = "page";
        //검색
        obj[key] = this.keyword;
        //페이지 번호
        obj[page] = this.pageNum;

        const {data} = await adminSearchMemberLists(obj);
        this.users = data;
    },
    //다음 페이지
    async nextPage(){
        this.pageNum+=1;

        const obj ={};
        let key = this.searchCondition;
        let page = "page";
        obj[key] = this.keyword;
        obj[page] = this.pageNum;

        const {data} = await adminSearchMemberLists(obj);
        this.users = data;
    }
   },

   async created(){
       const condition ={
            page: 0,
       }
        const {data} = await adminSearchMemberLists(condition);
        this.users = data;
        this.totalPageNum = this.users.totalPages;
    }
}
</script>

<style>

</style>