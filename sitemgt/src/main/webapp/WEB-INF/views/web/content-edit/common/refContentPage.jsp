<%--
  Created by IntelliJ IDEA.
  User: rick
  Date: 2018/2/28
  Time: 14:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <style>
        .msg {
            position: absolute;
            left: 50%;
            top: 50%;
            -webkit-transform: translate(-50%, -50%);
            -moz-transform: translate(-50%, -50%);
            -ms-transform: translate(-50%, -50%);
            -o-transform: translate(-50%, -50%);
            transform: translate(-50%, -50%);
            text-align: center;

        }
        .msg-label {
            line-height: 40px;
        }

        .msg img {
            height: 100px;
        }
    </style>
</head>
<body>
    <%--<button class="btn btn-link" onclick="gotoEditPage()">设计页面</button>--%>
    <div class="msg">
        <img src="${applicationScope.fileServer}/static/images/empty-bin.png" alt="">
        <div class="msg-label">该控件为引用控件，修改内容请到被引用控件处。<a href="javascript:;" onclick="gotoSourcePage()">点击前往</a></div>
    </div>

    <script>
        <%--function gotoEditPage() {--%>
            <%--window.parent.location = '/web/${webId}/design?id='+window.parent.content.curCpnId--%>
        <%--}--%>

        function gotoSourcePage() {
            window.parent.location = '/web/${webId}/content?id=${minCpnId}'
        }
    </script>
</body>
</html>
