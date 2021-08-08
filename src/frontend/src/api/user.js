import { user } from './index';
//북마크 여부 확인
function isUserBookmark(memberId, hospitalId){
    return user.get(memberId+'/bookmark/hospital/'+hospitalId);
}
//북마크 등록
function userRegisterBookmark(data){
    return user.post('/hospital/bookmark/register',data);
}
//유저가 등록한 북마크 보기
function viewUserBookmarks(memberId){
    return user.get(memberId+'/bookmarks');
}
//유저가 등록한 리뷰 보기
function viewUserReviews(memberId){
    return user.get(memberId+'/reviews');
}
//리뷰 등록하기
function registerUserReview(reviewData){
    return user.post('/review/register',reviewData);
}

function viewUserAppointments(memberId){
    return user.get(memberId+'/appointments');
}
function viewUserQuestions(memberId){
    return user.get(memberId+'/questions');
}


export{ isUserBookmark, userRegisterBookmark, 
    viewUserBookmarks,viewUserReviews,registerUserReview,
    viewUserAppointments,viewUserQuestions };