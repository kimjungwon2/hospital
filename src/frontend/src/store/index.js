import Vue from 'vue';
import Vuex from 'vuex';
import {getSearchNameFromCookie} from '@/utils/cookies'

Vue.use(Vuex);

export default new Vuex.Store({
    state:{
        memberIdName:'',
        name: '',
        nickName: '',
        phoneNumber: '',
        searchName: getSearchNameFromCookie() || '',
    },
    getters:{
        isLogin(state){
            return state.nickName !== '';
        },
        getSearchName(state){
            return state.searchName;
        },
    },
    mutations: {
        setUser(state, data){
            state.memberIdName = data.memberIdName;
            state.name = data.name;
            state.nickName = data.nickName;
            state.phoneNumber = data.phoneNumber;
        },
        clearUserInfo(state){
            state.memberIdName='';
            state.name='';
            state.nickName='';
            state.phoneNumber='';
        },
        setSearchName(state, data){
            state.searchName = data.searchName;
        }
    },

});