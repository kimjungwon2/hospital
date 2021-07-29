<template>
    <form @submit.prevent="submitForm">
        <div>
            <label for="memberIdName">회원 아이디(이메일):</label>
            <input id="memberIdName" type="text" v-model="memberIdName">
        </div>
        <div>
            <label for="password">비밀번호:</label>
            <input id="password" type="text" v-model="password">
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
        <button type="submit">회원가입</button>
        <p>{{ logMessage }}</p>
    </form>
</template>

<script>
import { signupUser } from '@/api/index'

export default {
    data() {
        return {
            memberIdName:'',
            password:'',
            userName:'',
            nickName:'',
            phoneNumber:'',
            //log
            logMessage:'',
        };
    },
    methods:{
        async submitForm(){
            const userData = {
                memberIdName: this.memberIdName,
                password: this.password,
                userName: this.userName,
                nickName: this.nickName,
                phoneNumber: this.phoneNumber,
            }
            await signupUser(userData);
            this.$alert("회원 가입이 완료되었습니다.")
            this.initForm();
            this.$router.push('/main').catch(error=>error);
        },
        initForm(){
            this.memberIdName = '';
            this.password = '';
            this.userName = '';
            this.nickName = '';
            this.phoneNumber = '';
        },
    }
};
</script>

<style>

</style>