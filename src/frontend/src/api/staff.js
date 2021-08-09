import { staff} from './index';

//병원 관계자 병원보기
function staffViewHospital(userId){
    return staff.get(userId+'/hospital/view');
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
function staffDeleteDetailedHosInfo(detailedHosId){
    return staff.delete('/detailedHos/delete/'+detailedHosId);
} 

//관리자 추가 정보 등록
function staffRegisterStaffHospitalInfo(staffHosInfo){
    return staff.post('/hospital/register/staff',staffHosInfo);
}

//병원 관계자 추가 정보 보기
function staffViewStaffHospitalInfo(staffHosInfoId){
    return staff.get('/staffHosInfo/'+staffHosInfoId);
} 

//병원 관계자 추가 정보 삭제
function staffDeleteStaffHosInfo(staffHosId){
    return staff.delete('/staffHosInfo/delete/'+staffHosId);
} 

//관리자 태그 생성
function staffCreateTag(tagData){
    return staff.post('/tag/create', tagData);
} 

//태그 이름 검색
function staffSearchTags(tagName){
    return staff.get('/tag/search/'+tagName);
}

//관리자 병원 태그 연결
function staffLinkHospitalTag(request){
    return staff.post('/hospital/tag/link',request);
} 


//병원 관계자 병원 태그 삭제
function staffDeleteHospitalTag(postTagId){
    return staff.delete('/hospital/tag/delete/'+postTagId);
} 

export{
    staffRegisterDetailedHosInfo,staffRegisterStaffHospitalInfo,
    staffSearchTags, staffLinkHospitalTag, staffCreateTag,
    staffModifyHospital,staffViewHospital,staffDeleteHospitalTag,staffDeleteDetailedHosInfo,
    staffViewStaffHospitalInfo,staffDeleteStaffHosInfo,
}