import axios from 'axios';

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

const instance = createInstance();
export const hospital = createInstanceWithURL('hospital');
export const user = createInstanceWithURL('user');

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