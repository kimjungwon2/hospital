<template>
    <section class="signup_container">
        <form @submit.prevent="submitForm">  
        <div class="title">Registration</div> 
           <div class="user-details"> 
               <div class="input-box">
                   <label class="details" for="memberIdName">회원 아이디(이메일)</label>
                   <input id="memberIdName" type="text" placeholder="Enter your email" required v-model="memberIdName">
               </div>
                <div class="input-box">
                   <label class="details" for="userName">이름</label>
                   <input id="userName" type="text" placeholder="Enter your name" required v-model="userName">
               </div>
               <div class="input-box">
                   <label class="details" for="password">비밀번호</label>
                   <input id="password" type="password" placeholder="Enter your password" required v-model="password">
               </div>
                <div class="input-box">
                   <label class="details" for="repassword">비밀번호 확인</label>
                   <input id="repassword" type="password" placeholder="Enter your password" required v-model="repassword">
               </div>
               <div class="input-box">
                   <label class="details" for="nickName">닉네임</label>
                   <input id="nickName" type="text" placeholder="Enter your nickname" required v-model="nickName">
               </div>
               <div class="input-box">
                   <label class="details" for="phoneNumber">전화번호</label>
                   <input id="phoneNumber" type="text" placeholder="Enter your phonenumber" required v-model="phoneNumber">
               </div>
           </div>  
            <div class="button">
                   <button type="submit">회원가입</button>
            </div>
            <p>{{ logMessage }}</p>
        </form>
    </section>

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
            repassword:'',
            //log
            logMessage:'',
        };
    },
    methods:{
        async submitForm(){
            if(this.password!==this.repassword){
                this.$alert('비밀번호와 비밀번호 확인이 일치하지 않습니다.');
            }
            else{
                const userData = {
                    memberIdName: this.memberIdName,
                    password: this.password,
                    userName: this.userName,
                    nickName: this.nickName,
                    phoneNumber: this.phoneNumber,
                }
                try{
                    await signupUser(userData);
                    this.$alert("회원 가입이 완료되었습니다.");
                    this.$router.push('/main');
                }catch(error){
                    alert(error.response.data.message);
                } finally{
                    this.initForm();
                }
            }
        },
        initForm(){
            this.memberIdName = '';
            this.password = '';
            this.userName = '';
            this.nickName = '';
            this.phoneNumber = '';
            this.repassword='';
        },
    }
};
</script>

<style>
.signup_container{
    display: flex;
    position:absolute;
    top:55%;
    left:50%;
    transform: translate(-50%,-50%);
    justify-content: center;
    align-items: center;
    max-width: 700px;
    width: 100%;
    background: #fff;
    padding:25px 30px;
    border-radius: 5px;
}

.signup_container .title{
    font-size:25px;
    font-weight:500;
    position: relative;
}

.signup_container .title::before{
    content:'';
    position: absolute;
    left:0;
    bottom:0;
    height:3px;
    width:30px;
    background:linear-gradient(-135deg,#71b7e6,#9b59b6);
}

.signup_container form .user-details{
    display:flex;
    flex-wrap:wrap;
    justify-content:space-between;
    margin: 20px 0 12px 0;
}

form .user-details .input-box{
    margin-bottom: 15px;
    width:calc(100%/2-20px);    
}

.user-details .input-box .details{
    display:block;
    font-weight: 500;
    margin-bottom:5px;
}

.user-details .input-box input{
    height: 45px;
    width: 100%;
    outline: none;
    border-radius: 5px;
    border:1px solid #ccc;
    padding-left:15px;
    font-size:16px;
}

form .button{
    height:45px;
    margin: 45px 0;
}

form .button button{
    height:100%;
    width: 100%;
    outline-color: none;
    color:white;
    font-size:18px;
    font-weight:500;
    border-radius:5px;
    background:#2691d9;
}

form .button button:hover{
    background:linear-gradient(-135deg,#71b7e6,#9b59b6);
}

@media (max-width: 1000px){
    .signup_container{
        max-width:100%;
    }
    form .user-details .input-box{
    margin-bottom: 15px;
    width:100%;
    }
    .signup_container form .user-details{
        max-height: 300px;
        overflow-y:scroll;
    }
    .user-details::-webkit-scrollbar{
        width:1;
    }
}

</style>