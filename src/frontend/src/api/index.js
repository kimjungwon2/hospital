import axios from 'axios';
import { setInterceptors } from './common/interceptors';


function createInstance(){
     return axios.create({
         baseURL: process.env.VUE_APP_API_URL,
     });
}

function createInstanceWithURL(url){
    return axios.create({
        baseURL: `${process.env.VUE_APP_API_URL}${url}`,
    });
}

function createInstanceWithToken(url){
    const instance = axios.create({
        baseURL: `${process.env.VUE_APP_API_URL}${url}`,
    });
    return setInterceptors(instance);
}

export const hospital = createInstanceWithURL('hospital');
export const user = createInstanceWithToken('user');


const instance = createInstance();

function signupUser(userData){
    return instance.post('signup', userData);
}

function loginUser(userData){
    return instance.post('login',userData);
}

function searchHospital(searchName){
    return instance.get(`search/hospital/${searchName}`);
}

export { signupUser, loginUser, searchHospital };