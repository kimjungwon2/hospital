<template>
  <section id="staffViewBookmarkUser">
    <section class="staffViewBookmarkUser__count">
      <h1>즐겨찾기한 유저 수: {{users.totalElements}}</h1><br><br>
    </section>

    <section class="staffViewBookmarkUser__search">
      <form @submit.prevent="submitForm">
              <select name="searchCondition" v-model="searchCondition">
                    <option value="nickName">닉네임</option>
                    <option value="memberIdName" >회원 아이디</option>
                    <option value="phoneNumber" >전화번호</option>
              </select>
          <input id="keyword" type="text" required  v-model="keyword"/>
          <button type="submit">
            <font-awesome-icon icon="search"/>
          </button>
          <br><br>
	    </form>
    </section>

    <section class="staffViewBookmarkUser__user">
      <div class="user__item" v-for="user in users.content" :key="user.bookmarkId" >
          <b>아이디</b> {{ user.memberIdName}}<br>
          <b>닉네임</b> {{ user.nickName }}<br>
          <b>전화번호</b> {{ user.phoneNumber}}<br>
      </div>
    </section>

    <section class="staffViewBookmarkUser__page" v-if="totalPageNum!==0">
      <button :disabled="pageNum === 0" @click.prevent="prevPage">
        이전
      </button>
      <span v-if="users.totalElements!==0">{{ pageNum + 1 }} / {{ totalPageNum }} 페이지</span>
      <span v-else> 0 / {{ totalPageNum }} 페이지</span>
      <button :disabled="pageNum >= totalPageNum - 1" @click.prevent="nextPage">
        다음
      </button>
    </section>
  </section>
</template>

<script>
import { staffSearchBookmarkUsers} from '@/api/staff';
import { library } from '@fortawesome/fontawesome-svg-core';
import { faSearch } from '@fortawesome/free-solid-svg-icons';
library.add(faSearch)


export default {
   data() {
     return {
       //데이터  
       pageNum: 0,
       totalPageNum:'',

       //리뷰 데이터
       users: [],
       //검색 조건 default = memberIdName로 한다. 
       searchCondition:'memberIdName',
       //검색명
       keyword:'',
     }
   },
   methods:{
    
    //검색
    async submitForm(){
        this.pageNum = 0;
        const obj ={};
        let key = this.searchCondition;
        obj[key] = this.keyword;

        const {data} = await staffSearchBookmarkUsers(obj);
        this.users = data;
        this.totalPageNum = this.users.totalPages;
    },
    //이전 페이지
    async prevPage(){
        this.pageNum-=1;
        
        const obj ={};
        let key = this.searchCondition;
        let page = "page";
        //검색
        obj[key] = this.keyword;
        //페이지 번호
        obj[page] = this.pageNum;

        const {data} = await staffSearchBookmarkUsers(obj);
        this.users = data;
    },
    //다음 페이지
    async nextPage(){
        this.pageNum+=1;

        const obj ={};
        let key = this.searchCondition;
        let page = "page";
        obj[key] = this.keyword;
        obj[page] = this.pageNum;

        const {data} = await staffSearchBookmarkUsers(obj);
        this.users = data;
    }
   },

   async created(){
       const condition ={
            page: 0,
       }
        const {data} = await staffSearchBookmarkUsers(condition);

        this.users = data;
        this.totalPageNum = this.users.totalPages;
    }
}
</script>

<style>
#staffViewBookmarkUser{
  position:relative;
  text-align:left;
  left:12%;
  width:73%;    
}

#staffViewBookmarkUser .staffViewBookmarkUser__count{
  text-align: center;
}

#staffViewBookmarkUser .staffViewBookmarkUser__search{
  text-align: center;
}

#staffViewBookmarkUser .staffViewBookmarkUser__page{
  text-align: center;
}

.staffViewBookmarkUser__user .user__item{
  margin-bottom: 20px;
  position:relative;
  text-align:left;
  left:12%;
  width:73%;
  border: 2px solid #0067a3;
  border-radius:10px;
}

</style>