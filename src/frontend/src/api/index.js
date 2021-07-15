import axios from 'axios';

const instance = axios.create({
    baseURL: process.env.VUE_APP_API_URL,
})

function signupUser(userData){
    return instance.post('signup', userData);
}

function loginUser(userData){
    return instance.post('login',userData);
}

export { signupUser, loginUser };