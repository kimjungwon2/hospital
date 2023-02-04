<template>
  <section id="staffViewBookmarkUser">
    즐겨찾기한 유저 수: {{users.totalElements}}<br>
    <form @submit.prevent="submitForm">
            <select name="searchCondition" v-model="searchCondition">
                  <option value="nickName">닉네임</option>
                  <option value="memberIdName" >회원 아이디</option>
                  <option value="phoneNumber" >전화번호</option>
            </select>
        <input id="keyword" type="text" required  v-model="keyword"/><button type="submit">검색하기</button>
	</form>

    <ul v-for="user in users.content" :key="user.bookmarkId" >
      <div>
        <li>아이디: {{ user.memberIdName}}</li>
        <li>닉네임: {{ user.nickName }}</li>
        <li>전화번호: {{ user.phoneNumber}}</li>
      </div>
    </ul>

    <div>
      <button :disabled="pageNum === 0" @click.prevent="prevPage">
        이전
      </button>
      <span v-if="users.totalElements!==0">{{ pageNum + 1 }} / {{ totalPageNum }} 페이지</span>
      <span v-else> 0 / {{ totalPageNum }} 페이지</span>
      <button :disabled="pageNum >= totalPageNum - 1" @click.prevent="nextPage">
        다음
      </button>
    </div>
  </section>
</template>

<script>
import { staffSearchBookmarkUsers} from '@/api/staff';


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

</style>