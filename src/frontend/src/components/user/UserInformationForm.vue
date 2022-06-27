<template>
    <section id="userInformation">
        <form @submit.prevent="submitForm">
            <div class="userInformation__title">회원 정보</div>
            <div class="userInformation__userDetails">
                <div class="userDetails__info">
                    <label class="info__details" for="userId">회원 번호</label>
                    <div class="info__result">{{member.id}}</div>
                </div>
                <div class="userDetails__info">
                    <label class="info__details" for="memberIdName">회원 아이디(이메일)</label>
                    <div class="info__result" >{{member.memberIdName}}</div>
                </div>
                <div class="userDetails__info">
                    <label class="info__details" for="userName">이름</label>
                    <input id="userName" type="text" required v-model="userName">
                </div>
                <div class="userDetails__info">
                    <label class="info__details" for="nickName">닉네임</label>
                    <input id="nickName" type="text" required v-model="nickName">
                </div>
                <div class="userDetails__info">
                    <label class="info__details" for="phoneNumber">전화번호</label>
                    <input id="phoneNumber" type="text" required v-model="phoneNumber">
                </div>
                <div class="userDetails__info">
                    <label class="info__details" for="memberStatus">회원 권한</label>
                    <div class="info__result" >{{member.memberStatus}}</div>
                </div>
            </div>

            <div class="userInformation__button">
                <button type="submit">수정하기</button>
            </div>
                <p>{{ logMessage }}</p>

        </form>
    </section>
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
#userInformation{
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

.userInformation__title{
    font-size:25px;
    font-weight:500;
    position: relative;
}

.userInformation__title::before{
    content:'';
    position: absolute;
    left:0;
    bottom:0;
    height:3px;
    width:30px;
    background:linear-gradient(-135deg,#71b7e6,#9b59b6);
}

.userInformation__userDetails{
    display:flex;
    flex-wrap:wrap;
    justify-content:space-between;
    margin: 20px 0 12px 0;
}

.userInformation__userDetails .userDetails__info{
    margin-bottom: 15px;
    width:calc(100%/2-20px);    
}

.info__details{
    display:block;
    font-weight: 500;
    margin-bottom:5px;   
}

.info__result{
    text-align:center;
    Transform: translate(-20%,20%);
    height:45px;    
    width: 100px;
    outline: none;
    border-radius: 5px;
    padding-left:15px;
    font-size:16px;
}

.userDetails__info input{
    height: 45px;
    width: 100%;
    outline: none;
    border-radius: 5px;
    border:1px solid #ccc;
    padding-left:15px;
    font-size:16px;
}

.userInformation__button{
    height:45px;
    margin: 45px 0;
}

.userInformation__button button{
    height:100%;
    width: 100%;
    outline-color: none;
    color:white;
    font-size:18px;
    font-weight:500;
    border-radius:5px;
    background:#2691d9;
}

.userInformation__button button:hover{
    background:linear-gradient(-135deg,#71b7e6,#9b59b6);
}

@media (max-width: 1000px){
    #userInformation{
        max-width:100%;
    }
    form .userInformation__userDetails .userDetails__info{
    margin-bottom: 15px;
    width:100%;
    }

    #userInformation form .userInformation__userDetails{
        max-height: 300px;
        overflow-y:scroll;
    }

    .userInformation__userDetails::-webkit-scrollbar{
        width:1;
    }
}



</style>