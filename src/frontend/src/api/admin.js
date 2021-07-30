import { admin } from './index';

/* 멤버 관리 */

//관리자 멤버 리스트 보기
function adminViewMemberLists(){
    return admin.get('/user');
}
//관리자 멤버 리스트 검색
function adminSearchMemberLists(allSearch, memberId, memberIdName, nickName, userName, phoneNumber,authorizationStatus,hospitalNumber){
    return admin.get('/user/search',
    {
        params:{
            allSearch: allSearch,
            memberId: memberId,
            memberIdName: memberIdName,
            nickName:nickName,
            userName:userName,
            phoneNumber:phoneNumber,
            authorizationStatus:authorizationStatus,
            hospitalNumber:hospitalNumber,
        },
    });
}

//관리자 멤버 상세 보기
function adminViewMember(memberId){
    return admin.get('/user/view/'+memberId);
}
//관리자 멤버 등록하기
function adminCreateMember(userData){
    return admin.post('/signup',userData);
}
//관리자 멤버 삭제
function adminDeleteMember(memberId){
    return admin.delete('/user/delete/'+memberId);
}

//관리자 멤버 수정하기
function adminModifyMember(memberId, userData){
    return admin.put('/user/modify/'+memberId, userData);
}

//관리자 멤버 권한 주기.
function adminAuthorizeMember(memberId, request){
    return admin.put('/user/authority/'+memberId, request);
}


/* 리뷰 관리 */

function adminViewReviewLists(){
    return admin.get('/review');
}

//리뷰 리스트 검색
function adminSearchReviewLists(nickName, hospitalName, memberIdName){
    return admin.get('/review/search',
    {
        params:{
            nickName: nickName,
            hospitalName: hospitalName,
            memberIdName: memberIdName,
        },
    });
}

//리뷰 상세보기
function adminViewReview(reviewId){
    return admin.get('/review/view/'+reviewId);
}

//리뷰 승인해주기
function adminApproveReview(reviewId, request){
    return admin.put('/review/approve/'+reviewId, request);
}

//리뷰 삭제하기
function adminDeleteReview(reviewId){
    return admin.delete('/review/delete/'+reviewId);
}

/* 태그 관리 */
function adminViewTagsList(){
    return admin.get('/tag');
}
//태그 이름 검색
function adminSearchTags(tagName){
    return admin.get('/tag/search/'+tagName);
}
//태그 삭제
function adminDeleteTag(tagId){
    return admin.delete('/tag/delete/'+tagId);
}
//태그 생성
function adminCreateTag(tagData){
    return admin.post('/tag/create', tagData);
}

/* Q&A 관리 */
function adminViewQuestionsList(){
    return admin.get('/question');
}

function adminSearchQuestionsList(nickName, hospitalName, memberIdName){
    return admin.get('/question/search',
    {
        params:{
            nickName: nickName,
            hospitalName: hospitalName,
            memberIdName: memberIdName,
        },
    });
}

function adminDeleteQuestion(){
    return admin.get('/question/delete');
}




export { 
    //멤버
    adminViewMemberLists, adminSearchMemberLists, 
    adminViewMember, adminCreateMember, 
    adminDeleteMember, adminModifyMember, 
    adminAuthorizeMember,

    //리뷰
    adminViewReviewLists, adminSearchReviewLists,
    adminViewReview, adminApproveReview, adminDeleteReview,

    //태그
    adminViewTagsList, adminSearchTags, adminDeleteTag, adminCreateTag,

    //Q&A
    adminViewQuestionsList, adminSearchQuestionsList, adminDeleteQuestion
};
