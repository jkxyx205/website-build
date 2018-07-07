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
    <link rel="stylesheet" href="${applicationScope.fileServer}/static/plugins/uploadify5/uploadifive.css">
    <style>
        .tip-info.video-info {
            position: absolute;
            top: 0px;
            left: 137px;
        }


        .list-icon-play {
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            -webkit-transform: translate(-50%, -50%);
            -moz-transform: translate(-50%, -50%);
            -ms-transform: translate(-50%, -50%);
            width: 40px;
            line-height: 40px;
            height: 40px;
            text-align: center;
            border-radius: 40px;
            background-color: rgba(0, 0, 0, .5);
        }
        .icon-play2 {
            font-size: 24px;
            color: #fff;

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
                <div class="photo-box photo-box-add" @click="save();">
                    <i class="iconfont icon-add"></i>
                </div>
            </div>
            <div class="container-photo-box col-xs-6 col-sm-4 col-md-3 col-lg-2" v-for="p in items" :data-id="p.id">
                <div class="photo-box photo-box-img">
                    <a :href="'javascript:play(' + p.id + ')'" :title="p.title">

                        <img class="list-img" :src="'${applicationScope.fileServer}/'+p.cover" :alt="p.title">

                        <div class="op-block">

                        </div>
                        <%--<div class="op-bar">--%>
                            <%--预览视频--%>
                        <%--</div>--%>
                        <div class="list-icon-play">
                            <i class="iconfont icon-play2"></i>
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
                <script :id="p.id" type="text/html">
                        {{p.embed}}
                 </script>
            </div>
        </div>
    </div>

</div>
</body>
<myfooter>
    <script src="${applicationScope.fileServer}/static/plugins/uploadify5/jquery.uploadifive.min.js"></script>
    <script src="${applicationScope.fileServer}/static/plugins/jquery-ui/jquery-ui.js"></script>
    <script src="${applicationScope.fileServer}/static/js/web/jquery.pic.js"></script>

    <script>
        var empty = {
            id: "",
            cover: "tpl/init/${aspectRatioW}x${aspectRatioH}/0.jpg",
            fullSrc: this.src,
            title: "",
            description: "",
            embed: ""
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
                    var url = 　"${pageContext.request.contextPath}/web/${webId}/content/video/delete/" + p.id;

                    layer.confirm('确定删除视频【'+p.title+'】？', {
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

//                editPanel.p = _.clone(empty)
//                $('#cover').attr("src", "")
            }
        })

        function play(id) {
            layer.open({
                type: 1,
                title: false,
//                closeBtn: 0,
                shadeClose: true,
                area: 'auto',
                maxWidth: 1024,
                content: $('#'+id).html()
            });
        }

        function open() {
            layer.open({
                type: 1,
                closeBtn: 0,
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
//                title: '视频管理',
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

            $('#video_upload').uploadifive({
                fileObjName: 'video_upload',
                height: 'auto',
                'auto' : true,
                'buttonText':'本地上传',
                'multi'        : false,
                removeCompleted:true,
                'fileSizeLimit' : '50MB',
                'fileType'     : 'video/*',
                'uploadScript'     : '${pageContext.request.contextPath}/web/${webId}/content/video/upload',
                'onSelect':function() {

                },
                'onUploadComplete' : function(file, data) {
                    data = JSON.parse(data)
                    if (!data.status) {
                        layer.msg('文件上传失败', {icon: 1})
                        return
                    }

                    var embed = []

                    embed.push('<video width="480" height="350" controls autoplay>')
                    embed.push('<source src="'+data.data.urlPath+'" type="video/mp4"/>')
                    embed.push('</video>')

                    editPanel.p.embed = embed.join('')
                }
            });


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

                    $.post('${pageContext.request.contextPath}/web/${webId}/content/video/sort/${categoryId}',{idsStr: idsStr.join(';')},  function (data) {

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
        <form action="${pageContext.request.contextPath}/web/${webId}/content/video/save" id="form" method="post">
            <input type="hidden" name="id" v-model="p.id">
            <input type="hidden" name="cover" v-model="p.cover">
            <input type="hidden" name="webId" value="${webId}">
            <input type="hidden" name="categoryId" value="${categoryId}">
            <div class="form-group">
                <label>封面</label>
                <div id="cover-div">
                    <img class="lazy" id="cover" name="cover" :src="'${applicationScope.fileServer}/' + p.cover">
                </div>
            </div>
            <div class="form-group">
                <label for="title">标题</label>
                <input type="text" class="form-control" id="title" placeholder="图片名称" name="title" v-model = "p.title">
            </div>
            <div class="form-group" style="position: relative">
                <label for="embed" style="margin-right: 30px;">视频地址</label>
                <input id="video_upload" name="video_upload" type="file">
                <span class="tip-info video-info">(支持mp4,m4v,webm格式,大小50M以内)</span>
                <textarea class="form-control" name="embed" id="embed" rows="4" v-model = "p.embed"></textarea>
            </div>
            <div class="form-group">
                <label for="description">描述</label>
                <textarea class="form-control" rows="3" id="description" name="description" v-model="p.description" placeholder="添加视频描述"></textarea>
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
                editPanel.p.cover = doc.filePath;
                editPanel.p.fullSrc = doc.remarks;
                if (!editPanel.p.title)
                    editPanel.p.title = doc.name
            }
        })

        //        $(".lazy").lazyload({
        ////            effect : "fadeIn",
        //        });
    </script>
</out>