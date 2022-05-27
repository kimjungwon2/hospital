<template>
    <header>
      <nav class="navbar">
        <div class="navbar_logo">
          <router-link :to="logoLink"><font-awesome-icon icon="ambulance" /> K-Hospital</router-link>
        </div>

        
          <ul id = "main-menu" class="navbar_menu" v-if="isLogin">
              <li>나의 활동
                <ul id="sub-menu">
                    <li @click="routeBookmark">즐겨찾기 병원</li>
                    <li @click="routeQuestion">내가 등록한 Q&A</li>
                    <li @click="routeReview">내가 등록한 리뷰</li>
                </ul>
              </li>
              <li @click.prevent="routerUser">내 정보 수정</li>
          </ul>

          <ul id = "main-menu" class="navbar_menu" v-if="isStaff">
              <li><router-link to="/staff/view/hospital">내 병원 관리</router-link></li>
              <li>병원 활동
                <ul id="sub-menu">
                    <li><router-link to="/staff/reviews">리뷰 보기</router-link></li>
                    <li><router-link to="/staff/Q&A">Q&A 관리</router-link></li>
                    <li><router-link to="/staff/bookmark/users">즐겨찾기한 유저 보기</router-link></li>
                </ul>
              </li>
          </ul>

          <ul id = "main-menu" class="navbar_menu" v-if="isAdmin">
              <li>관리 페이지
                <ul id="sub-menu">
                  <li><router-link to="/admin/users">유저 관리</router-link></li>
                  <li><router-link to="/admin/hospitals">병원 관리</router-link></li>
                  <li><router-link to="/admin/reviews">리뷰 관리</router-link></li>
                  <li><router-link to="/admin/QandAs">Q&A 관리</router-link></li>
                  <li><router-link to="/admin/tags">태그 관리</router-link></li>
                </ul>
              </li>
          </ul>


          <ul class="navbar_user" v-if="isLogin||isStaff||isAdmin">
              <li v-if="isStaff">
                <router-link to="/staff/questions">
                  <font-awesome-icon icon="bell" /><span v-if="getNoAnswerCount!=0"> {{getNoAnswerCount}}</span>
                </router-link>
              </li>
              <li v-if="isAdmin">
                <router-link to="/admin/reviews/unapproved">
                  <font-awesome-icon icon="bell" /><span v-if="getReviewCount!=0"> {{getReviewCount}}</span>
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
  data() {
    return {
      memberId: this.$store.getters.getMemberId
    }
  },
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
      return '/main';
    },
    getNoAnswerCount(){
      return this.$store.getters.getNoAnswerCount;
    },
    getReviewCount(){
      return this.$store.getters.getReviewCount;
    },
  },
  methods:{
    logoutUser(){
      deleteCookie('member_status');
      deleteCookie('nick_name');
      deleteCookie('token');
      deleteCookie('member_id');
      deleteCookie('no_answer_count');
      deleteCookie('review_count');
      deleteCookie('search_name');
      this.count='';
      this.$store.commit('clearUserInfo');
      this.$router.push('/').catch(err => {
                if (
                    err.name !== 'NavigationDuplicated' &&
                    !err.message.includes('Avoided redundant navigation to current location')
                ) {alert(err);}}
      );
      //새로고침하기
      this.$router.go();
    },
    routerUser(){
       const memberId= this.$store.getters.getMemberId;
       this.$router.push('/user/'+memberId+'/info');
    },
    routeBookmark(){
       const id = this.$store.getters.getMemberId;
       this.$router.push(`/user/${id}/bookmarks`);
    },
    routeQuestion(){
        const id = this.$store.getters.getMemberId;
        this.$router.push(`/user/${id}/questions`);
    },
    routeReview(){
        const id = this.$store.getters.getMemberId;
        this.$router.push(`/user/${id}/reviews`);
    },
  },
};
</script>

<style>
header{
  z-index:1;
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

#sub-menu{
  margin:0;
  padding:0;
  list-style-type:none;
}

#main-menu>li{
  float:left;
}

#main-menu>li{
  font-size:0.85rem;
  color: rbga(255,255,255,0.85);
  text-decoration: none;
  letter-spacing: 0.05rem;
  display:block;
  padding:16px 36px;
  text-align: center;
}

#sub-menu{
  position:absolute;
  background: #4c7e80;
  opacity:0;
  visibility: hidden;
  z-index:2;
}

#sub-menu>li{
  padding: 16px 28px;
  border-bottom:1px solid rgba(0, 0, 0, 0.15);
}

#sub-menu>li>a{
  color:white;
  text-decoration: none;
}

#main-menu>li:hover #sub-menu{
  opacity:1;
  visibility: visible;
}

#sub-menu > li >  a:hover {
 text-decoration: underline;
}

</style>