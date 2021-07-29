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
        <p>{{logMessage}}</p>
    </form>
</template>

<script>
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
                
               await this.$store.dispatch('login',userData);

               this.$router.push('/main').catch(error=>error);
            } catch (error) {
                this.logMessage = error.response.data;
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