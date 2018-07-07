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

    <link rel="stylesheet" href="${applicationScope.fileServer}/static/css/content/content-base.css">
    <style>
        .bmap {
            width: 100%;
            height:400px;
        }
        #form label {
            padding-left: 0;
            text-align: left;
        }

         a:link, a:visited {
             color: #06C;
         }


    </style>
</head>
<body>
<div class="content-header">
    <h5>${label}</h5>
</div>

<div class="content-body container-fluid">
    <form class="form-horizontal" action="${pageContext.request.contextPath}/web/${webId}/content/pic/save" id="form" method="post">
        <input type="hidden" name="id" value="${pic.id}">
        <input type="hidden" name="categoryId" value="${categoryId}">
        <input type="hidden" name="src" value="${pic.src}">
        <input type="hidden" name="webId" value="${webId}">
        <%--<div class="props">--%>
            <%--<div class="label">--%>
                <%--标题--%>
            <%--</div>--%>
            <%--<div class="control">--%>
                <%--<input type="text" name="title" value="${pic.title}">--%>
            <%--</div>--%>
        <%--</div>--%>
        <div class="form-group">
            <label class="col-sm-2 col-md-1 control-label">
                百度地址
            </label>
            <div class="col-sm-10 col-md-11">
                <input class="form-control" type="text" name="title" value= "${pic.title}" id="title">
            </div>
        </div>

        <div class="form-group">
            <label class="label-control">地图&nbsp;<a href="javascript:;" onclick="searchMap()">搜索</a></label>
            <div class="md-col-12">
                <div id="address" class="bmap"></div>
            </div>
        </div>
    </form>
</div>

<button class="btn btn-primary" type="button" onclick="publish();">保存</button>
</div>



</body>
<myfooter>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=2LPGvlD0ZCIX1tvApbGQCUlccLVdaAZX"></script>
    <script>
        var map = new BMap.Map("address");
        map.enableScrollWheelZoom(true);
        map.centerAndZoom(new BMap.Point(116.404, 39.915), 11);
        var local = new BMap.LocalSearch(map, {
            renderOptions:{map: map}
        });
        local.search("${pic.title}");


        function searchMap() {
            local.search($('#title').val());
        }

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
    </script>
</myfooter>
