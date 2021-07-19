// 병원과 관련된 API 함수.

import { hospital } from './index';

//병원 정보 보기.
function viewHospital(hospitalId){
    return hospital.get('/view/'+hospitalId);
}

export { viewHospital };