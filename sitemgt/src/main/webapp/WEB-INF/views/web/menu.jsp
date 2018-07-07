<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<head>
    <link rel="stylesheet" href="${applicationScope.fileServer}/static/css/web/menu.css">
</head>
<body>
<div class="children-container">
    <div class="container-fluid">
    <div id="menu" class="menu-panel row">
        <ul class="first-menu">
            <li class="first-menu-item" v-for="menuDto in menuList">
                <a v-text="menuDto.menu.title" :title="menuDto.menu.title" :id="menuDto.menu.id" @click="edit($event, menuDto)"></a>
                <ul class="second-menu">
                    <li class="second-menu-item" v-for="subMenuDto in menuDto.subMenu">
                        <a v-text="subMenuDto.menu.title" :title="subMenuDto.menu.title" :id="subMenuDto.menu.id"  @click="edit($event, subMenuDto)"></a>
                        <ul class="third-menu">
                            <li  class="third-menu-item" v-for="thrMenuDto in subMenuDto.subMenu">
                                <a v-text="thrMenuDto.menu.title" :id="thrMenuDto.menu.id" :title="thrMenuDto.menu.title" @click="edit($event, thrMenuDto)"></a>
                            </li>
                            <div @click="add($event, subMenuDto.menu, subMenuDto.subMenu)"><a class="add">+</a></div>
                        </ul>
                    </li>
                    <div  @click="add($event, menuDto.menu, menuDto.subMenu)"><a class="add">+</a></div>
                </ul>

            </li>
            <div class="first-menu-item" @click="add($event,{},menuList)" v-show="menuList.length <10"><a class="add">+</a></div>
        </ul>
    </div>


    <div id = "edit-panel" class="menu-props-area row">

        <div class="col-md-12">
            <h1 class="menu-title">菜单设置</h1>
        </div>

        <div class="form-horizontal">
            <div class="form-group">
                <label for="menu-title" class="col-sm-2 control-label" >菜单名称</label>
                <div class="col-sm-3">
                    <input type="text" class="form-control" id="menu-title" v-model="menuDto.menu.title" placeholder="菜单名称不能为空">
                </div>
                <label v-show="menuDto.page.name != 'index'" class="col-sm-7 control-label" style="text-align: left;"><a href="javascript:;" class="btn-delete" @click="del(menuDto.menu.id)">删除菜单</a></label>
            </div>
            <div class="form-group" v-show="menuDto.page.name != 'index' && menuDto.subMenu.length == 0">
                <label class="col-sm-2 control-label">菜单类型</label>
                <div class="col-sm-10">
                    <label class="radio-inline">
                        <input type="radio" name="linkType" v-model="menuDto.menu.linkType" value="0"> <span>无动作</span>
                    </label>
                    <label class="radio-inline">
                        <input type="radio" name="linkType" v-model="menuDto.menu.linkType" value="1"> <span>页面</span>
                    </label>
                    <label class="radio-inline">
                        <input type="radio" name="linkType" v-model="menuDto.menu.linkType" value="2"> <span>链接</span>
                    </label>
                </div>
                <%--<div class="col-sm-offset-2 col-sm-9" v-show="menuDto.menu.linkType == 1">--%>
                    <%--<div class="page-panel form-group">--%>

                        <%--<ul class="design-img-container layout-img-container">--%>
                            <%--<li data-layout-id="1" class="design-img-box" :class="{active: 1 == (menuDto.page.layoutId)}" @click="layout(1)" style="background-position: 0 0;">--%>
                                <%--<div class="design-img-box-mask"></div>--%>
                            <%--</li>--%>
                            <%--<li data-layout-id="2" class="design-img-box" :class="{active: 2 == (menuDto.page.layoutId)}" @click="layout(2)" style="background-position: -99px 0;">--%>
                                <%--<div class="design-img-box-mask"></div>--%>
                            <%--</li>--%>
                            <%--<li data-layout-id="3" class="design-img-box" :class="{active: 3 == (menuDto.page.layoutId)}" @click="layout(3)" style="background-position: -198px 0;">--%>
                                <%--<div class="design-img-box-mask"></div>--%>
                            <%--</li>--%>
                            <%--<li data-layout-id="4" class="design-img-box" :class="{active: 4 == (menuDto.page.layoutId)}" @click="layout(4)" style="background-position: -297px 0;">--%>
                                <%--<div class="design-img-box-mask"></div>--%>
                            <%--</li>--%>
                            <%--<li data-layout-id="5" class="design-img-box" :class="{active: 5 == (menuDto.page.layoutId)}" @click="layout(5)" style="background-position: -396px 0;">--%>
                                <%--<div class="design-img-box-mask"></div>--%>
                            <%--</li>--%>
                            <%--<li data-layout-id="6" class="design-img-box" :class="{active: 6 == (menuDto.page.layoutId)}" @click="layout(6)" style="background-position: -495px 0;">--%>
                                <%--<div class="design-img-box-mask"></div>--%>
                            <%--</li>--%>
                            <%--<li data-layout-id="7" class="design-img-box" :class="{active: 7 == (menuDto.page.layoutId)}" @click="layout(7)" style="background-position: -594px 0;">--%>
                                <%--<div class="design-img-box-mask"></div>--%>
                            <%--</li>--%>
                            <%--<li data-layout-id="8" class="design-img-box" :class="{active: 8 == (menuDto.page.layoutId)}" @click="layout(8)" style="background-position: -693px 0;">--%>
                                <%--<div class="design-img-box-mask"></div>--%>
                            <%--</li>--%>
                            <%--<li data-layout-id="9" class="design-img-box" :class="{active: 9 == (menuDto.page.layoutId)}" @click="layout(9)" style="background-position: -792px 0;">--%>
                                <%--<div class="design-img-box-mask"></div>--%>
                            <%--</li>--%>
                            <%--<li data-layout-id="10" class="design-img-box" :class="{active: 10 == (menuDto.page.layoutId)}" @click="layout(10)" style="background-position: -891px 0;">--%>
                                <%--<div class="design-img-box-mask"></div>--%>
                            <%--</li>--%>
                        <%--</ul>--%>
                    <%--</div>--%>
                <%--</div>--%>
                <div class="col-sm-offset-2 col-sm-8" v-show="menuDto.menu.linkType == 2">
                    <div class="link-panel form-group">
                            <label class="col-sm-2 control-label">链接</label>
                            <div class="col-sm-10">
                                <input class="form-control" type="text" v-model="menuDto.menu.href" placeholder="http://">
                            </div>
                        <label class="col-sm-2 control-label"></label>
                        <div class="col-sm-8">
                            <button id="link" type="button" class="btn-link">从内部链接中选择</button>
                        </div>

                    </div>
                </div>
            </div>
            <div class="toolbar">
                <button class="btn btn-primary" onclick="saveConfig()">保存设置</button>
            </div>
        </div>
    </div>
    </div>
</div>

<%--<div class="toolbar">--%>
    <%--<button class="btn btn-primary" onclick="saveConfig()">保存设置</button>--%>
<%--</div>--%>
</body>
<myfooter>
    <script src="${applicationScope.fileServer}/static/plugins/jquery-ui/jquery-ui.js"></script>
    <script src="${applicationScope.fileServer}/static/js/web/jquery.link.js"></script>
    <script>
        var needSave = false;
        var init = true

        function pushSave() {
            if (!needSave) {
                needSave = true
                $(window).on("beforeunload", function(e) {
                    return true;
                });
            }
        }



        empty = {
            menu: {
                id: "",
                title: "",
                linkType:"0",
                href: ""
            },
            page:{
                layoutId: 1
            },
            subMenu:[],
        }

        editPanel = new Vue({
            el: '#edit-panel',
            data: {
                menuDto: empty
            },
            methods: {
                del: function (id) {
                    layer.confirm('删除菜单后对应的页面内容也会被清除。确定要删除菜单吗？', {icon: 0, area: '500px'},function(index){
                        layer.close(index);

                        $.post("${pageContext.request.contextPath}/web/${webId}/menu/delete/"+id, function () {
                            var $old = $("#" + id)
                            var $a = $old.parent().prev().find('>a')
                            if ($a.length === 0)
                                $a = $old.parent().next('li').find('>a')
                            if ($a.length === 0)
                                $a = $old.parents('li:eq(1)').find('>a')

                            delMenu(menu.menuList, id)

                            menu.status = 3
                            menu.activeId = $a.attr('id');

                        })

                    });
                },
                layout: function(id) {
                    var vm = this
                    if(vm.menuDto.page.layoutId == id) {
                        return
                    }

                    if (this.menuDto.menu.pageId) {
                        layer.confirm('修改页面布局，原页面的内容将被清除。确认修改页面布局吗？', {
                            icon: 0,
                            area: '500px',
                            btn: ['确定','取消'] //按钮
                        }, function(index){
                            vm.menuDto.page.layoutId = id
                            vm.menuDto.menu.pageId = -1 //表示修改layout
                            layer.close(index);
                        }, function(){
                        });
                    } else {
                        this.menuDto.page.layoutId = id
                    }

                }
            },
            updated: function () {
                if (!init)
                    pushSave()
                init =  false
            }
        })


        menu = new Vue({
            el: '#menu',
            data: {
                status: 2, //0 add 1 edit 2 normal 3 delete
                menuList: ${menuList}
            },
            methods: {
                edit: function (event, menuDto) {
                    setActive(event.currentTarget)
                },
                add: function (event, parent,list) {
                    if ((parent.linkType == "2" || (parent.linkType == "1" && parent.pageId )) && list.length == 0) {
                        layer.confirm('添加子菜单后，原菜单的内容将被清除。确定添加子菜单？', {
                            icon: 0,
                            area: '500px',
                            btn: ['确定','取消'] //按钮
                        }, function(index){
                            parent.linkType = "0"
                            process()
                            layer.close(index);
                        }, function(){

                        });


                    } else {
                        process()
                    }

                    var vm = this

                    this.d = $(event.currentTarget)

                    function process() {
                        var seq = 0;
                        if (list.length > 0) {
                            var prevMenuDto = list[list.length-1]
                            seq = prevMenuDto.menu.seq + 1
                        }

                        //save to db
                        var menu = {"title":"新建菜单","pid":parent.id,"linkType":"0","webId":${webId}, "seq": seq};

                        $.post("${pageContext.request.contextPath}/web/${webId}/menu/save",menu,function(data) {
                            var menuDto = data.data

                            list.push(menuDto)

                            vm.status = 0
                        })
                    }
                }
            },
            updated: function() {
                if (this.status == 0) {
                    setActive(this.d.prev().find(">a:last"))
                    bindSortableUl(this.d.prev().find("ul"))

                } else if (this.status == 3) {
                    setActive($('#' + this.activeId))
                }

                this.status = 2

            }
        })

        function getMenuDtoById(id) {
            var m = {}
            _iteratorMenu(menu.menuList, id)
            function _iteratorMenu(list, id) {
                if (list.length > 0) {
                    for (var i = 0; i< list.length; i++) {
                        var menuDto = list[i]
                        if (menuDto.menu.id == id){
                            m = menuDto;
                        } else {
                            _iteratorMenu(menuDto.subMenu, id)
                        }
                    }
                }
            }
            return m;
        }

        function setActive(a) {
            var $a = $(a)
            $('#menu a').removeClass()

            $a.addClass('active')
            $a.parent().click()

            editPanel.menuDto = getMenuDtoById($a.attr('id'))
        }

        function delMenu(list,id) {
            if (list.length > 0) {
                for (var i = 0; i< list.length; i++) {
                    var menuDto = list[i]
                    if (menuDto.menu.id === id){
                        list.splice(i, 1)
                    } else {
                        delMenu(menuDto.subMenu, id)
                    }
                }
            }
        }

        function getSortIds() {
            var ids = []
            var $ul =  $('ul.first-menu')
            _iterator($ul)
            return ids;
            function _iterator($ul) {
                if ($ul.length > 0) {
                    $ul.find('>li').each(function() {
                        ids.push(parseInt($(this).find('a').attr('id')))
                        _iterator($(this).find('ul:first'))
                    })
                }
            }
        }



        function bindSortableUl(selector) {
            $(selector).sortable({
                items: '>li',
                handle:'>a',
                placeholder: "portlet-placeholder",
                opacity: 0.6, //拖动时，透明度为0.6
                revert: true, //释放时，增加动画
                stop: function(event, ui) {
                    //console.log(getSortIds())
                    pushSave()
                }
            }).disableSelection();
        }

        bindSortableUl("#menu ul")
        setActive($('.first-menu>li:eq(0)>a:eq(0)'))


        $('#menu').delegate('li,.add', 'click', function(event) {
            event.stopPropagation();
            $(this).siblings().find("ul").hide()
            $(this).find('ul:first').show()
        })

        function saveConfig() {
            var ids = getSortIds()
//            debugger;
            _iteratorMenu(menu.menuList)
            function _iteratorMenu(list) {
                if (list.length > 0) {
                    for (var i = 0; i< list.length; i++) {
                        var menuDto = list[i]
                        menuDto.menu.seq = _.indexOf(ids, menuDto.menu.id)
                        _iteratorMenu(menuDto.subMenu)
                    }
                }
            }

            $.ajax({
                'type': 'POST',
                'url':"${pageContext.request.contextPath}/web/${webId}/menu/saveList",
                'contentType': 'application/json',
                'data': JSON.stringify(menu.menuList),
                'dataType': 'json',
                success: function(data) {
                    $(window).unbind("beforeunload")
                    layer.msg('保存成功',{icon: 1});
                }
            });

        }

        $('#link').LinkDialog({
            webId: ${webId},
            success: function (lk) {
                console.table(lk)
//                $('#href').val(lk.href)
                editPanel.menuDto.menu.href = lk.href
            }
        })

    </script>
</myfooter>