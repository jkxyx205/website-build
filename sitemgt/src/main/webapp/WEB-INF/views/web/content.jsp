<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<head>
    <link href="${applicationScope.fileServer}/static/plugins/left-menu/css/jquery-accordion-menu.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="${applicationScope.fileServer}/static/css/content/content.css">
</head>
<body>
    <div class="content-left-nav">
        <div style="text-align: center; padding: 10px 0; height: 51px;">
            <div class="btn-group" role="group" id="menu-btn-group">
                <div class="btn btn-primary" data-target="#jquery-accordion-menu">页面</div>
                <div class="btn btn-default" data-target="#container-bin">回收站</div>
            </div>
        </div>
        <div class="jquery-accordion-menu white" id="jquery-accordion-menu">
            <ul id="menu-list">
                <li class="active" id="-1"><a href="javascript:;" data-href="/web/${webId}/content/info?label=页眉页脚"><i class="iconfont icon-page"></i>|&nbsp;页眉页脚</a></li>
                <c:forEach  var="menuDto" items="${menuList}">
                    <li>
                        <a href="javascript:;">${menuDto.menu.title}</a>
                        <c:if test="${fn:length(menuDto.subMenu) gt 0 || fn:length(menuDto.pageComponentContentDtoList) gt 0}">
                            <ul class="submenu">

                                <c:forEach var="subMenuDto" items="${menuDto.subMenu}">
                                    <li>
                                        <a href="javascript:;">${subMenuDto.menu.title}</a>
                                        <!--三级菜单-->
                                        <c:if test="${fn:length(subMenuDto.subMenu) gt 0 || fn:length(subMenuDto.pageComponentContentDtoList) gt 0}">
                                            <ul class="submenu">
                                                <c:forEach var="thrMenuDto" items="${subMenuDto.subMenu}">
                                                    <li>
                                                        <a href="javascript:;">${thrMenuDto.menu.title}</a>
                                                        <c:if test="${fn:length(thrMenuDto.pageComponentContentDtoList) gt 0}">
                                                            <ul class="submenu">
                                                                <c:forEach var="cpn" items="${thrMenuDto.pageComponentContentDtoList}">
                                                                    <li id="${cpn.id}" data-content-id="${cpn.categoryId}"><a href="javascript:;" data-href="/web/${webId}/content/update/${cpn.id}/${cpn.componentId}/${cpn.contentType}/${cpn.categoryId}?page=1&label=${cpn.label}"><i class="iconfont icon-page"></i>|&nbsp;${cpn.label}</a></li>
                                                                </c:forEach>
                                                            </ul>
                                                        </c:if>
                                                    </li>
                                                </c:forEach>
                                            </ul>
                                        </c:if>
                                        <!--三级菜单 end-->

                                        <c:if test="${fn:length(subMenuDto.pageComponentContentDtoList) gt 0}">
                                            <ul class="submenu">
                                                <c:forEach var="cpn" items="${subMenuDto.pageComponentContentDtoList}">
                                                    <li id="${cpn.id}" data-content-id="${cpn.categoryId}"><a href="javascript:;" data-href="/web/${webId}/content/update/${cpn.id}/${cpn.componentId}/${cpn.contentType}/${cpn.categoryId}?page=1&label=${cpn.label}"><i class="iconfont icon-page"></i>|&nbsp;${cpn.label}</a></li>
                                                </c:forEach>
                                            </ul>
                                        </c:if>
                                    </li>
                                </c:forEach>
                                <c:forEach var="cpn" items="${menuDto.pageComponentContentDtoList}">
                                    <li id="${cpn.id}" data-content-id="${cpn.categoryId}"><a href="javascript:;" data-href="/web/${webId}/content/update/${cpn.id}/${cpn.componentId}/${cpn.contentType}/${cpn.categoryId}?page=1&label=${cpn.label}"><i class="iconfont icon-page"></i>|&nbsp;${cpn.label}</a></li>
                                </c:forEach>
                            </ul>
                        </c:if>
                    </li>
                </c:forEach>
            </ul>

        </div>
        <div id="container-bin">
            <div class="jquery-accordion-menu white" id="jquery-accordion-menu2">

            </div>
        </div>
        <%--<c:if test="${not empty pageName}">--%>
            <%--<div style="position: absolute; width: 100%; bottom: 0; left: 0; background: #5985ff;; text-align: center; height: 40px; line-height: 40px;">--%>
                <%--<a href="/web/${webId}/design?pageName=${pageName}&id=${pageComponentId}" style="color: #fff;">[返回页面设计]</a>--%>
            <%--</div>--%>
        <%--</c:if>--%>
    </div>
    <div class="editor-area" id="editor-area">
        <iframe id="iframe" src="/web/${webId}/content/info?label=页眉页脚" frameborder="0"></iframe>
    </div>

    <div id="navbar-collapse-container">
        <div class="s-navbar-collapse">
            <i class="iconfont icon-shouqi"></i>
        </div>
    </div>
</body>
<myfooter>
    <script src="${applicationScope.fileServer}/static/plugins/left-menu/js/jquery-accordion-menu.js" type="text/javascript"></script>
    <script src="${applicationScope.fileServer}/static/plugins/jquery.nicescroll/jquery.nicescroll.min.js"></script>
    <script src="${applicationScope.fileServer}/static/js/template.js"></script>
    <script>

        var content = {
            curId : -1,
            curCpnId : -1,
            menuWidth: 180,
            closeStatus: false,
            binStatus: false
        }
        $(function(){

            $("#jquery-accordion-menu").jqueryAccordionMenu({
                singleOpen: false,
                clickEffect: false,
                expandComplete: function (el) {//展开事件结束
                    $(el).getNiceScroll().resize()
                },
                clickComplete: function (li) {
                    iframeHref(li)
                }
            }).niceScroll();


            $('.icon-page').on('click', function(e) {
                    e.stopPropagation();
                    e.preventDefault();
                    window.location = '/web/${webId}/design?id='+$(this).parents('li').attr('id')
            })



            function iframeHref(li) {
                if ($(li).length == 0) {
                    $('#iframe').attr('src', '${applicationScope.fileServer}/static/html/bin.html');
                    $('.cpn-empty').remove()
                    return
                }


                var href = $(li).find('a').data('href');

                if(href) {
                    $(".content-left-nav li.active").removeClass("active")
                    $(li).addClass("active");
                    content.curId = $(li).data('content-id')
                    content.curCpnId = $(li).attr('id')
                    $('#iframe').attr('src', encodeURI(href));
                }
            }

            //设置当前编辑内容
            var pageComponentId = "${pageComponentId}";
            if (pageComponentId) {
                $('#'+pageComponentId).parents('li').click();
                content.curId = ${cpn.categoryId}
                content.curCpnId = ${cpn.id}
                $('#'+pageComponentId).click();
            }

//            $("#jquery-accordion-menu2 li a").append('<span class="submenu-indicator cpn-delete">删除</span>')

//            $("#menu-list>li:last-child .submenu").prepend('<li class="history-li"><a href="javascript:;">历史控件<span class="submenu-indicator cpn-empty"><i class="iconfont icon-empty"></i>清空</span></a></li>')
//            $("li.history-li").on('click touchstart', function (e) {
//                e.stopPropagation();
//                e.preventDefault();
//            })





            $('#menu-btn-group .btn').on('click', function() {
                $(this).addClass('btn-primary').siblings().removeClass('btn-primary').addClass('btn-default')

                $($(this).siblings().data('target')).hide()

                var targetPanel = $(this).data('target')

                $(targetPanel).show()

                if (!content.binStatus) {
                    $.ajax({
                        url: '/web/${webId}/content/bin',
                        async: false,
                        success: function (data) {
                            var html = template('bin-tpl', {"list": data});

                            $('#jquery-accordion-menu2').html(html)

                            $('#jquery-accordion-menu2 li').on('click', function () {
                                iframeHref($(this))
                            })

                            $('.cpn-delete').on('click', function (e) {
                                e.stopPropagation();
                                e.preventDefault();
                                var $li = $(this).parent()

                                layer.confirm('确认删除此控件?', {icon: 3, title:'提示'},function(index){
                                    var categoryId = $li.data('content-id')

                                    $.post('/web/${webId}/content/remove/'+categoryId+'', function(str){
                                        $li.remove()

                                        if(categoryId === content.curId) {
//                                            $('#iframe').attr('src', "");
                                            iframeHref($('#bin-list li:eq(0)'))
                                        }

                                        layer.close(index);
                                        layer.msg('控件删除成功', {icon: 1});
                                    })


                                });
                            })

                            $('.cpn-empty').on('click', function (e) {
//                                e.stopPropagation();
//                                e.preventDefault();
//                                var $li = $(this).parent()

                                layer.confirm('确认清空回收站?', {icon: 3, title:'提示'},function(index){

                                    $.post('/web/${webId}/content/removeAll/' + webId, function(str){
                                        $('#bin-list, .cpn-empty').remove()

//                                        $li.siblings().each(function() {
//                                            if ($(this).data('content-id') == content.curId) {
//                                                $('#iframe').attr('src', "");
//                                                return false;  //退出each循环
//                                            }
//                                        })
                                        $('#iframe').attr('src', '${applicationScope.fileServer}/static/html/bin.html');

                                        layer.msg('清空成功', {icon: 1})
                                        layer.close(index);

                                    })


                                });
                            })



                            content.binStatus = true
                        }
                    })




                }


                if ('#jquery-accordion-menu' == targetPanel) {
                    <%--$('#iframe').attr('src', '/web/${webId}/content/info?label=页眉页脚')--%>
                    var li = $('#menu-list li:eq(0)')
                    iframeHref(li)
                } else {
                    var li = $('#bin-list li:eq(0)')
                    iframeHref(li)
                }
            })

            $('#navbar-collapse-container').on('click', function() {
                if (content.closeStatus) {
                    $(this).removeClass('close-menu')
                    $('.content-left-nav, #menu-list').animate({"width": content.menuWidth})
                    $('#editor-area').animate({"padding-left": content.menuWidth})
                    $('#menu-btn-group').show()

                } else {

                    $(this).addClass('close-menu')
                    $('.content-left-nav, #menu-list').animate({"width": 0})
                    $('#editor-area').animate({"padding-left": 0})
                    $('#menu-btn-group').hide('slow')
                }

                content.closeStatus = !content.closeStatus

            })
        })

    </script>
    <script id="bin-tpl" type="text/html">
        {{if list.length > 0}}
        <div style="text-align: right; margin-right: 35px;">
            <a class="cpn-empty" href="javascript:;"><i class="iconfont icon-empty"></i>清空控件</a>
        </div>
        {{/if}}
        <ul id="bin-list">
            {{each list as cpn index}}
            <li id="{{cpn.id}}" data-content-id="{{cpn.categoryId}}">
                <a href="javascript:;" data-href="/web/${webId}/content/update/{{cpn.id}}/{{cpn.componentId}}/{{cpn.contentType}}/{{cpn.categoryId}}?page=1&label={{cpn.label}}">
                <i class="iconfont icon-brush"></i>{{cpn.label}}</a>
                <span class="submenu-indicator cpn-delete">删除</span>
            </li>
            {{/each}}
        </ul>
    </script>
</myfooter>