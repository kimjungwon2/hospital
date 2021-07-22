// 병원과 관련된 API 함수.

import { hospital } from './index';

//병원 정보 보기.
function viewHospital(hospitalId){
    return hospital.get('/view/'+hospitalId);
}

//병원 추가 정보 보기
function viewStaffHosInfo(staffHosId){
    return hospital.get('/staffHosInfo/'+staffHosId);
}

//병원 리뷰 보기
function viewHospitalReview(hospitalId){
    return hospital.get('/review/'+hospitalId);
}

//병원 Q&A 보기
function viewHospitalQandA(hospitalId){
    return hospital.get('/question/'+hospitalId);
}


export { viewHospital, viewStaffHosInfo, viewHospitalReview, viewHospitalQandA };