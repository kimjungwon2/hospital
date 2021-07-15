import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({
    state:{
        memberIdName:'',
        name: '',
        nickName: '',
        phoneNumber:'',
    },
    getters:{
        isLogin(state){
            return state.nickName !== '';
        }
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
    },

});