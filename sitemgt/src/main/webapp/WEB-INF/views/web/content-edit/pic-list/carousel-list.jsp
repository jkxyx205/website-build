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
    <link rel="stylesheet" href="${applicationScope.fileServer}/static/plugins/noUiSlider/nouislider.min.css">
    <link rel="stylesheet" href="${applicationScope.fileServer}/static/plugins/bgrins-spectrum/spectrum.css" type="text/css" />
    <link rel="stylesheet" type="text/css" href="${applicationScope.fileServer}/static/css/content/pic-list.css"/>

    <style>

        .noUi-horizontal .noUi-tooltip {
            top: 16px;
            left: 50%;
        }

        #cover-div  {
            width: 100%;
        }

        .box-mask {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: #000;
        }



        .banner-text {
            position: absolute;
            top: 50%;
            width: 270px;
            transform: translateY(-50%);
            -webkit-transform: translateY(-50%);
            -moz-transform: translateY(-50%);
            -ms-transform: translateY(-50%);
        }

        .banner-text.banner-center {
            left: 50%;
            transform: translate(-50%, -50%);
            -webkit-transform: translate(-50%, -50%);
            -moz-transform: translate(-50%, -50%);
            -ms-transform: translate(-50%, -50%);
            text-align: center;
        }

        .banner-text.banner-left {
            left: 10%;
            text-align: left;
        }

        .banner-text.banner-right {
            right: 10%;
            text-align: right;
        }


        .banner-text h1, p {
            font-size: 12px;
            font-weight: normal;
            margin: 0;
            padding: 0;
            color: #ffff;
            word-wrap:break-word;
        }

        .banner-text h1 {
            margin: 0 0 10px 0;
        }

        .title-font-set {
            position: absolute;
            right: 0;
            top: -10px;
        }

        .title-font-set input[type=text], select {
            width: auto;
            height: 28px;
            margin-right: 5px;
            float: left;
        }

        #edit-panel .form-group {
            position: relative;
        }



    </style>
</head>
<body>
<div class="content-header">
    <h5>${label}</h5>
</div>
<div class="content-body">
<%--<c:if test="${not empty minCpnId}">--%>
    <%--<button class="btn btn-link" onclick="gotoEditPage()">设计页面</button>--%>
<%--</c:if>--%>
<%--<c:if test="${status > 1}">--%>
    <%--<div class="alert alert-warning" role="alert">修改该控件内容，其他引用控件也会相应修改！</div>--%>
<%--</c:if>--%>
    <div class="photo-container container-fluid" id = "photo-container">
    <div class="row">
        <div class="col-xs-6 col-sm-4 col-md-3 col-lg-2">
            <div class="photo-box photo-box-add" style="height: 100px; line-height: 100px;">
                    <i class="iconfont icon-add"></i>
                    <button class="btn btn-primary" @click="save();">单个添加</button>
                    <button class="btn btn-default" id="batchSave">批量添加</button>
            </div>
        </div>
        <div class="container-photo-box col-xs-6 col-sm-4 col-md-3 col-lg-2" v-for="p in items" :data-id="p.id">
            <div class="photo-box photo-box-img">
                <a :href = "'${applicationScope.fileServer}/' + p.fullSrc" :title="p.title" data-lightbox="roadtrip" :data-title="p.title">

                    <img class="list-img" :src="'${applicationScope.fileServer}/'+p.src" :alt="p.title">

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
    <script src="${applicationScope.fileServer}/static/plugins/noUiSlider/nouislider.min.js"></script>
    <script src="${applicationScope.fileServer}/static/plugins/wNumb.js"></script>
    <script src="${applicationScope.fileServer}/static/plugins/bgrins-spectrum/spectrum.js"></script>
    <script src="${applicationScope.fileServer}/static/js/web/jquery.pic.js"></script>
    <script src="${applicationScope.fileServer}/static/js/web/jquery.link.js"></script>

    <script>

        <%--function gotoEditPage() {--%>
            <%--window.parent.location = '/web/${webId}/design?id='+window.parent.content.curCpnId--%>
        <%--}--%>

        var photoContainer = new Vue({
            el: '#photo-container',
            data: {
                items: ${jqGrid.rows}
            },
            methods: {
                save: function (p) {

                    if (!p) {
                        p = {
                            id: "",
                            src: "tpl/init/14x5/0.jpg",
                            fullSrc: this.src,
                            title: "",
                            description: "",
                            href: "",
                            focus: false,
                            focus2: false,
                            options: {
                                position:'banner-center',
                                opacity: 0.15,
                                fontSize: "36px",
                                fontStyle: 'normal',
                                fontFamily: '微软雅黑',
                                color: '#ffffff',
                                fontSize2: "20px",
                                fontStyle2: 'normal',
                                fontFamily2: '微软雅黑',
                                color2: '#ffffff'
                            }

                        }
                        editPanel.status = 0
                    } else {
                        editPanel.status = 1
                        editPanel.edit = p
                    }

                    editPanel.p = p


                    if ('string' === typeof editPanel.p.options)
                        editPanel.p.options = JSON.parse(editPanel.p.options)

                    Vue.set(editPanel.p, "focus", false)
                    Vue.set(editPanel.p, "focus2", false)

                    open()
                },
                del: function (p) {
                    var url = 　"${pageContext.request.contextPath}/web/${webId}/content/pic/delete/" + p.id;

                    layer.confirm('确定删除图片【'+p.title+'】？', {
                        icon: 2,
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


            }
        })

        $('#batchSave').PicDialog({
            aspectRatioW: 14,
            aspectRatioH: 5,
            webId: ${webId},
            multiSelect: true,
            success: function (list) {
                var pics = [];

                list.forEach(function (doc) {
                    var pic = {}
                    pic.src = doc.filePath;
                    pic.fullSrc = doc.remarks;
                    pic.title = doc.name
                    pic.categoryId = ${categoryId}
                    pic.description = ""
                    pic.href = ""
                    pic.options = JSON.stringify(editPanel.p.options)
                    pics.push(pic)
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
                success: function() {
                    opacity.noUiSlider.set(editPanel.p.options.opacity * 100);

                    $("#colorSelector").spectrum("set", editPanel.p.options.color);
                    $("#colorSelector").spectrum("option", "showPaletteOnly", true);

                    $("#colorSelector2").spectrum("set", editPanel.p.options.color2);
                    $("#colorSelector2").spectrum("option", "showPaletteOnly", true);
                },
//                title: '轮播图管理',
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
            <input type="hidden" name="options" :value="JSON.stringify(p.options)">
            <div class="form-group">
                <div id="cover-div" title="点击上传图片">
                    <img class="lazy" id="cover" name="cover" :src="'${applicationScope.fileServer}/' + p.src">
                    <div class="box-mask" :style="'opacity:' + p.options.opacity"></div>
                    <div :class="'banner-text '+p.options.position">
                        <h1 v-text="p.title" :style="'font: '+p.options.fontStyle+' '+parseInt(p.options.fontSize) * 0.6+'px '+p.options.fontFamily+';color:'+p.options.color+''"></h1>
                        <p v-text="p.description" :style="'font: '+p.options.fontStyle2+' '+parseInt(p.options.fontSize2) * 0.6 +'px '+p.options.fontFamily2+';color:'+p.options.color2+''"></p>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="title">标题</label>
                <input type="text" class="form-control" id="title" placeholder="图片名称" name="title" v-model = "p.title">
                <div class="title-font-set" v-show="p.title != '' && p.focus">
                    <select v-model="p.options.fontFamily">
                        <option value="Arial">Arial</option>
                        <option value="微软雅黑">微软雅黑</option>
                        <option value="宋体">宋体</option>
                    </select>
                    <select v-model="p.options.fontStyle">
                        <option value="normal">正常</option>
                        <option value="italic">斜体</option>
                        <option value="bold">粗体</option>
                        <option value="bold italic">粗体 斜体</option>
                    </select>
                    <select v-model="p.options.fontSize">
                        <option value="20px">20px</option>
                        <option value="24px">24px</option>
                        <option value="26px">26px</option>
                        <option value="28px">28px</option>
                        <option value="36px">36px</option>
                        <option value="48px">48px</option>
                        <option value="72px">72px</option>
                    </select>
                    <input type="text" id="colorSelector">
                </div>
            </div>
            <div class="form-group">
                <label for="href">链接</label>
                <button id="link" type="button" class="btn-link">从内部链接中选择</button>
                <input type="text" class="form-control" name="href" v-model = "p.href" id="href" placeholder="http://">
            </div>
            <div class="form-group">
                <label for="description">描述</label>
                <textarea class="form-control" rows="3" id="description" name="description" v-model="p.description" placeholder="添加图片描述"></textarea>
                <div class="title-font-set" v-show="p.description != '' && p.focus2 ">
                    <select v-model="p.options.fontFamily2">
                        <option value="Arial">Arial</option>
                        <option value="微软雅黑">微软雅黑</option>
                        <option value="宋体">宋体</option>
                    </select>
                    <select v-model="p.options.fontStyle2">
                        <option value="normal">正常</option>
                        <option value="italic">斜体</option>
                        <option value="bold">粗体</option>
                        <option value="bold italic">粗体 斜体</option>
                    </select>
                    <select v-model="p.options.fontSize2">
                        <option value="20px">20px</option>
                        <option value="24px">24px</option>
                        <option value="26px">26px</option>
                        <option value="28px">28px</option>
                        <option value="36px">36px</option>
                        <option value="48px">48px</option>
                        <option value="72px">72px</option>
                    </select>
                    <input type="text" id="colorSelector2">
                </div>
            </div>
            <div class="form-group">
                <label for="href">文字位置</label>
                <div>
                <label class="radio-inline">
                    <input type="radio" name="position" value="banner-left" v-model="p.options.position"> 左对齐
                </label>
                <label class="radio-inline">
                    <input type="radio" name="position" value="banner-center" v-model="p.options.position"> 居中
                </label>
                <label class="radio-inline">
                    <input type="radio" name="position" value="banner-right" v-model="p.options.position"> 右对齐
                </label>
                </div>
            </div>
            <div class="form-group">
                <label for="href">图片透明度</label>
                <div id="opacity"></div>
            </div>


        </form>
    </div>
    <script>
        var editPanel = new Vue({
            el: '#edit-panel',
            data: {
                p: {
                    id: "",
                    src: "tpl/init/14x5/0.jpg",
                    fullSrc: this.src,
                    title: "",
                    description: "",
                    href: "",
                    focus: false,
                    focus2: false,
                    options: {
                        position: 'banner-center',
                        opacity: 0.15,
                        fontSize: "36px",
                        fontStyle: 'normal',
                        fontFamily: '微软雅黑',
                        color: '#ffffff',
                        fontSize2: "20px",
                        fontStyle2: 'normal',
                        fontFamily2: '微软雅黑',
                        color2: '#ffffff'
                    }
                },
                status: 0 //0新增 1 编辑
            }
        })

        $('#cover-div').PicDialog({
            aspectRatioW: 14,
            aspectRatioH: 5,
            webId: ${webId},
            success: function (doc) {
                editPanel.p.src = doc.filePath;
                editPanel.p.fullSrc = doc.remarks;
                if (!editPanel.p.title)
                    editPanel.p.title = doc.name
            }
        })

        //
        var sliderCfg = {
            step: 1,
            connect: [true, false],
            tooltips: [true],
            range: {
                'min': 0,
                'max': 100
            },
            format: wNumb({
                postfix: '%',
            })
        }

        var colorCfg = {
            showInput: true,
            // showPalette: true,
            showPaletteOnly: true,
            preferredFormat: "hex",
            showSelectionPalette: true,
            className: "full-spectrum",
            clickoutFiresChange: true,
            togglePaletteOnly: true,
            cancelText: '取消',
            chooseText: '确定',
            togglePaletteMoreText: '更多色彩',
            togglePaletteLessText: '关闭右侧',
            palette: [["#000000", "#434343", "#666666", "#999999", "#b7b7b7", "#cccccc", "#d9d9d9", "#efefef", "#f3f3f3", "#ffffff"],
                ["#980000", "#ff0000", "#ff9900", "#ffff00", "#00ff00", "#00ffff", "#4a86e8", "#0000ff", "#9900ff", "#ff00ff"],
                ["#e6b8af", "#f4cccc", "#fce5cd", "#fff2cc", "#d9ead3", "#dfead2", "#c9daf8", "#cfe2f3", "#d9d2e9", "#ead1dc"],
                ["#dd7e6b", "#ea9999", "#f9cb9c", "#ffe599", "#b6d7a8", "#a2c4c9", "#a4c2f4", "#9fc5e8", "#b4a7d6", "#d5a6bd"],
                ["#cc4125", "#e06666", "#f6b26b", "#ffd966", "#93c47d", "#76a5af", "#6d9eeb", "#6fa8dc", "#8e7cc3", "#c27ba0"],
                ["#a61c00", "#cc0000", "#e69138", "#f1c232", "#6aa84f", "#45818e", "#3c78d8", "#3d85c6", "#674ea7", "#a64d79"],
                ["#85200c", "#990000", "#b45f06", "#bf9000", "#38761d", "#134f5c", "#1155cc", "#0b5394", "#351c75", "#741b47"],
                ["#5b0f00", "#660000", "#783f04", "#7f6000", "#274e13", "#0c343d", "#1c4587", "#073763", "#20124d", "#4c1130"]]
        }


        $("#colorSelector").spectrum($.extend({}, {color: editPanel.p.options.color, move: function(color) {
            editPanel.p.options.color = color.toHexString(); // #ff0000
        }, change: function(color) {
            editPanel.p.options.color = color.toHexString(); // #ff0000
        }}, colorCfg))


        $("#colorSelector2").spectrum($.extend({}, {color: editPanel.p.options.color, move: function(color) {
            editPanel.p.options.color2 = color.toHexString(); // #ff0000
        }, change: function(color) {
            editPanel.p.options.color2 = color.toHexString(); // #ff0000
        }}, colorCfg))




        $('#edit-panel').on('click', function(e) {
            if ("title" === e.target.name) {
                editPanel.p.focus = true;
                editPanel.p.focus2 = false;
            } else if("description" === e.target.name) {
                editPanel.p.focus = false;
                editPanel.p.focus2 = true;
            } else {
                var $colorpicker = $(e.target).parents(".colorpicker")
                var $set = $(e.target).parents('.title-font-set')
                if ($colorpicker.length > 0 || $set.length > 0) return;

                editPanel.p.focus = false;
                editPanel.p.focus2 = false;
            }

        })


        var opacity = document.getElementById('opacity');
        noUiSlider.create(opacity, $.extend({}, {start: editPanel.p.options.opacity * 100}, sliderCfg));

        opacity.noUiSlider.on('update', function ( values, handle ) {
            editPanel.p.options.opacity = parseInt(values)/100
        });


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