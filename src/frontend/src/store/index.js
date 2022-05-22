import Vue from 'vue';
import Vuex from 'vuex';
import {
    getNoAnswerCountFromCookie,
    getMemberIdFromCookie, 
    getMemberStatusFromCookie, 
    getNickNameFromCookie, 
    getTokenFromCookie,
    getUnapprovedReviewCountFromCookie,
    saveMemberIdToCookie,
    saveNickNameToCookie, 
    saveTokenToCookie, 
    saveMemberStatusToCookie,
    saveNoAnswerCountToCookie,
    saveUnapprovedReviewCountToCookie
} from '@/utils/cookies';
import { loginUser } from '@/api/index';
import {staffNoAnswerCount} from '@/api/staff';
import {adminUnapprovedReviewCount} from '@/api/admin';

Vue.use(Vuex);

export default new Vuex.Store({
    state:{
        memberId: getMemberIdFromCookie() ||'',
        nickName: getNickNameFromCookie() || '',
        memberStatus: getMemberStatusFromCookie() || '',
        token: getTokenFromCookie() || '',
        noAnswerCount: getNoAnswerCountFromCookie()||'',
        reviewCount: getUnapprovedReviewCountFromCookie()||'',
    },
    getters:{
        getMemberId(state){
            return state.memberId;
        },
        getNoAnswerCount(state){
            return state.noAnswerCount;
        },
        getReviewCount(state){
            return state.reviewCount;
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
            state.reviewCount='';
        },
        setNoAnswerCount(state, noAnswerCount){
            state.noAnswerCount = noAnswerCount;
        },
        setUnapprovedReviewCount(state,reviewCount){
            state.reviewCount = reviewCount;
        },
        minusAnswerCount(state){
            if(state.noAnswerCount> 0){
                state.noAnswerCount -=1;
                saveNoAnswerCountToCookie(state.noAnswerCount);
            }
        },
        minusReviewCount(state){
            if(state.reviewCount> 0){
                state.reviewCount -=1;
                saveUnapprovedReviewCountToCookie(state.reviewCount);
            }
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

            //STAFF 권한이면 미답변 카운터 load하고 저장.
            if(data.memberStatus === 'STAFF'){
                const data = await staffNoAnswerCount();
                const count = data.data;
                commit('setNoAnswerCount',count);
                saveNoAnswerCountToCookie(count);
            }

            //ADMIN 권한이면 미승인 리뷰 카운터 load하고 저장.
            if(data.memberStatus === 'ADMIN'){
                const data = await adminUnapprovedReviewCount();
                const count = data.data;
                commit('setUnapprovedReviewCount',count);
                saveUnapprovedReviewCountToCookie(count);
            }
            
            return data;
        },
    }
});