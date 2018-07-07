<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <title>${site.title}</title>
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
        <li><a href="/site/list"><i class="iconfont icon-home"></i>&nbsp;点研智企</a></li>
         <%--<li><a href="/site/list">建站</a></li>--%>
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
        <div class="site-banner inner">
            <%--<img class="site-banner-logo lazy" data-original="${applicationScope.fileServer}/${site.logo}" alt="${site.title}" src="${applicationScope.fileServer}/tpl/init/1x1/0.jpg">--%>
            <span class="site-banner-title">${site.title}</span>
        </div>
        <div class="site-operator">
            <ul>
                <li><a href="/web/${webId}/view" target="_blank"><i class="iconfont icon-view" style="font-size: 16px"></i><span>网站预览</span></a></li>
                <li><a id="sitePublish" href="javascript:;"><i class="iconfont icon-publish"></i><span>网站发布</span></a></li>
            </ul>
            <div class="clearfix"></div>
        </div>


    </div>
    <div class="site-body">
        <div class="site-body-left" id = "site-body-left">
            <div class="site-expend"><a href="javascript:;" id="menu-expend"><i class="iconfont icon-shrinkbar"></i></a></div>
            <ul class="site-body-left-menu">
                <li>
                    <a href="${pageContext.request.contextPath}/web/${webId}/site" class="side-bar" data-title="site"><i class="iconfont icon-earth"></i><span>站点信息</span></a>
                    <div class="side-bar-detail"><span>网站信息</span></div>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/web/${webId}/menu" class="side-bar" data-title="menu"><i class="iconfont icon-nav"></i><span>菜单管理</span></a>
                    <div class="side-bar-detail"><span>菜单管理</span></div>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/web/${webId}/design" class="side-bar" data-title="design"><i class="icon iconfont icon-huabi"></i><span>页面设计</span></a>
                    <div class="side-bar-detail"><span>页面设计</span></div>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/web/${webId}/content" class="side-bar" data-title="content"><i class="iconfont icon-text"></i><span>内容维护</span></a>
                    <div class="side-bar-detail"><span>内容维护</span></div>
                </li>
                <%--<li>--%>
                    <%--<a href="${pageContext.request.contextPath}/web/${webId}/gallery?pid=1" class="side-bar" data-title="gallery"><i class="iconfont icon-buju"></i><span>图库维护</span></a>--%>
                    <%--<div class="side-bar-detail"><span>图库维护</span></div>--%>
                <%--</li>--%>
            </ul>
            <%--<ul class="site-body-left-menu site-body-left-bottom">--%>
                <%--<li>--%>
                    <%--<a href="${pageContext.request.contextPath}/web/${webId}/setting" class="side-bar"><i class="iconfont icon-setting"></i><span>系统设置</span></a>--%>
                    <%--<div class="side-bar-detail"><span>系统设置</span></div>--%>
                <%--</li>--%>

            <%--</ul>--%>
        </div>
        <div class="site-body-right" id="site-body-right">
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



<script>
    var realPath = '<%=realPath%>';
    var fileServer = '${applicationScope.fileServer}';
    var webId = ${webId};
    var site = ${siteJSON}

    $(function() {
        $('a.side-bar[data-title=${menu}]').addClass('active')

        $("img.lazy").lazyload({
            effect : "fadeIn",
            threshold: 100,
            placeholder_data_img: '${applicationScope.fileServer}/tpl/init/4x3/0.jpg' //默认4x3的placeholder
        });

        $('#sitePublish').on('click', function () {


            //check if has custom domain
            if(!site.domain) {
                layer.confirm('您需要完善【自定义域名】才能使用发布功能，现在前往完善?', {icon: 2, title:'提示'}, function(index){
                    layer.load(2)
                    location.href = "/web/${webId}/site?domain=focus"
                });
                return
            }

            layer.load(2)

            $.ajax({
                url:"/site/checkPublishStatus/${webId}",
                method: 'get',
                async: false,
                global: false,
                success: function (data) {
                    layer.closeAll('loading')

                    if (data.data.length> 0) {
                        layer.confirm('您有页面【'+data.data.join(',')+'】尚未添加任何控件，确认现在发布吗?', {icon: 0, title:'提示'}, function(index){
                            layer.close(index)
                            focusPublish()
                        });
                    } else {
                        focusPublish()
                    }
                }
            })





            function focusPublish() {
                if(typeof design === 'undefined') {
                    publish()
                } else {
                    design.save(function () {
                        publish()
                    })
                }

                function publish() {
                    $.get("/tool/publish/"+site.id, function (data) {
                        layer.open({
                            type: 1,
                            title: false,
                            shadeClose: true, //点击遮罩关闭
                            content: '<div style="padding: 30px 50px; text-align: center;"><div><span>发布成功!</span>&nbsp;|&nbsp;<a href="'+site.visit+'" target="_blank" style="color: blue;">访问站点</a></div></div>',
                        });
                    })
                }
            }

        })

        layer.config({
            skin: 'site-class',
            shade: .5
        })


        $( document ).ajaxStart(function() {
            layer.load(2)
        });

        $(document).ajaxComplete(function() {
            layer.closeAll('loading')
        });
    });
</script>
<script src="${applicationScope.fileServer}/static/js/web/tool.js"></script>
<script src="${applicationScope.fileServer}/static/js/web/base.js"></script>

<sitemesh:write property='myfooter'/>
</body>
<sitemesh:write property='out'/>
</html>