<%--
  Created by IntelliJ IDEA.
  User: rick
  Date: 2017/7/12
  Time: 11:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<head>
    <%--<link rel="stylesheet" type="text/css" href="${applicationScope.fileServer}/static/plugins/vanillabox/theme/bitter/vanillabox.css"/>--%>
    <link rel="stylesheet" type="text/css" href="${applicationScope.fileServer}/static/css/content/pic-list.css"/>
    <style>
        div.photo-box>a {
            display: block;
            height: auto;
            padding-bottom: 100%;
            background-position: 50% 50%;
            background-size: contain;
            background-repeat: no-repeat;
        }
    </style>

</head>
<body>
<div class="content-header">
    <h5>${label}</h5>
</div>
<div class="content-body">
    <div class="photo-container container-fluid" id = "photo-container">
        <div class="row">
            <div class="col-xs-6 col-sm-4 col-md-3 col-lg-2">
                <div class="photo-box photo-box-add">
                    <i class="iconfont icon-add"></i>
                    <button class="btn btn-primary" @click="save();">单个添加</button>
                    <button class="btn btn-default" id="batchSave">批量添加</button>
                </div>
            </div>
            <div class="container-photo-box col-xs-6 col-sm-4 col-md-3 col-lg-2" v-for="p in items" :data-id="p.id">
                <div class="photo-box photo-box-img">
                    <a :href = "'${applicationScope.fileServer}/' + p.fullSrc" :title="p.title"  :style="'background-image: url(${applicationScope.fileServer}/'+p.src" data-lightbox="roadtrip" :data-title="p.title">
                        <div class="op-block">
                            <i class="iconfont icon-screen_expand"></i>
                        </div>
                        <div class="op-bar">
                            <div class="photo-title">
                                <h6 :title="p.title" v-text="p.title"></h6>
                            </div>
                        </div>
                    </a>
                    <%--<label class="label-drag" title="拖动排序"><i class="iconfont icon-menu"></i></label>--%>
                    <%--<div class="photo-title">--%>
                        <%--<h6 :title="p.title" v-text="p.title"></h6>--%>
                    <%--</div>--%>
                    <div class="photo-do">
                        <div @click="save(p)">
                            <div style="border-right: 1px solid #e7e7eb">
                                <i class="iconfont icon-brush"> </i>
                            </div>
                        </div><div @click="del(p)">
                        <i class="iconfont icon-empty"> </i>
                    </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>
</body>
<myfooter>
    <%--<script src="${applicationScope.fileServer}/static/plugins/vanillabox/jquery.vanillabox.js"></script>--%>
    <script src="${applicationScope.fileServer}/static/plugins/jquery-ui/jquery-ui.js"></script>
    <script src="${applicationScope.fileServer}/static/js/web/jquery.pic.js"></script>
    <script src="${applicationScope.fileServer}/static/js/web/jquery.link.js"></script>

    <script>
        var empty = {
            id: "",
            src: "tpl/init/${aspectRatioW}x${aspectRatioH}/0.jpg",
            fullSrc: this.src,
            title: "",
            description: "",
            href: ""
        }

        var photoContainer = new Vue({
            el: '#photo-container',
            data: {
                items: ${jqGrid.rows}
            },
            methods: {
                save: function (p) {
                    if (!p) {
                        p = empty
                        editPanel.status = 0
                    } else {
                        editPanel.status = 1
                        editPanel.edit = p
                        <%--$('#cover').attr('src', '${applicationScope.fileServer}' + p.src)--%>

                    }

                    editPanel.p = _.clone(p)

                    open()
                },
                del: function (p) {
                    var url = 　"${pageContext.request.contextPath}/web/${webId}/content/pic/delete/" + p.id;

                    layer.confirm('确定删除图片【'+p.title+'】？', {
                        btn: ['确定','取消'] //按钮
                    }, function(){
                        $.get(url,function() {
                            photoContainer.items = _.without(photoContainer.items, _.findWhere(photoContainer.items, {
                                id: p.id
                            }));

                            layer.msg('删除成功', {icon: 1});
                        })

                    }, function(){

                    });
                }
            },
            updated: function () {
//                if (photoContainer.items.length > 0)
//                    $(".photo-box>a").unbind("click").vanillabox()

                layer.msg('编辑成功', {icon: 1});

//                editPanel.p = _.clone(empty)
//                $('#cover').attr("src", "")
            }
        })

        $('#batchSave').PicDialog({
            aspectRatioW: ${aspectRatioW},
            aspectRatioH: ${aspectRatioH},
            webId: ${webId},
            multiSelect: true,
            success: function (list) {
                var pics = [];

                list.forEach(function (doc) {
                    pics.push($.extend({}, empty, {
                        src:  doc.filePath,
                        fullSrc:  doc.remarks,
                        title: doc.name,
                        categoryId : ${categoryId}
                    }))
                })

                $.ajax({
                    url: '${pageContext.request.contextPath}/web/${webId}/content/pic/batchSave',
                    contentType : 'application/json; charset=utf-8',
                    dataType : 'json',
                    data: JSON.stringify(pics),
                    method: 'post',
                    success: function (data) {
                        data.data.forEach(function (pic) {
                            photoContainer.items.push(pic)
                            layer.msg('编辑成功', {icon: 1});
                        })
                    }
                })
            }
        })



        function open() {
            layer.open({
                type: 1,
//                closeBtn: 0,
                btn: ['保存', '取消'], //可以无限个按钮
                yes: function(index, layero){
                    //check

                    layer.close(index)
                    //submit
                    $form.submit();
                },
                btn2: function () {
//                    $('#cover').attr("src", "")
                },
//                title: '图片管理',
                title: false,
                area: '600px',
//                offset: '10px',
//                shadeClose: true, //点击遮罩关闭
                content: $('#edit-panel')
            });
        }

        $(function () {
            <%--if (${jqGrid.rows}.length > 0)--%>
                <%--$('.photo-box>a').vanillabox();--%>

            //拖动排序
            $( ".row" ).sortable({
                cancel: ".portlet-toggle",
                items: ".container-photo-box",
                placeholder: "col-xs-6 col-sm-4 col-md-3 col-lg-2 portlet-placeholder",
                handle: ".label-drag, .photo-box-img",
                opacity: 0.6, //拖动时，透明度为0.6
                revert: true, //释放时，增加动画
                stop:function(event, ui) {
                    var idsStr = []

                    $('.container-photo-box').each(function () {
                        idsStr.push($(this).data('id'))
                    })
                    
                    $.post('${pageContext.request.contextPath}/web/${webId}/content/pic/sort/${categoryId}',{idsStr: idsStr.join(';')},  function (data) {
                        layer.msg('排序成功', {icon: 1});
                    })
                }
            }).disableSelection();



            $form = $('#form');

            $form.ajaxForm(function(data) {

                    if (editPanel.status === 0) {
                        photoContainer.items.push(data.data)
                        layer.msg('添加成功',{icon: 1});
                    } else {
                        $.extend(editPanel.edit, data.data)
                        layer.msg('编辑成功',{icon: 1});

                    }

                }
            )
        })



    </script>
</myfooter>

<out>
    <div id ="edit-panel">
        <form action="${pageContext.request.contextPath}/web/${webId}/content/pic/save" id="form" method="post">
            <input type="hidden" name="id" v-model="p.id">
            <input type="hidden" name="src" v-model="p.src">
            <input type="hidden" name="fullSrc" v-model="p.fullSrc">
            <input type="hidden" name="webId" value="${webId}">
            <input type="hidden" name="categoryId" value="${categoryId}">
            <div class="form-group">
                <label>图片</label>
                <div id="cover-div">
                    <img class="lazy" id="cover" name="cover" :src="'${applicationScope.fileServer}/' + p.src">
                </div>
            </div>
            <div class="form-group">
                <label for="title">标题</label>
                <input type="text" class="form-control" id="title" placeholder="图片名称" name="title" v-model = "p.title">
            </div>
            <div class="form-group">
                <label for="href">链接</label>
                <button id="link" type="button" class="btn-link">从内部链接中选择</button>
                <input type="text" class="form-control" name="href" v-model = "p.href" id="href" placeholder="http://">
            </div>
            <div class="form-group">
                <label for="description">描述</label>
                <textarea class="form-control" rows="3" id="description" name="description" v-model="p.description" placeholder="添加图片描述"></textarea>
            </div>
        </form>
    </div>

    <script>
        var editPanel = new Vue({
            el: '#edit-panel',
            data: {
                p: empty,
                status: 0 //0新增 1 编辑
            }
        })

        $('#cover-div').PicDialog({
            aspectRatioW: ${aspectRatioW},
            aspectRatioH: ${aspectRatioH},
            webId: ${webId},
            success: function (doc) {
                editPanel.p.src = doc.filePath;
                editPanel.p.fullSrc = doc.remarks;
                if (!editPanel.p.title)
                    editPanel.p.title = doc.name
            }
        })

         $('#link').LinkDialog({
            webId: ${webId},
            success: function (lk) {
                console.table(lk)
                editPanel.p.href = lk.href
            }
        })

        //        $(".lazy").lazyload({
        ////            effect : "fadeIn",
        //        });
    </script>
</out>