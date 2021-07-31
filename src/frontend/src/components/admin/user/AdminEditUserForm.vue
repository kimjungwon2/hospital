<template>

    <form @submit.prevent="submitForm">
        <div>
            회원 번호: {{member.id}}
        </div>
        <div>
            회원 아이디(이메일): {{member.memberIdName}}
        </div>
        <div>
            <label for="userName">이름:</label>
            <input id="userName" type="text" v-model="userName">
        </div>
        <div>
            <label for="nickName">닉네임:</label>
            <input id="nickName" type="text" v-model="nickName">
        </div>
        <div>
            <label for="phoneNumber">전화번호:</label>
            <input id="phoneNumber" type="text" v-model="phoneNumber">
        </div>
        <div>
            <select name="memberStatus" v-model="memberStatus">
                  <option value="ADMIN" v-if="isAdmin" >ADMIN</option>
                  <option value="" v-if="isNormal">멤버 권한 선택</option>
                  <option value="NORMAL" v-if="isNormal">NORMAL</option>
                  <option value="STAFF" v-if="isNormal">STAFF</option>
            </select>
        </div>
        <button type="submit">수정하기</button>
        <p>{{ logMessage }}</p>
    </form>

</template>

<script>
import {adminViewMember,adminModifyMember} from '@/api/admin';

export default {
    data() {
        return {
            userName:'',
            nickName:'',
            phoneNumber:'',
            memberStatus:'',
            //log
            logMessage:'',
            member:{},
        }
    },
    computed:{
        isAdmin(){
            return this.confirmAdmin();
        },
        isNormal(){
            return this.confirmNormal();
        },
    },
    methods:{
        confirmAdmin(){
            if(this.memberStatus ==="ADMIN") return true;
            else false;
        },
        confirmNormal(){
            if(this.memberStatus !=="ADMIN") return true;
            else false;
        },
        async submitForm(){
            const userData = {
                userName: this.userName,
                nickName: this.nickName,
                phoneNumber: this.phoneNumber,
                memberStatus: this.memberStatus,
            }
            await adminModifyMember(this.$route.params.userId,userData);
            this.initForm();
            this.$alert("회원 수정이 정상적으로 이루어졌습니다.")
            this.$router.push('/admin/users').catch(error=>error);
        },
        initForm(){
            this.member = {};
            this.userName = '';
            this.nickName = '';
            this.phoneNumber = '';
        },
    },
    async created(){
        const memberId = this.$route.params.userId;
        const {data} = await adminViewMember(memberId);
        this.member = data;
        this.userName = data.userName;
        this.nickName = data.nickName;
        this.phoneNumber = data.phoneNumber;
        this.memberStatus = data.memberStatus;
    },
}
</script>

<style>

</style>