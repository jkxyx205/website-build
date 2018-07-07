<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
    <%--<link rel="stylesheet" href="${applicationScope.fileServer}/static/css/design/design.css">--%>
        <style>
            html,body, #inner-page, iframe {
                padding: 0;
                margin: 0;
                height: 100%;
                width: 100%;
            }
        </style>
</head>
<body>
<div id="inner-page" class="inner-page">
    <%--<div class="design-bar">--%>
        <%--<ul class="design-bar-left btn-operator">--%>
            <%--<li>--%>
                <%--<a id="design-bar-full" class="design-bar-full" href="javascript:;"><i class="iconfont icon-full"></i></a>--%>
            <%--</li>--%>
            <%--<li><a class="design-bar-save" id="save" href="javascript:;"><i class="iconfont icon-save"></i></a></li>--%>
            <%--<li>--%>
                <%--<a href="javascript:;"  style="width: 80px;"><label style="margin-bottom: 0;"><span style="color: #fff; margin-right: 5px;">预览模式</span><input id="status" type="checkbox"></label></a>--%>
            <%--</li>--%>

        <%--</ul>--%>
        <%--<ul class="design-bar-right" id="color-panel">--%>
            <%--<li><a id="c1" class="c1" data-block-id="1" href="javascript:;"></a></li>--%>
            <%--<li><a id="c2" class="c2" data-block-id="2" href="javascript:;"></a></li>--%>
            <%--<li><a id="c3" class="c3" data-block-id="3" href="javascript:;"></a></li>--%>
            <%--<li><a id="c4" class="c4" data-block-id="4" href="javascript:;"></a></li>--%>
            <%--<li><a id="c5" class="c5" data-block-id="5" href="javascript:;"></a></li>--%>
            <%--<li><a id="c6" class="c6" data-block-id="6" href="javascript:;"></a></li>--%>
            <%--<li><a id="c7" class="c7" data-block-id="7" href="javascript:;"></a></li>--%>
            <%--<li><a id="c8" class="c8" data-block-id="8" href="javascript:;"></a></li>--%>
        <%--</ul>--%>

        <%--<ul class="design-bar-center btn-operator">--%>
            <%--<li style="color: #fff;">预览页面，不能用于正式发布</li>--%>
            <%--<li>--%>
                <%--<a class="" id="dialogHeader" href="javascript:;"><i class="iconfont icon-yemei"></i>--%>
                    <%--<div class="operator-tip">页眉</div>--%>
                <%--</a>--%>
            <%--</li>--%>
            <%--<li><a class="" id="dialogLayout" href="javascript:;"><i class="iconfont icon-grid"></i><div class="operator-tip">页面布局</div></a></li>--%>
            <%--<li><a class="" id="dialogFooter" href="javascript:;"><i class="iconfont icon-yejiao"></i><div class="operator-tip">页脚</div></a></li>--%>
            <%--<li><a id="dialogStyle" href="javascript:;"><i class="iconfont icon-fengge"></i><div class="operator-tip">风格</div></a></li>--%>

        <%--</ul>--%>
    <%--</div>--%>
    <div class="design-container" id="design-container">
            <iframe id="iframe" name="iframe" src="/web/${webId}/view/page/index" frameborder="0"></iframe>
    </div>
</div>
</body>


<%--<myfooter>--%>
    <%--<script src="${applicationScope.fileServer}/static/plugins/Swiper-4.0.5/js/swiper.min.js"></script>--%>
    <%--<script src="${applicationScope.fileServer}/static/js/web/design/design.js"></script>--%>
<%--</myfooter>--%>
