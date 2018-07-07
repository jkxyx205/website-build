function newWin(url, id) {
    var a = document.createElement('a');
    a.setAttribute('href', url);
    a.setAttribute('target', '_blank');
    a.setAttribute('id', id);
    // 防止反复添加
    if(!id || (id && !document.getElementById(id))) {
        document.body.appendChild(a);
    }

    a.click();
}
