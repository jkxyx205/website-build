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
    <link rel="stylesheet" href="${applicationScope.fileServer}/static/plugins/uploadify5/uploadifive.css">
    <style>
       .tip-info.video-info {
           position: absolute;
           top: 0px;
           left: 137px;
       }
    </style>
</head>
<body>
<div class="content-header">
    <h5>${label}</h5>
</div>

<div class="content-body container-fluid">
    <form class="row" action="${pageContext.request.contextPath}/web/${webId}/content/video/save" id="form" method="post">
        <input type="hidden" name="id" value="${video.id}">
        <input type="hidden" name="categoryId" value="${categoryId}">
        <input type="hidden" name="cover" value="${video.cover}" id="src">
        <input type="hidden" name="webId" value="${webId}">
        <div class="form-group">
            <label>封面</label>

            <div id="cover-div">
                <img class="lazy" style="height: 188px; width: auto" id="cover" name="cover" alt="$pic.title" src="/static/images/default-img.png" data-original="${fileServer}/${video.cover}">
            </div>
        </div>
        <div class="form-group">
            <label for="title">
                标题
            </label>
            <input class="form-control" type="text" id="title" name="title" value="${video.title}">
        </div>
        <div class="form-group" style="position: relative">
            <label for="embed">
                视频地址
            </label>
            <%--<input id="video_upload" name="video_upload" type="file">--%>
            <%--<span class="tip-info video-info">(支持mp4,m4v,webm格式,大小50M以内)</span>--%>
            <button class="btn btn-link" type="button" id="btn-link">添加视频</button>
            <textarea class="form-control" name="embed" id="embed" rows="4" readonly>${video.embed}</textarea>
        </div>
        <div class="form-group">
            <label for="description">
                描述
            </label>
            <textarea class="form-control" name="description" id="description">${video.description}</textarea>
        </div>
    </form>
    <script id="${video.id}" type="text/html">
        ${video.embed}
    </script>
</div>
<div class="btn-split-group">
    <button class="btn btn-info" type="button" onclick="play();">视频预览</button>
    <button class="btn btn-primary" type="button" onclick="publish();">保存</button>
</div>

</div>



</body>
<myfooter>
    <script src="${applicationScope.fileServer}/static/plugins/lazyload/lazyload.min.js"></script>
    <script src="${applicationScope.fileServer}/static/plugins/uploadify5/jquery.uploadifive.min.js"></script>
    <script src="${applicationScope.fileServer}/static/js/web/jquery.pic.js"></script>
    <script src="${applicationScope.fileServer}/static/js/web/jquery.video.js"></script>
    <script>
        function play(id) {
            layer.open({
                type: 1,
                title: false,
                shadeClose: true,
                area: 'auto',
                maxWidth: 1024,
                content: $('#${video.id}').html()
            });
        }

        function publish() {
            $form.submit();
        }

        $(function () {
            <%--$('#video_upload').uploadifive({--%>
                <%--fileObjName: 'video_upload',--%>
                <%--height: 'auto',--%>
                <%--'auto' : true,--%>
                <%--'buttonText':'本地上传',--%>
                <%--'multi'        : false,--%>
                <%--removeCompleted:true,--%>
                <%--'fileSizeLimit' : '50MB',--%>
                <%--'fileType'     : 'video/*',--%>
                <%--'uploadScript'     : '${pageContext.request.contextPath}/web/${webId}/content/video/upload',--%>
                <%--'onSelect':function() {--%>

                <%--},--%>
                <%--'onUploadComplete' : function(file, data) {--%>
                    <%--data = JSON.parse(data)--%>
                    <%--if (!data.status) {--%>
                        <%--layer.msg('文件上传失败', {icon: 1})--%>
                        <%--return--%>
                    <%--}--%>

                    <%--var embed = []--%>

                    <%--embed.push('<video width="480" height="350" controls autoplay>')--%>
                    <%--embed.push('<source src="'+data.data.urlPath+'" type="video/mp4"/>')--%>
                    <%--embed.push('</video>')--%>

                    <%--var html = embed.join('')--%>

                    <%--$('#embed').html(html)--%>

                    <%--$('#${video.id}').html(html)--%>
                <%--}--%>
            <%--});--%>


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


        $('#btn-link').VideoDialog({
            webId: ${webId},
            autoplay: true,
            success: function (html) {
//                alert(html)
                $('#embed').html(html)
                $('#${video.id}').html(html)
            }
        })


    </script>
</myfooter>
