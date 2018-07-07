<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<head>
    <link rel="stylesheet" href="${applicationScope.fileServer}/static/css/web/site-baseinfo.css">
    <%--<link rel="stylesheet" href="https://v3.bootcss.com/dist/css/bootstrap.min.css">--%>


    <%--<link rel="stylesheet" href="${applicationScope.fileServer}/static/plugins/bootstrap/css/bootstrap.min.css">--%>
</head>
<body>
<div class="children-container">
<div class="container-fluid">
        <form class="form-horizontal baseinfo" id="info" method="post" action="${pageContext.request.contextPath}/web/${webId}/site/save">
            <input type="hidden" name="favicon" id="favicon" value="${site.favicon}">
            <div class="form-group">
                <label for="title" class="col-sm-2 control-label">站点名称</label>
                <div class="col-sm-8">
                    <input type="text" class="form-control" id="title" name="title" placeholder="必填" value="${site.title}">
                </div>
            </div>
            <div class="form-group">
                <label for="keywords" class="col-sm-2 control-label">站点关键字</label>
                <div class="col-sm-8">
                    <input type="text" class="form-control" id="keywords" name="keywords" placeholder="关键字用，隔开 如：点研，科技，云计算" value="${site.keywords}">
                </div>
            </div>
            <div class="form-group">
                <label for="description" class="col-sm-2 control-label">站点描述</label>
                <div class="col-sm-8">
                        <textarea name="description" class="form-control" id ="description" name="description" rows="3" placeholder="描述网站的主要内容">${site.description}</textarea>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">站点favicon图标</label>
                <div class="col-sm-8">
                    <div id="favicon-container">
                        <img class="lazy img-rounded" id="faviconImg" src="${applicationScope.fileServer}/tpl/init/1x1/0.jpg" alt="" data-original="${applicationScope.fileServer}/${site.favicon}">
                    </div>
                    <div class="tip-info">建议请上传32x32,格式为ico的图标</div>
                </div>
            </div>
            <div class="form-group">
                <label for="domain" class="col-sm-2 control-label">自定义域名</label>
                <div class="col-sm-8">
                    <input type="text" class="form-control" id="domain" name="domain" placeholder="如：tmall.com" value="${site.domain}">
                </div>
            </div>
            <div class="form-group">
                <label for="icp" class="col-sm-2 control-label">ICP备案号</label>
                <div class="col-sm-8">
                    <input type="text" class="form-control" id="icp" name="icp" placeholder="如：京ICP证100557号" value="${site.icp}">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">站点语言</label>
                <div class="col-sm-8">
                    <label class="radio-inline">
                        <input type="radio" name="lang" value="0" checked> <span>中文简体</span>
                    </label>
                    <label class="radio-inline">
                        <input type="radio" name="lang" value="1" <c:if test="${site.lang == 1}">checked</c:if>> <span>English</span>
                    </label>
                </div>
            </div>
            <div class="form-group btn-container">
                <div class="col-sm-offset-2 col-sm-8">
                    <button type="submit" class="btn btn-primary" id="btn-save">保存</button>
                </div>
            </div>

        </form>

</div>
</div>
</body>
<myfooter>
    <script src="${applicationScope.fileServer}/static/js/web/jquery.pic.js"></script>
    <script>
        $(function() {
            $('#favicon-container').PicDialog({
                aspectRatioW: 0,
                aspectRatioH: 0,
                webId: ${webId},
                success: function (doc) {
                    $('#faviconImg').attr('src', doc.urlPath)
                    $('#favicon').val(doc.filePath)
                }
            })

            var $info = $('#info');

            $info.ajaxForm(function(data) {
                    site.domain = $('#domain').val()
                    layer.msg('保存成功',{icon: 1});
                }
            )

//            $('#btn-save').on('click', function () {
//                $info.submit();
//            })

            if ('${param.domain}' === 'focus') {
                $('#domain').focus()
            }

        })

    </script>
</myfooter>
