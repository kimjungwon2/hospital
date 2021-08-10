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

export{
    staffRegisterDetailedHosInfo,staffRegisterStaffHospitalInfo,staffModifyStaffHosInfo,
    staffSearchTags, staffLinkHospitalTag, staffCreateTag,
    staffModifyHospital,staffViewHospital,staffDeleteHospitalTag,staffDeleteDetailedHosInfo,
    staffViewStaffHospitalInfo,staffDeleteStaffHosInfo,
    staffCreateDoctor,staffDeleteDoctor,staffModifyDoctor
}