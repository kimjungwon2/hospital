import Vue from 'vue';
import Vuex from 'vuex';
import { getMemberStatusFromCookie, 
    getNickNameFromCookie, 
    getTokenFromCookie,
    saveNickNameToCookie, 
    saveTokenToCookie, 
    saveMemberStatusToCookie 
} from '@/utils/cookies';
import { loginUser } from '@/api/index';

Vue.use(Vuex);

export default new Vuex.Store({
    state:{
        nickName: getNickNameFromCookie() || '',
        memberStatus: getMemberStatusFromCookie() || '',
        token: getTokenFromCookie() || '',
    },
    getters:{
        isLogin(state){
            return state.memberStatus === 'NORMAL';
        },
        isAdmin(state){
            return state.memberStatus === 'ADMIN';
        }
    },
    mutations: {
        setUser(state, data){
            state.nickName = data.nickName;
            state.memberStatus = data.memberStatus;
            state.token = data.token;
        },
        clearUserInfo(state){
            state.nickName='';
            state.memberStatus='';
            state.token='';
        },
    },
    actions:{
        async login({commit}, userData){
            const { data } = await loginUser(userData);
            commit('setUser', data);
            //쿠키 저장.
            saveTokenToCookie(data.token);
            saveNickNameToCookie(data.nickName);
            saveMemberStatusToCookie(data.memberStatus);
            return data;
        }
                       
    }

});