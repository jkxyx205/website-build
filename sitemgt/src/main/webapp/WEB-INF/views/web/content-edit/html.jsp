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
    <link rel="stylesheet" href="${applicationScope.fileServer}/static/css/content/content-base.css">
    <style>
        body, .content-body {
            padding: 0;
        }
    </style>
</head>
<body>
<div class="content-body">
<form action="${pageContext.request.contextPath}/web/${webId}/content/article/save" class="article-edit" id="form" method="post">
    <input type="hidden" name="id" id="id" value="${article.id}">
    <input type="hidden" name="categoryId" value="${categoryId}">
    <input type="hidden" name="title" value="${article.title}">
    <input type="hidden" name="status"  value="1">
    <input type="hidden" name="webId" value="${webId}">
    <textarea name="html" id="html" class="hidden"></textarea>

    <div id="editor">
        ${article.html}
    </div>

</form>
    <div class="toolbar navbar-fixed-bottom">
        <button class="btn btn-primary" onclick="publish()">保存</button>&nbsp;
    </div>

</div>
</body>
<myfooter>
    <script src="${applicationScope.fileServer}/static/plugins/jquery.nicescroll/jquery.nicescroll.min.js"></script>
    <script src="${applicationScope.fileServer}/static/plugins/jquery-ui/jquery-ui.js"></script>
    <script src="${applicationScope.fileServer}/static/plugins/jQuery-File-Upload/js/jquery.fileupload.js"></script>
    <script src="${applicationScope.fileServer}/static/plugins/ckeditor/ckeditor.js"></script>

    <script>

        var style = ${site.styleId} + '/sass/'+ ${site.blockId} + ".css";

        var css =  ["${applicationScope.fileServer}/static/css/reset.css",
                "${applicationScope.fileServer}/static/css/bootstrap.min.css",
                "${applicationScope.fileServer}/tpl-css/style/"+style,
                "${applicationScope.fileServer}/tpl-css/header/${site.headerId}/"+ style,
                "${applicationScope.fileServer}/tpl-css/footer/${site.headerId}/"+ style]
    </script>

    <script src="${applicationScope.fileServer}/static/js/web/html.js"></script>

    <script>
        var status = 0;

        editorInit();

        var $form = $('#form');

        /***
         * 发布
         */
        function publish() {
            if ($form.valid()) {
                status = 1;
                $('#status').val("1");
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
            $('#btn-group button').attr('disabled', true);

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
                var url = '${pageContext.request.contextPath}/portal/${webId}/article/' + $('#id').val();
                window.open(url);
            } else if(status == 0) { //保存成功
                //alert('保存成功')
            } else if(status == 1) { //发布成功
                layer.msg('保存成功', {icon: 1});
            }

        });

    </script>
</myfooter>
