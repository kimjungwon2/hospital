<template>
    <div>
        <template v-if ="isSearchHospital===true" >
            <AdminSearchHospitalModal 
            @hospitalCancel="cancelSearchHospital"
            @selectHospital="getHospitalId"/>
        </template>
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
            <div v-if="memberStatus=='STAFF'">
                <font-awesome-icon icon="hospital-user" @click.prevent="searchHospital"/>
                병원 번호 : {{hospitalId}}
            </div>

            <button type="submit">수정하기</button>
            <p>{{ logMessage }}</p>
        </form>

    </div>


</template>

<script>
import {adminViewMember,adminModifyMember} from '@/api/admin';
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
            userName:'',
            nickName:'',
            phoneNumber:'',
            memberStatus:'',
            //log
            logMessage:'',
            member:{},
            hospitalId:'',

            isSearchHospital:false,
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
            if(this.memberStatus==="STAFF"){
                const userData = {
                    userName: this.userName,
                    nickName: this.nickName,
                    phoneNumber: this.phoneNumber,
                    memberStatus: this.memberStatus,
                    hospitalId:this.hospitalId
                }
                await adminModifyMember(this.$route.params.userId,userData);
                this.initForm();
                this.$alert("회원 수정이 정상적으로 이루어졌습니다.")
                this.$router.push('/admin/users').catch(error=>error);
            }
            else{
                const userData = {
                    userName: this.userName,
                    nickName: this.nickName,
                    phoneNumber: this.phoneNumber,
                    memberStatus: this.memberStatus,
                    hospitalId:null
                }
                await adminModifyMember(this.$route.params.userId,userData);
                this.initForm();
                this.$alert("회원 수정이 정상적으로 이루어졌습니다.")
                this.$router.push('/admin/users').catch(error=>error);
            }

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
        this.hospitalId = data.hospitalNumber;
    },
}
</script>

<style>

</style>