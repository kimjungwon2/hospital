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
            회원 권한: {{member.memberStatus}}
        </div>
        <button type="submit">수정하기</button>
        <p>{{ logMessage }}</p>
    </form>
</template>

<script>
import {viewUserInformation,modifyUserInformation} from '@/api/user';

export default {
    data() {
        return {
            userName:'',
            nickName:'',
            phoneNumber:'',
            //log
            logMessage:'',
            member:{},
        }
    },
    methods:{
        async submitForm(){
            const userData = {
                userName: this.userName,
                nickName: this.nickName,
                phoneNumber: this.phoneNumber,
            }
            await modifyUserInformation(this.$route.params.userId, userData);
            this.$alert("회원 수정이 정상적으로 이루어졌습니다.")
            this.$router.push('/main');
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
        const {data} = await viewUserInformation(memberId);
        this.member = data;
        this.userName = data.userName;
        this.nickName = data.nickName;
        this.phoneNumber = data.phoneNumber;
    },
}
</script>

<style>

</style>