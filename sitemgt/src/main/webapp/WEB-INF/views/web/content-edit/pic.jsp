<%--
  Created by IntelliJ IDEA.
  User: rick
  Date: 2017/7/19
  Time: 14:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<head>
    <%
        String  realPath  =  "http://"  +  request.getServerName()  +  ":"  +  request.getServerPort()  +  request.getContextPath();
    %>
    <link rel="stylesheet" href="${applicationScope.fileServer}/static/css/content/pic-list.css">
</head>
<body>
<div class="content-header">
    <h5>${label}</h5>
</div>

<div class="content-body container-fluid">
    <form class="row" action="${pageContext.request.contextPath}/web/${webId}/content/pic/save" id="form" method="post">
        <input type="hidden" name="id" value="${pic.id}">
        <input type="hidden" name="categoryId" value="${categoryId}">
        <input type="hidden" name="src" value="${pic.src}" id="src">
        <input type="hidden" name="webId" value="${webId}">
        <div class="form-group">
            <label>图片</label>

            <div id="cover-div">
                <img class="lazy" style="height: 188px; width: auto" id="cover" name="cover" alt="$pic.title" src="/static/images/default-img.png" data-original="${fileServer}/${pic.src}">
            </div>
        </div>
        <div class="form-group">
            <label for="title">
                标题
            </label>
            <input class="form-control" type="text" id="title" name="title" value="${pic.title}">
        </div>
        <div class="form-group">
            <label for="href">
                链接
            </label>
                <button id="link" type="button" class="btn-link">从内部链接中选择</button>
               <input class="form-control" name="href" id="href" value="${pic.href}">
        </div>
        <div class="form-group">
            <label for="description">
                描述
            </label>
            <textarea class="form-control" name="description" id="description">${pic.description}</textarea>
        </div>

    </form>
</div>
<button class="btn btn-primary" type="button" onclick="publish();">保存</button>
</div>



</body>

<myfooter>
    <script src="${applicationScope.fileServer}/static/plugins/lazyload/lazyload.min.js"></script>
    <script src="${applicationScope.fileServer}/static/js/web/jquery.pic.js"></script>
    <script src="${applicationScope.fileServer}/static/js/web/jquery.link.js"></script>
    <script>

        $('#link').LinkDialog({
            webId: ${webId},
            success: function (lk) {
                console.table(lk)
                $('#href').val(lk.href)
            }
        })

        function publish() {
            $form.submit();
        }

        $(function () {
            $form = $('#form');
            $form.ajaxForm(function(data) {
                    layer.msg('编辑成功',{icon: 1});
                }
            )
        })

        $('#cover-div').PicDialog({
            aspectRatioW: 0,
            aspectRatioH: 0,
            webId: ${webId},
            success: function (doc) {
                $('#src').val(doc.filePath)
                $('#cover').attr('src',doc.urlPath);
            }
        })

        $(".lazy").lazyload({
//            effect : "fadeIn",
        });


    </script>
</myfooter>
