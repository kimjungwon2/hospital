<template>
    <div class="login_container">
        <h1>Login</h1>
        <form @submit.prevent="submitForm">
            <div class="login_input">
                <input id="memberIdName" type="text" required v-model="memberIdName"/>
                <span></span>
                <label for="memberIdName">Username</label>
            </div>
            <div class="login_input">
                <input id="password" type="password" required v-model="password"/>
                <span></span>
                <label for="password">Password</label>
            </div>
            <button v-bind:disabled="!isMemberIdValid || !password" type="submit">로그인</button> 
            <div class="signup_link">
                회원이 아니세요? <router-link class="link" to="/signup">Signup</router-link>
            </div>
        </form>
    </div>
</template>

<script>
import { validateEmail } from '@/utils/validation';
import { deleteCookie } from '@/utils/cookies';


export default {
    data() {
        return {
            memberIdName: '',
            password:'',
        }
    },
    computed:{
        isMemberIdValid(){
            return validateEmail(this.memberIdName);
        }
    },
    methods:{
        async submitForm(){
            //기존 로그인 계정이 있으면 로그인 정보 삭제.
            if(this.$store.getters.getMemberId !=='' ){
                deleteCookie('member_status');
                deleteCookie('nick_name');
                deleteCookie('token');
                deleteCookie('member_id');
                deleteCookie('no_answer_count');
                deleteCookie('review_count');
                deleteCookie('search_name');
                this.$store.commit('clearUserInfo');
                this.$alert('기존 로그인된 계정을 로그아웃 했습니다.');
                
                this.$router.push('/').catch(err => {
                if (
                    err.name !== 'NavigationDuplicated' &&
                    !err.message.includes('Avoided redundant navigation to current location')
                ) {alert(err);}}
                );
                    //새로고침하기
                    this.$router.go();
            }

            try {
                const userData ={
                     memberIdName: this.memberIdName,
                     password: this.password,
                }; 
               await this.$store.dispatch('login',userData);
               this.$router.push('/main');
            } catch (error) {
                alert(error.response.data.error);
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
.login_container{
    position:absolute;
    top:50%;
    left:50%;
    transform: translate(-50%,-50%);
    width:400px;
    border-radius:10px;
}

.login_container h1{
    text-align: center;
    padding: 0 0 20px 0;
    border-bottom:1px solid silver;
}

.login_container form{
    padding: 0 40px;
    box-sizing: border-box;
}

form .login_input{
    position:relative;
    border-bottom:2px solid #adadad;
    margin:30px 0;
}

.login_input input{
    width:100%;
    padding:0 5px;
    height:40px;
    font-size:16px;
    border:none;
    background:none;
    outline:none;
}

.login_input label{
    position:absolute;
    top: 50%;
    left:5px;
    color: #adadad;
    transform: translateY(-50%);
    font-size:16px;
    pointer-events:none;
    transition: .5s;
}

.login_input span::before{
    content:'';
    position:absolute;
    top:40px;
    left:0;
    width:100%;
    height:2px;
    background:#2691d9;
}

.login_input input:focus ~ label,
.login_input input:valid ~ label{
    top: -5px;
    color: #2691d9
}

.login_container button{
    width:100%;
    height:50px;
    border:1px solid;
    background: #2691d9;
    border-radius: 25px;
    font-size:18px;
    color:#e9f4fb;
    font-weight:700;
    cursor:pointer;
    outline: none;
}

.login_container button:hover{
    border-color: #2691d9;
    transition: .5s;
}

.signup_link{
    margin: 30px 0;
    text-align:center;
    font-size: 16px;
    color: #666666;
}
.signup_link .link{
    color:#2691d9;
    text-decoration: none;
}
.signup_link .link:hover{
    text-decoration: underline;
}

</style>