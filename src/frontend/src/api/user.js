import { user } from './index';

function viewUserBookmarks(memberId){
    return user.get(memberId+'/bookmarks');
}

function viewUserReviews(memberId){
    return user.get(memberId+'/reviews');
}

function viewUserAppointments(memberId){
    return user.get(memberId+'/appointments');
}
function viewUserQuestions(memberId){
    return user.get(memberId+'/questions');
}



export{ viewUserBookmarks,viewUserReviews,viewUserAppointments,viewUserQuestions };