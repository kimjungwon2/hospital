<template>
    <header>
      <nav class="navbar">
        <div class="navbar_logo">
          <router-link :to="logoLink"><font-awesome-icon icon="ambulance" /> K-Hospital</router-link>
        </div>

        
          <ul class="navbar_menu" v-if="isLogin">
              <li><router-link to="/user/activity">나의 활동</router-link></li>
              <li @click.prevent="routerUser">정보 수정</li>
          </ul>

          <ul class="navbar_menu" v-if="isStaff">
              <li><router-link to="/staff/view/hospital">내 병원 관리</router-link></li>
              <li><router-link to="/staff">병원 활동</router-link></li>
          </ul>

          <ul class="navbar_menu" v-if="isAdmin">
              <li><router-link to="/admin">관리 페이지</router-link></li>
          </ul>


          <ul class="navbar_user" v-if="isLogin||isStaff||isAdmin">
              <li v-if="isStaff">
                <router-link to="/staff/questions">
                  <font-awesome-icon icon="bell" /><span v-if="getNoAnswerCount!=0"> {{getNoAnswerCount}}</span>
                </router-link>
              </li>
              <li><span>{{ $store.state.nickName }}</span></li>
              <li><a @click="logoutUser">로그아웃</a></li>
          </ul>
          <ul class="navbar_user" v-else>
              <li><router-link to="/login">Login</router-link></li>
              <li><router-link to="/signup">Signup</router-link></li>
          </ul>
      
      </nav>
    </header>
</template>

<script>

import { library } from '@fortawesome/fontawesome-svg-core'
import { faAmbulance,faBell } from '@fortawesome/free-solid-svg-icons'
import { deleteCookie} from '@/utils/cookies'

library.add(faAmbulance, faBell)

export default {
  computed:{
    isLogin(){
      return this.$store.getters.isLogin;
    },
    isStaff(){
      return this.$store.getters.isStaff;
    },
    isAdmin(){
      return this.$store.getters.isAdmin;
    },
    logoLink(){
      if(this.$store.getters.isLogin){
        return '/main';
      }
      else if(this.$store.getters.isStaff){
        return '/main';
      }
      else if(this.$store.getters.isAdmin){
        return '/admin';
      }
      else{
        return '/main';
      }
    },
    getNoAnswerCount(){
      return this.$store.getters.getNoAnswerCount;
    }
  },
  methods:{
    logoutUser(){
      deleteCookie('member_status');
      deleteCookie('nick_name');
      deleteCookie('token');
      deleteCookie('member_id');
      deleteCookie('no_answer_count');
      this.count='';
      this.$store.commit('clearUserInfo');
      this.$router.push('/').catch(err => {
                if (
                    err.name !== 'NavigationDuplicated' &&
                    !err.message.includes('Avoided redundant navigation to current location')
                ) {alert(err);}}
      );
    },
    routerUser(){
       const memberId= this.$store.getters.getMemberId;
       this.$router.push('/user/'+memberId+'/info');
    },
  },
};
</script>

<style>
header{
  margin: 0;
}

.navbar{
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #5F9EA0;
  padding: 8px 50px;
}

.navbar_logo {
  font-size: 24px;
  color: #A0605F;
}

.navbar a{
  text-decoration: none;
  color:white;
}

.navbar_menu{
  display: flex;
  list-style:none;
  padding-left: 0;
}

.navbar_menu li{
  color:white;
  padding:8px 12px;
}

.navbar_user{
  display: flex;
  list-style: none;
  color:white;
  padding-left:0;
}

.navbar_user li{
  padding: 8px 12px;
}

a.router-link-exact-active {
  text-decoration: none;
  color:#226365;
}

</style>