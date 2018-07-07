<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<head>
    <link rel="stylesheet" href="${applicationScope.fileServer}/static/plugins/salsa-calendar/build/SalsaCalendar.min.css">
    <link rel="stylesheet" href="${applicationScope.fileServer}/static/css/content/article-list.css">

</head>
<body>
<div class="content-header">
    <h5>${label}</h5>
</div>

<div class="content-body article-edit">
    <div class="container-fluid">
    <form class="row"  autocomplete ="off" action="${pageContext.request.contextPath}/web/${webId}/content/article/save" id="form" method="post">
        <input type="hidden" name="id" id="id" value="${article.id}">
        <input type="hidden" name="categoryId" value="${categoryId}">
        <input type="hidden" name="status" id="status" value="${article.status}">
        <input type="hidden" name="cover" value="${article.cover}" id="src">
        <input type="hidden" name="webId" value="${webId}">

        <textarea name="html" id="html" class="hidden"></textarea>

        <div class="form-horizontal">
            <div class="form-group">
                <div class="col-sm-12">
                    <input type="text" class="form-control title" name="title" placeholder="请在这里输入标题" autocomplete ="off" value="${article.title}" >
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-12">
                    <input type="text" class="form-control author" name="author" placeholder="请输入作者" value="${article.author}">
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-12">
                   <div class="media-group">
                       <div class="media-item" id="cpn-pic"><i class="iconfont icon-image"></i><span>图片</span></div>
                       <div class="media-item" id="cpn-video"><i class="iconfont icon-video"></i><span>视频</span></div>
                   </div>
                </div>
            </div>
            <div class="form-group" id="editor">
                ${article.html}
            </div>

        </div>
            <div class="form-inline" style="margin-top: 10px;">
                <div class="form-group">
                    <label for="publishDate" class="control-label">发布日期</label>
                    <input type="text" class="form-control" name="publishDate" id="publishDate" readonly value="<fmt:formatDate pattern = "yyyy-MM-dd" value = "${article.publishDate}"/>">

                </div>
                <%--<div class="checkbox">--%>
                    <%--<label>--%>
                         <%--<input type="checkbox" name="stickTop" value="1" <c:if test="${article.stickTop eq '1'}">checked</c:if>> 置顶--%>
                    <%--</label>--%>
                <%--</div>--%>
            </div>
            <div class="form-inline">
                <div class="form-group">
                    <label  class="control-label" style="vertical-align: top">封面&nbsp;</label>
                    <div class="cover-container" id="cover-container">
                        <img class="lazy" src="${applicationScope.fileServer}/tpl/init/9x5/0.jpg" alt="" id="cover" data-original="${applicationScope.fileServer}/${article.cover}">
                    </div>
                </div>
            </div>
            <div>
                <div class="form-group">
                    <label for="description">摘要&nbsp;<span class="tip-info">选填，如果不填写默认抓取正文前80个字</span></label>
                    <textarea id="description" class="form-control" rows="3" name="description">${article.description}</textarea>
                </div>
            </div>
        </form>

    </div>



</div>


<div class="toolbar navbar-fixed-bottom">
    <button class="btn btn-default" onclick="view()">预览</button>
    <button class="btn btn-info" onclick="save()">保存草稿</button>
    <button class="btn btn-primary" onclick="publish()">保存待发布</button>
</div>




</body>
<myfooter>
    <script src="${applicationScope.fileServer}/static/plugins/ckeditor/ckeditor.js"></script>


    <script src="${applicationScope.fileServer}/static/plugins/jquery.nicescroll/jquery.nicescroll.min.js"></script>
    <script src="${applicationScope.fileServer}/static/plugins/jquery-ui/jquery-ui.js"></script>
    <script src="${applicationScope.fileServer}/static/plugins/jQuery-File-Upload/js/jquery.fileupload.js"></script>

    <script src="${applicationScope.fileServer}/static/plugins/salsa-calendar/build/SalsaCalendar.min.js"></script>

    <script src="${applicationScope.fileServer}/static/js/web/article.js"></script>
    <script src="${applicationScope.fileServer}/static/js/web/jquery.pic.js"></script>
    <script src="${applicationScope.fileServer}/static/js/web/jquery.video.js"></script>

    <script>
        var status = 0;


        editorInit();
        new SalsaCalendar({
            inputId: 'publishDate',
            lang: 'zh',
            calendarPosition: 'bottom',
            fixed: false,
            connectCalendar: true,
            allowEmptyDate: true
        });

       

        var $form = $('#form');

        /***
         * 发布
         */
        function publish() {
            if ($form.valid()) {
                status = 1;
                _save();
            }
        }
        /***
         * 保存
         */
        function save() {
            status = 0;
            _save();
        }

        function _save () {
            $('#status').val(status);

            //set data
            var html = CKEDITOR.instances['editor'].getData();
            $('#html').text(html);

            $form.submit();
        }

        /***
         * 预览
         */

        function view() {
            status = 2;
            _save();
        }




        $('#form').ajaxForm(function(data) {
            //设置id
            var obj = data.data;

            $('#id').val(obj.id);

            $('#btn-group button').attr('disabled', false);

            if (status == 2) {//保存并预览
                var url = '${pageContext.request.contextPath}/web/${webId}/view/article/' + $('#id').val();
                newWin(url)
            } else if(status == 0) { //保存成功
                layer.msg('保存成功', {icon: 1});
            } else if(status == 1) { //发布成功
                layer.msg('保存成功', {icon: 1});
            }

        });

        $('#cover-container').PicDialog({
            aspectRatioW: ${aspectRatioW},
            aspectRatioH: ${aspectRatioH},
            webId: ${webId},
            success: function (doc) {
                $('#src').val(doc.filePath)
                $('#cover').attr('src',doc.urlPath);
            }
        })


//        $('#cpn-pic').on('click', function () {
//            CKEDITOR.instances['editor'].insertHtml('xx is a good boy')
//        })

        $('#cpn-pic').PicDialog({
            aspectRatioW: 0,
            aspectRatioH: 0,
            webId: ${webId},
            success: function (doc) {
                CKEDITOR.instances['editor'].insertHtml('<img src="'+doc.urlPath+'">')
            }
        })

        $('#cpn-video').VideoDialog({
            webId: ${webId},
            autoplay: false,
            success: function (html) {
                CKEDITOR.instances['editor'].insertHtml('<div class="ckeditor-html5-video" style="text-align:center">' + html + '</div>')
            }
        })

    </script>
</myfooter>