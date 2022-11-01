import { admin} from './index';

/* 멤버 관리 */
//관리자 멤버 리스트 검색
function adminSearchMemberLists(searchCondition){
    return admin.get('/user/search',
    {
        params:{
            allSearch: searchCondition.allSearch,
            memberId: searchCondition.memberId,
            memberIdName: searchCondition.memberIdName,
            nickName: searchCondition.nickName,
            userName: searchCondition.userName,
            phoneNumber: searchCondition.phoneNumber,
            authorizationStatus: searchCondition.authorizationStatus,
            hospitalNumber: searchCondition.hospitalNumber,
            page: searchCondition.page,
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


/* 병원 관리 */
//관리자 병원 검색
function adminSearchHospitalLists(searchCondition){
    return admin.get('/hospital/search',
    {
        params:{
            hospitalId: searchCondition.hospitalId,
            hospitalName: searchCondition.hospitalName,
            businessCondition: searchCondition.businessCondition,
            cityName: searchCondition.cityName,
            page: searchCondition.page,
        },
    });
}
//관리자 병원 생성
function adminCreateHospital(hospitalData){
    return admin.post('/hospital/register',hospitalData);
}

//관리자 병원 상세 보기
function adminViewHospital(hospitalId, detailedHosInfoId, staffHosInfoId,thumbnailId){
    return admin.get('/hospital/view',
    {
        params:{
            hospitalId:hospitalId,
            detailedHosInfoId: detailedHosInfoId,
            staffHosInfoId: staffHosInfoId,
            thumbnailId:thumbnailId,
        },
    });
}

//관리자 병원 수정
function adminModifyHospital(hospitalId,hospitalData){
    return admin.put('/hospital/modify/'+hospitalId,hospitalData);
}

//관리자 병원 삭제
function adminDeleteHospital(hospitalId,staffHosInfoId){
    return admin.delete('/hospital/delete/'+hospitalId,
    {
        params:{
            staffHosInfoId:staffHosInfoId,
        }
    });
}

//관리자 상세 정보 삭제
function adminDeleteDetailedHosInfo(detailedHosId){
    return admin.delete('/detailedHos/delete/'+detailedHosId);
}

//관리자 상세 정보 등록
function adminRegisterDetailedHosInfo(detailedHosInfo){
    return admin.post('/hospital/register/detailed',detailedHosInfo);
}

//관리자 추가 정보 등록
function adminRegisterStaffHospitalInfo(staffHosInfo){
    return admin.post('/hospital/register/staff',staffHosInfo);
}

//관리자 추가 정보 보기
function adminViewStaffHospitalInfo(staffHosInfoId){
    return admin.get('/staffHosInfo/'+staffHosInfoId);
}


//관리자 추가 병원 정보 수정
function adminModifyStaffHosInfo(staffHosId, staffHosData){
    return admin.put('/staffHosInfo/modify/'+staffHosId, staffHosData);
}

//관리자 추가 병원 정보 삭제
function adminDeleteStaffHosInfo(staffHosId){
    return admin.delete('/staffHosInfo/delete/'+staffHosId);
}

//관리자 병원 태그 삭제
function adminDeleteHospitalTag(postTagId){
    return admin.delete('/hospital/tag/delete/'+postTagId);
}

//관리자 병원 태그 연결
function adminLinkHospitalTag(request){
    return admin.post('/hospital/tag/link',request);
}

//관리자 병원 평가 등록
function adminCreateHospitalEstimation(request){
    return admin.post('/estimation/register',request);
}

//관리자 병원 평가 삭제
function adminDeleteHospitalEstimation(estimationId){
    return admin.delete('/estimation/delete/'+estimationId);
}

//관리자 병원 평가 수정
function adminModifyHospitalEstimation(estimationId,estimationData){
    return admin.put('/estimation/modify/'+estimationId, estimationData);
}

//관리자 병원 의사 등록
function adminCreateDoctor(request){
    return admin.post('/doctor/register',request);
}

//관리자 병원 의사 삭제
function adminDeleteDoctor(doctorId){
    return admin.delete('/doctor/delete/'+doctorId);
}

//관리자 병원 의사 수정
function adminModifyDoctor(doctorId,doctorData){
    return admin.put('/doctor/modify/'+doctorId, doctorData);
}


//관리자 병원 섬네일 보기
function adminViewThumbnail(thumbnailId){
    return admin.get('/hospital/view/thumbnail',
    {
        params:{
            thumbnailId:thumbnailId,
        },
    });
}

//관리자 섬네일 업로드
function adminCreateThumbnail(data){
    return admin.post(`/hospital/register/thumbnail`,data);
}

//관리자 섬네일 삭제하기
function adminDeleteThumbnail(thumbnailId){
    return admin.delete('/hospital/delete/thumbnail/'+thumbnailId);
}

//관리자 병원 이미지 업로드
function adminCreateHospitalImage(data){
    return admin.post(`/hospital/register/images`,data);
}

//관리자 병원 이미지들 보기
function adminViewHospitalImages(hospitalId){
    return admin.get('/hospital/view/hospitalImages',
    {
        params:{
            hospitalId:hospitalId,
        },
    });
}

//관리자 병원 이미지 삭제
function adminDeleteHospitalImage(hospitalImageId){
    return admin.delete('/hospital/delete/hospitalImages/'+hospitalImageId);
}


/* 리뷰 관리 */

function adminViewReviewLists(){
    return admin.get('/review');
}

//미승인 리뷰 갯수
function adminUnapprovedReviewCount(){
    return admin.get('/review/unapproved/count');
}

//리뷰 리스트 검색
function adminSearchReviewLists(searchCondition){
    return admin.get('/review/search',
    {
        params:{
            nickName: searchCondition.nickName,
            hospitalName: searchCondition.hospitalName,
            memberIdName: searchCondition.memberIdName,
            page: searchCondition.page,
        },
    });
}

//리뷰 리스트 검색
function adminSearchUnapprovedReviewLists(searchCondition){
    return admin.get('/review/unapproved/search',
    {
        params:{
            page: searchCondition.page,
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
function adminViewTagsList(page){
    return admin.get('/tags',
    {
        params:{
            page:page,
        },
    });
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

function adminSearchQuestionsList(searchCondition){
    return admin.get('/question/search',
    {
        params:{
            nickName: searchCondition.nickName,
            hospitalName: searchCondition.hospitalName,
            memberIdName: searchCondition.memberIdName,
            page: searchCondition.page,
        },
    });
}

function adminDeleteQuestion(questionId,answerId){
    return admin.delete('/question/delete',
    {
        params:{
            questionId:questionId,
            answerId:answerId
        }
    });
}




export { 
    //멤버
    adminSearchMemberLists, 
    adminViewMember, adminCreateMember, 
    adminDeleteMember, adminModifyMember, 

    //병원
    adminSearchHospitalLists,adminCreateHospital,adminViewThumbnail,
    adminViewHospital, adminModifyHospital,
    adminDeleteHospital,adminRegisterStaffHospitalInfo,
    adminViewStaffHospitalInfo,adminModifyStaffHosInfo,adminDeleteStaffHosInfo,
    adminDeleteHospitalTag,adminLinkHospitalTag, adminCreateHospitalEstimation,
    adminDeleteHospitalEstimation,adminModifyHospitalEstimation,adminDeleteDoctor,
    adminModifyDoctor,adminCreateDoctor,adminRegisterDetailedHosInfo,
    adminDeleteDetailedHosInfo,adminDeleteThumbnail,adminCreateThumbnail,
    adminCreateHospitalImage,adminViewHospitalImages,adminDeleteHospitalImage,

    //리뷰
    adminViewReviewLists, adminSearchReviewLists,adminUnapprovedReviewCount,
    adminApproveReview, adminDeleteReview,adminViewReview,adminSearchUnapprovedReviewLists,

    //태그
    adminViewTagsList, adminSearchTags, adminDeleteTag, adminCreateTag,

    //Q&A
    adminSearchQuestionsList, adminDeleteQuestion
};
