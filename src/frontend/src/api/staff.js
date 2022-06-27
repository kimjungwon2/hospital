import { staff} from './index';

//병원 관계자 병원보기
function staffViewHospital(){
    return staff.get('/hospital/view');
} 

//병원 관계자 병원 정보 수정
function staffModifyHospital(hospitalId,hospitalData){
    return staff.put('/hospital/modify/'+hospitalId,hospitalData);
} 

//병원 관계자 상세 정보 등록
function staffRegisterDetailedHosInfo(detailedHosInfo){
    return staff.post('/hospital/register/detailed',detailedHosInfo);
}

//병원 관계자 상세 정보 삭제
function staffDeleteDetailedHosInfo(memberId,detailedHosId){
    return staff.delete(+memberId+'/detailedHos/delete/'+detailedHosId);
} 

//관리자 추가 정보 등록
function staffRegisterStaffHospitalInfo(staffHosInfo){
    return staff.post('/hospital/register/staffHosInfo',staffHosInfo);
}

//병원 관계자 추가 정보 보기
function staffViewStaffHospitalInfo(staffHosInfoId){
    return staff.get('/staffHosInfo/'+staffHosInfoId);
} 

//병원 관계자 추가 정보 수정
function staffModifyStaffHosInfo(staffHosId, staffHosData){
    return staff.put('/staffHosInfo/modify/'+staffHosId, staffHosData);
}

//병원 관계자 추가 정보 삭제
function staffDeleteStaffHosInfo(memberId,staffHosId){
    return staff.delete(+memberId+'/staffHosInfo/delete/'+staffHosId);
} 

//병원 관계자 태그 생성
function staffCreateTag(tagData){
    return staff.post('/tag/create', tagData);
} 

//태그 이름 검색
function staffSearchTags(tagName){
    return staff.get('/tag/search/'+tagName);
}

//병원 관계자 병원 태그 연결
function staffLinkHospitalTag(request){
    return staff.post('/hospital/tag/link',request);
} 

//병원 관계자 병원 태그 삭제
function staffDeleteHospitalTag(memberId, postTagId){
    return staff.delete(+memberId+'/hospital/tag/delete/'+postTagId);
} 

//병원 관계자 병원 의사 등록
function staffCreateDoctor(request){
    return staff.post('/doctor/register',request);
}

//병원 관계자 병원 의사 삭제
function staffDeleteDoctor(memberId,doctorId){
    return staff.delete(+memberId+'/doctor/delete/'+doctorId);
}

//병원 관계자 병원 의사 수정
function staffModifyDoctor(doctorId,doctorData){
    return staff.put('/doctor/modify/'+doctorId, doctorData);
}


//관계자 병원 섬네일 보기
function staffViewThumbnail(thumbnailId){
    return staff.get('/hospital/view/thumbnail',
    {
        params:{
            thumbnailId:thumbnailId,
        },
    });
}

//관계자 섬네일 등록하기
function staffCreateThumbnail(data){
    return staff.post(`/hospital/register/thumbnail`,data);
}


//관리자 섬네일 삭제하기
function staffDeleteThumbnail(thumbnailId){
    return staff.delete('/hospital/delete/thumbnail/'+thumbnailId);
}

//관리자 병원 이미지 업로드
function staffCreateHospitalImage(data){
    return staff.post(`/hospital/register/images`,data);
}

//관리자 병원 이미지들 보기
function staffViewHospitalImages(hospitalId){
    return staff.get('/hospital/view/hospitalImages',
    {
        params:{
            hospitalId:hospitalId,
        },
    });
}

//관리자 병원 이미지 삭제
function staffDeleteHospitalImage(hospitalImageId){
    return staff.delete('/hospital/delete/hospitalImages/'+hospitalImageId);
}



/*등록된 병원 검색 서비스*/

function staffSearchBookmarkUsers(searchCondition){
    return staff.get('/bookmark/search/user',
    {
        params:{
            nickName: searchCondition.nickName,
            memberIdName: searchCondition.memberIdName,
            phoneNumber:searchCondition.phoneNumber,
            page: searchCondition.page,
        },
    });
}

//리뷰 리스트 검색
function staffSearchReviewLists(searchCondition){
    return staff.get('/review/search',
    {
        params:{
            nickName: searchCondition.nickName,
            memberIdName: searchCondition.memberIdName,
            page: searchCondition.page,
        },
    });
}

//리뷰 상세보기
function staffViewReview(reviewId){
    return staff.get('review/view/'+reviewId);
}

//답변하지 않은 질문 수 받아오기
function staffNoAnswerCount(){
    return staff.get('question/count');
}

//질문에 답변하기
function staffRegisterAnswer(answerData){
    return staff.post('question/answer',answerData);
}

//질문 리스트 검색
function staffSearchQuestionLists(searchCondition){
    return staff.get('/question/search',
    {
        params:{
            nickName: searchCondition.nickName,
            memberIdName: searchCondition.memberIdName,
            page: searchCondition.page,
        },
    });
}

//답변이 없는 리스트 검색
function staffSearchNoAnswerQuestions(searchCondition){
    return staff.get('/question/noAnswer/search',
    {
        params:{
            nickName: searchCondition.nickName,
            memberIdName: searchCondition.memberIdName,
            page: searchCondition.page,
        },
    });
}







export{
    staffRegisterDetailedHosInfo,staffRegisterStaffHospitalInfo,staffModifyStaffHosInfo,
    staffSearchTags, staffLinkHospitalTag, staffCreateTag,
    staffModifyHospital,staffViewHospital,staffDeleteHospitalTag,staffDeleteDetailedHosInfo,
    staffViewStaffHospitalInfo,staffDeleteStaffHosInfo,
    staffCreateDoctor,staffDeleteDoctor,staffModifyDoctor,

    staffViewThumbnail,staffDeleteThumbnail,staffCreateThumbnail,
    staffCreateHospitalImage,staffViewHospitalImages,staffDeleteHospitalImage,

    staffRegisterAnswer,staffNoAnswerCount,staffSearchBookmarkUsers,staffViewReview,
    staffSearchReviewLists,staffSearchQuestionLists,staffSearchNoAnswerQuestions,
}