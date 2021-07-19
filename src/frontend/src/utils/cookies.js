function saveSearchNameToCookie(value){
    document.cookie = `search_name=${value}`;
}

function getSearchNameFromCookie() {
  return document.cookie.replace(
    /(?:(?:^|.*;\s*)search_name\s*=\s*([^;]*).*$)|^.*$/,
    '$1',
  );
}

function deleteCookie(value) {
  document.cookie = `${value}=; expires=Thu, 01 Jan 1970 00:00:01 GMT;`;
}

export{
    saveSearchNameToCookie,
    getSearchNameFromCookie,
    deleteCookie
};