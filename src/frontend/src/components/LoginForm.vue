<template>
    <form @submit.prevent="submitForm">
        <div>
            <label for="memberIdName"></label>
            <input id="memberIdName" type="text" v-model="memberIdName"/>
        </div>
        <div>
            <label for="password"></label>
            <input id="password" type="text" v-model="password"/>
        </div>
        <button v-bind:disabled="!isMemberIdValid || !password" type="submit">로그인</button> 
        <p>{{ logMessage }}</p>
    </form>
</template>

<script>
import { loginUser } from '@/api/index';
import { validateEmail } from '@/utils/validation';

export default {
    data() {
        return {
            memberIdName: '',
            password:'',
            logMessage:'',
        }
    },
    computed:{
        isMemberIdValid(){
            return validateEmail(this.memberIdName);
        }
    },
    methods:{
        async submitForm(){
            try {
                const userData ={
                     memberIdName: this.memberIdName,
                     password: this.password,
                }; 
               const { data } = await loginUser(userData);
               this.$router.push('/main');
               this.logMessage=`${data.nickName}님 환영합니다.`
            } catch (error) {
                this.logMessage = error.reponse.data;
            } finally{
                this.initForm();
            }
        },
        initForm(){
            this.memberIdName = '';
            this.password = '';
        }
    },
};
</script>

<style>
</style>