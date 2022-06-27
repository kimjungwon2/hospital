<template>
    <div>
        <template v-if ="isSearchHospital===true" >
            <AdminSearchHospitalModal 
            @hospitalCancel="cancelSearchHospital"
            @selectHospital="getHospitalId"/>  
        </template>

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
        <div>
            <select name="memberStatus" v-model="memberStatus">
                  <option value="">멤버 권한 선택</option>
                  <option value="NORMAL">NORMAL</option>
                  <option value="STAFF">STAFF</option>
                  <option value="ADMIN">ADMIN</option>
            </select>
        </div>
        <div v-if="memberStatus=='STAFF'">
            <font-awesome-icon icon="hospital-user" @click.prevent="searchHospital"/>
            병원 번호 : {{hospitalId}}
        </div>

        <button type="submit">회원가입</button>
    </form>

    </div>
</template>

<script>
import {adminCreateMember} from '@/api/admin'
import AdminSearchHospitalModal from '@/components/admin/user/AdminSearchHospitalModal.vue'
import { faHospitalUser } from '@fortawesome/free-solid-svg-icons';
import { library } from '@fortawesome/fontawesome-svg-core';

library.add(faHospitalUser)

export default {
    components:{
        AdminSearchHospitalModal,
    },
    data() {
        return {
            memberIdName:'',
            password:'',
            userName:'',
            nickName:'',
            phoneNumber:'',
            memberStatus:'',
            hospitalId:'',
            //log
            isSearchHospital:false,
        };
    },
    methods:{
        async submitForm(){
            if(this.memberStatus==="STAFF"){
                const userData = {
                    memberIdName: this.memberIdName,
                    password: this.password,
                    userName: this.userName,
                    nickName: this.nickName,
                    phoneNumber: this.phoneNumber,
                    memberStatus: this.memberStatus,
                    hospitalId:this.hospitalId
                }
                try{
                    await adminCreateMember(userData);
                    this.$alert("회원 가입이 완료되었습니다.");
                    this.$router.push('/admin/users').catch(error=>error);
                }catch(error){
                    alert(error.response.data.message);
                }finally{
                    this.initForm();
                }
            }

            else{
                const userData = {
                    memberIdName: this.memberIdName,
                    password: this.password,
                    userName: this.userName,
                    nickName: this.nickName,
                    phoneNumber: this.phoneNumber,
                    memberStatus: this.memberStatus,
                    hospitalId:null
                }
                try{
                    await adminCreateMember(userData);
                    this.$alert("회원 가입이 완료되었습니다.");
                    this.$router.push('/admin/users').catch(error=>error);
                }catch(error){
                    this.$alert(error.response.data.message);
                }finally{
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
        },
        searchHospital(){
            this.isSearchHospital = true;
        },
        cancelSearchHospital(){
            this.isSearchHospital = false;
        },
        getHospitalId(value){
            this.hospitalId = value;
        },
    }

}
</script>

<style>

</style>