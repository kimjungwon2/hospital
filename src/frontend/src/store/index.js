import Vue from 'vue';
import Vuex from 'vuex';
import {
    getMemberIdFromCookie, 
    getMemberStatusFromCookie, 
    getNickNameFromCookie, 
    getTokenFromCookie,
    saveMemberIdToCookie,
    saveNickNameToCookie, 
    saveTokenToCookie, 
    saveMemberStatusToCookie 
} from '@/utils/cookies';
import { loginUser } from '@/api/index';

Vue.use(Vuex);

export default new Vuex.Store({
    state:{
        memberId: getMemberIdFromCookie()||'',
        nickName: getNickNameFromCookie() || '',
        memberStatus: getMemberStatusFromCookie() || '',
        token: getTokenFromCookie() || '',
    },
    getters:{
        getMemberId(state){
            return state.memberId;
        },
        isLogin(state){
            return state.memberStatus === 'NORMAL';
        },
        isStaff(state){
            return state.memberStatus === 'STAFF';
        },
        isAdmin(state){
            return state.memberStatus === 'ADMIN';
        }
    },
    mutations: {
        setUser(state, data){
            state.memberId = data.memberId;
            state.nickName = data.nickName;
            state.memberStatus = data.memberStatus;
            state.token = data.token;
        },
        clearUserInfo(state){
            state.memberId = '';
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
            saveMemberIdToCookie(data.memberId);
            saveTokenToCookie(data.token);
            saveNickNameToCookie(data.nickName);
            saveMemberStatusToCookie(data.memberStatus);
            return data;
        }
    }
});