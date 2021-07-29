function saveSearchNameToCookie(value){
    document.cookie = `search_name=${value}`;
}

function saveNickNameToCookie(value){
    document.cookie = `nick_name=${value}`;
}

function saveTokenToCookie(value){
    document.cookie = `token=${value}`;  
}

function saveMemberStatusToCookie(value){
    document.cookie = `member_status=${value}`;  
}

function getSearchNameFromCookie() {
  return document.cookie.replace(
    /(?:(?:^|.*;\s*)search_name\s*=\s*([^;]*).*$)|^.*$/,
    '$1',
  );
}

function getMemberStatusFromCookie() {
  return document.cookie.replace(
    /(?:(?:^|.*;\s*)member_status\s*=\s*([^;]*).*$)|^.*$/,
    '$1',
  );
}


function getNickNameFromCookie() {
  return document.cookie.replace(
    /(?:(?:^|.*;\s*)nick_name\s*=\s*([^;]*).*$)|^.*$/,
    '$1',
  );
}

function getTokenFromCookie() {
  return document.cookie.replace(
    /(?:(?:^|.*;\s*)token\s*=\s*([^;]*).*$)|^.*$/,
    '$1',
  );
}

function deleteCookie(value) {
  document.cookie = `${value}=; expires=Thu, 01 Jan 1970 00:00:01 GMT;`;
}

export{
    saveSearchNameToCookie,
    saveNickNameToCookie,
    saveTokenToCookie,
    saveMemberStatusToCookie,
    getSearchNameFromCookie,
    getNickNameFromCookie,
    getTokenFromCookie,
    getMemberStatusFromCookie,
    deleteCookie
};