<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<head>
    <link rel="stylesheet" href="${applicationScope.fileServer}/static/css/content/info.css">
</head>
<body>
<div class="content-header">
    <h5>页眉页脚</h5>
</div>
<div class="content-body">
    <div class="container-fluid">
        <form class="form-horizontal" id="info" method="post" action="${pageContext.request.contextPath}/web/${webId}/content/info/save">
            <input type="hidden" name="logo" id="logo" value="${site.logo}">
            <input type="hidden" name="wechat" id="wechat" value="${site.wechat}">
            <div class="form-group">
                <label class="col-sm-2 control-label">公司LOGO</label>
                <div class="col-sm-8">
                    <div id="logo-container" class="pic-container">
                        <img class="lazy img-rounded" id="logoImg" src="${applicationScope.fileServer}/tpl/init/1x1/0.jpg" alt="" data-original="${applicationScope.fileServer}/${site.logo}">
                    </div>
                    <div class="tip-info">建议请上传大小200KB以内,格式为png的图片</div>
                </div>
            </div>
            <div class="form-group">
                <label for="tel" class="col-sm-2 control-label">电话</label>
                <div class="col-sm-8">
                    <input type="text" class="form-control" id="tel" name="tel" placeholder="如：021-78788233" value="${site.tel}">
                </div>
            </div>
            <div class="form-group">
                <label for="fax" class="col-sm-2 control-label">传真</label>
                <div class="col-sm-8">
                    <input type="text" class="form-control" id="fax" name="fax" placeholder="如：010-68788233" value="${site.fax}">
                </div>
            </div>

            <div class="form-group">
                <label for="email" class="col-sm-2 control-label">邮箱</label>
                <div class="col-sm-8">
                    <input type="text" class="form-control" id="email" name="email" placeholder="如：jkxyx205@gmail.com" value="${site.email}">
                </div>
            </div>
            <div class="form-group">
                <label for="addr" class="col-sm-2 control-label">地址</label>
                <div class="col-sm-8">
                    <input type="text" class="form-control" id="addr" name="addr" placeholder="如：苏州工业园区国际科技园" value="${site.addr}">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">官方公众号</label>
                <div class="col-sm-8">
                    <div id="wechat-container" class="pic-container">
                        <img class="lazy img-rounded" id="wechatImg" src="${applicationScope.fileServer}/tpl/init/1x1/0.jpg" alt="" data-original="${applicationScope.fileServer}/${site.wechat}">
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="weibo" class="col-sm-2 control-label">官方微博</label>
                <div class="col-sm-8">
                    <input type="text" class="form-control" id="weibo" name="weibo" placeholder="如：https://weibo.com/u/2396878314/home" value="${site.weibo}">
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
            $('#logo-container').PicDialog({
                aspectRatioW: 0,
                aspectRatioH: 0,
                webId: ${webId},
                success: function (doc) {
                    $('#logoImg').attr('src', doc.urlPath)
                    $('#logo').val(doc.filePath)
                }
            })

            $('#wechat-container').PicDialog({
                aspectRatioW: 0,
                aspectRatioH: 0,
                webId: ${webId},
                success: function (doc) {
                    $('#wechatImg').attr('src', doc.urlPath)
                    $('#wechat').val(doc.filePath)
                }
            })

            var $info = $('#info');

            $info.ajaxForm(function(data) {
                    layer.msg('编辑成功',{icon: 1});
                }
            )

//            $('#save-btn').on('click', function () {
//                $info.submit();
//            })

        })

    </script>
</myfooter>