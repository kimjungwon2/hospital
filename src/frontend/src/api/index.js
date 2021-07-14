import axios from 'axios';

const instance = axios.create({
    baseURL: process.env.VUE_APP_API_URL,
})

function signupUser(userData){
    return instance.post('signup', userData);
}

export { signupUser };