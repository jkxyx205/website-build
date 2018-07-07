<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <title>点研建站智能平台</title>
    <link rel="shortcut icon" href="${applicationScope.fileServer}/static/favicon.ico" type="image/x-icon" />
    <link rel="stylesheet" href="http://at.alicdn.com/t/font_417789_uahn6ykfntqk6gvi.css">
    <link rel="stylesheet" href="${applicationScope.fileServer}/static/plugins/bootstrap/css/bootstrap.min.css">


    <%
        String  realPath  =  "http://"  +  request.getServerName()  +  ":"  +  request.getServerPort()  +  request.getContextPath();
    %>

    <sitemesh:write property='head' />

</head>
<body id="parent-body">
    <div class="platform-banner">
        <ul class="platform-menu">
            <li><a href="javascript:;"><i class="iconfont icon-home"></i>&nbsp;点研智企</a></li>
            <%--<li><a href="#">建站</a></li>--%>
            <%--<li><a href="#">ERP</a></li>--%>
            <%--<li><a href="#">OA</a></li>--%>
            <%--<li><a href="#">进销存</a></li>--%>
            <%--<li><a href="#">电商</a></li>--%>
        </ul>
        <ul class="userinfo">
            <li><a href="#">消息</a></li>
            <li class="nav-user-account">
                <a id="account" class="account" href="javascript:;">jkxyx205的账号
                    <span class="arrow"></span>
                </a>
                <div class="more-items" id="more-items">
                    <ul>
                        <li><a href="#">账号管理</a></li>
                        <li><a href="#">退出</a></li>
                    </ul>
                </div>
            </li>
        </ul>
    </div>
    <div class="site-container" id="site-container">
        <div class="site-header">
            <div class="site-banner"><img class="site-banner-logo" src="${applicationScope.fileServer}/static/images/logo.png" alt="">
                <span>点研智能建站平台</span>
            </div>
        </div>
        <div class="site-body">
            <%--<div class="site-body-left" id = "site-body-left">--%>
                <%--<div class="site-expend"><a href="javascript:;" id="menu-expend"><i class="iconfont icon-expand"></i></a></div>--%>
                <%--<ul class="site-body-left-menu">--%>
                    <%--<li>--%>
                        <%--<a href="javascript:;" class="side-bar active"><i class="iconfont icon-web"></i><span>我的站点</span></a>--%>
                        <%--<div class="side-bar-detail"><span>我的网站</span></div>--%>
                    <%--</li>--%>
                <%--</ul>--%>
                <%--<ul class="site-body-left-menu site-body-left-bottom">--%>
                    <%--<li>--%>
                        <%--<a href="javascript:;" class="side-bar"><i class="iconfont icon-setting"></i><span>系统设置</span></a>--%>
                        <%--<div class="side-bar-detail"><span>系统设置</span></div>--%>
                    <%--</li>--%>

                <%--</ul>--%>
            <%--</div>--%>
            <div class="site-body-right" id="site-body-right" style="width: 100%">
                <div id="inner-page" class="inner-page">
                    <sitemesh:write property='body'/>
                </div>
            </div>
        </div>
    </div>

<script src="${applicationScope.fileServer}/static/js/jquery.min.js"></script>
<script src="${applicationScope.fileServer}/static/js/jquery.form.js"></script>
<script src="${applicationScope.fileServer}/static/plugins/validation/jquery.validate.js"></script>
<script src="${applicationScope.fileServer}/static/plugins/validation/localization/messages_zh.min.js"></script>
<script src="${applicationScope.fileServer}/static/plugins/lazyload/lazyload.min.js"></script>

<script src="https://cdn.bootcss.com/underscore.js/1.8.3/underscore-min.js"></script>
<script src="https://cdn.bootcss.com/vue/2.4.2/vue.min.js"></script>
<script src="${applicationScope.fileServer}/static/plugins/layer/layer.js"></script>
<script src="${applicationScope.fileServer}/static/js/web/base.js"></script>



<script>
    var realPath = '<%=realPath%>';
    var fileServer = '${applicationScope.fileServer}';
    $(function() {
        <%--$('a.menu-item[data-title=${menu}]').addClass('active')--%>

        $("img.lazy").lazyload({
            effect : "fadeIn",
            threshold: 200,
            placeholder_data_img: '${applicationScope.fileServer}/tpl/init/14x5/0.jpg' //默认14x5的placeholder
        });

        layer.config({
            skin: 'site-class',
            shade: .5
        })

        $( document ).ajaxStart(function(e) {
            this.ajaxIndex = layer.load(2)
        });

        $(document).ajaxComplete(function() {
            layer.close(this.ajaxIndex)
        });
    });
</script>
<sitemesh:write property='myfooter'/>
</body>
<sitemesh:write property='out'/>
</html>