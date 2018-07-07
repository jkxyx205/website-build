<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta name="renderer" content="webkit">
    <link rel="stylesheet" href="http://at.alicdn.com/t/font_417789_uahn6ykfntqk6gvi.css">
    <link rel="stylesheet" href="${applicationScope.fileServer}/static/plugins/bootstrap/css/bootstrap.min.css">

    <%
        String  realPath  =  "http://"  +  request.getServerName()  +  ":"  +  request.getServerPort()  +  request.getContextPath();
    %>
    <sitemesh:write property='head' />

</head>
<body>
<sitemesh:write property='body'/>


<script src="${applicationScope.fileServer}/static/js/jquery.min.js"></script>
<script src="${applicationScope.fileServer}/static/js/jquery.form.js"></script>
<%--<script src="${applicationScope.fileServer}/static/plugins/jquery.nicescroll/jquery.nicescroll.min.js"></script>--%>

<%--<script src="${applicationScope.fileServer}/static/js/common.js"></script>--%>
<%--<script src="${applicationScope.fileServer}/static/js/template.js"></script>--%>

<script src="${applicationScope.fileServer}/static/plugins/lazyload/lazyload.min.js"></script>

<script src="https://cdn.bootcss.com/underscore.js/1.8.3/underscore-min.js"></script>
<script src="https://cdn.bootcss.com/vue/2.4.2/vue.min.js"></script>

<script src="${applicationScope.fileServer}/static/plugins/layer/layer.js"></script>

<script src="${applicationScope.fileServer}/static/plugins/validation/jquery.validate.js"></script>
<script src="${applicationScope.fileServer}/static/plugins/validation/localization/messages_zh.min.js"></script>

<script src="${applicationScope.fileServer}/static/js/web/tool.js"></script>
<script>
    var ctx = '<%=realPath%>';
    var fileServer = '${applicationScope.fileServer}';
    var site = ${siteJSON}

        $(function () {

            var folder = '${aspectRatioW}x${aspectRatioH}'

//            $("body").niceScroll()

            if (1 == folder.length)
                folder = '1x1'

            var index = 0;

            $("img.lazy").lazyload({
                effect : "fadeIn",
                threshold: 100,
                placeholder_data_img: '${applicationScope.fileServer}/tpl/init/'+folder+'/0.jpg' //默认4x3的placeholder
            });


            layer.config({
                skin: 'site-class',
                shade: .5
            })

            $( document ).ajaxStart(function() {
                index = layer.load(2)
            });

            $(document).ajaxComplete(function() {
                layer.close(index)
            });
        })
</script>

<sitemesh:write property='myfooter'/>
</body>
<sitemesh:write property='out'/>
</html>

