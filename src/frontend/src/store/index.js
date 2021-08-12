import Vue from 'vue';
import Vuex from 'vuex';
import {
    getNoAnswerCountFromCookie,
    getMemberIdFromCookie, 
    getMemberStatusFromCookie, 
    getNickNameFromCookie, 
    getTokenFromCookie,
    saveMemberIdToCookie,
    saveNickNameToCookie, 
    saveTokenToCookie, 
    saveMemberStatusToCookie,
    saveNoAnswerCountToCookie
} from '@/utils/cookies';
import { loginUser } from '@/api/index';

Vue.use(Vuex);

export default new Vuex.Store({
    state:{
        memberId: getMemberIdFromCookie() ||'',
        nickName: getNickNameFromCookie() || '',
        memberStatus: getMemberStatusFromCookie() || '',
        token: getTokenFromCookie() || '',
        noAnswerCount: getNoAnswerCountFromCookie()||'',
    },
    getters:{
        getMemberId(state){
            return state.memberId;
        },
        getNoAnswerCount(state){
            return state.noAnswerCount;
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
            state.noAnswerCount='';
        },
        setNoAnswerCount(state, noAnswerCount){
            state.noAnswerCount = noAnswerCount;
        },
        minusAnswerCount(state){
            state.noAnswerCount -=1;
            saveNoAnswerCountToCookie(state.noAnswerCount);
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
        },
    }
});