<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<head>
    <link rel="stylesheet" href="${applicationScope.fileServer}/static/css/site/site-list.css">
</head>
<body>
<div class="children-container site-list">
<div class="container-fluid">
    <div class="row">
    <div class="col-xs-12">
        <div class="pull-right site-list-bar">
            <button class="btn btn-primary" onclick="add()">+&nbsp;新建站点</button>
        </div>
    </div>
    </div>

   <div class="row">

    <c:forEach var="site" items="${sites}">
        <div class="col-lg-3 col-md-3 col-sm-4 col-xs-6" id="${site.id}">
                <div class="site-wrapper">
                    <h5 class="status_${site.status}">${site.title}</h5>
                    <div class="site-detail">
                        <%--<a class="site-cover" style="display: block;"  href="/web/${site.id}/site" target="_blank" title="设计站点">--%>
                            <%--<img class="lazy" data-original="${fileServer}/site/${site.id}/html/index.png" alt="${site.title}">--%>
                                <%--<div class="site-cover-mask">--%>
                                <%--</div>--%>
                        <%--</a>--%>

                            <a class="site-cover" href="/web/${site.id}/site" target="_blank">
                            <img class="lazy" data-original="${fileServer}/site/${site.id}/html/index-thumbnail.png?version=${site.version}" alt="${site.title}">
                            <div class="site-cover-mask">
                            </div>
                                <button class="btn btn-primary btn-sm">设计网站</button>
                            </a>
    <div class="site-bar">
        <div class="site-bar-oprator pull-right">
            <%--<a href="/web/${site.id}/site" target="_blank" title="设计站点"><i class="iconfont icon-brush"></i></a>--%>
            <%--<a href="javascript:del(${site.id},'${site.title}');" title="删除站点"><i class="iconfont icon-empty"></i></a>--%>
            <div title="更多" class="site-more-items">
                <a href="javascript:;" class="site-more-items-a"><i class="iconfont icon-more4"></i></a>
                <ul class="more-items-bar">
                    <c:if test="${site.status == 1}">
                        <li><a href="javascript:stop(${site.id},'2');"><i class="iconfont icon-shijian"></i>&nbsp;&nbsp;停用</a></li>
                    </c:if>
                    <c:if test="${site.status == 2}">
                        <li><a href="javascript:stop(${site.id},'1');"><i class="iconfont icon-shijian"></i>&nbsp;&nbsp;启用</a></li>
                    </c:if>

                    <li><a href="javascript:del(${site.id},'${site.title}');"><i class="iconfont icon-empty"></i>&nbsp;&nbsp;删除</a></li>
                </ul>
            </div>
        </div>
        <div>发布日期:<span class="create-date"><fmt:formatDate pattern = "yyyy-MM-dd" value = "${site.updateDate}" /></span></div>
    </div>
</div>
</div>
</div>
</c:forEach>
</div>
</div>
</div>
</body>
<myfooter>
<script>
function add() {
layer.prompt({
formType: 0,
value: '',
title: '请输入站点名称',
offset: '100px',
area: ['800px', '350px'] //自定义文本域宽高
}, function(value, index, elem){
$.post('${pageContext.request.contextPath}/site/save',{title: value}, function (data) {
location.reload()
})
});

}




function del(id, title) {
layer.confirm('确认删除站点【'+title+'】?',{icon: 0},  function(index){
$.post('${pageContext.request.contextPath}/site/delete/' + id, function (data) {
$('#'+id).hide('slow')
})

layer.close(index);
});

}


function stop(id, status) {
    layer.load(2)
    $.post('${pageContext.request.contextPath}/site/status/' + id + '/' + status, function (data) {
        location.reload()
    })

}

$(function () {
    $('.site-more-items-a').on('click', function () {
        $(this).siblings('.more-items-bar').show()
    })

    $('.more-items-bar').on('mouseleave', function () {
        $(this).hide()
    })
})
</script>
</myfooter>


