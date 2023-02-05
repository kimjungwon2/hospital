<template>
      <form class="main__searchHospital" @submit.prevent="searchForm">
        <div class="main__search">
            <label for="searchHospital"></label>
            <input id="searchHospital" type="text" v-model="searchName">
            <font-awesome-icon icon="search"  @click.prevent="searchForm"/>
        </div>
    </form>
</template>

<script>
import { library } from '@fortawesome/fontawesome-svg-core';
import { faSearch } from '@fortawesome/free-solid-svg-icons';
library.add(faSearch)

export default {
    data(){
        return{
            searchName:'',
        };
    },
    methods: {
        searchForm(){
            if(this.searchName==='') alert('검색어를 입력하세요.');
            else this.$router.push(`/search/view/${this.searchName}`);
        },
    },
};
</script>

<style>
.main__search {
    position: relative;
    text-align: center;
    width: 400px;
    margin: 0 auto;
}
.main__search input {
    width: 100%;
    border-radius: 20px;
    border: 1px solid #bbb;
    margin: 10px 0;
    padding: 10px 12px;
}
.main__searchHospital .fa-search {
    position: absolute;
    right: 5px;    
    top: 20px;
}
</style>