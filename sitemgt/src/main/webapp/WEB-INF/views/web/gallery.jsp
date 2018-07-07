<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<head>
    <link rel="stylesheet" href="${applicationScope.fileServer}/static/css/web/site-baseinfo.css">
</head>
<body>
<div class="children-container">
    <div class="container-fluid">
        <button class="btn btn-primary" onclick="syncGallery()">批量同步</button>

        <form class="form-horizontal baseinfo" id="gallery-upload-form" method="post" action="${pageContext.request.contextPath}/web/${webId}/gallery/save" enctype="multipart/form-data">
            <div class="form-group">
                <label class="col-sm-2 control-label">图片分类</label>
                <div class="col-sm-8">
                    <select class="form-control" name="pid" id="pidSelect">
                        <option value="1">图标</option>
                        <option value="2">自然风景</option>
                        <option value="3">动物植物</option>
                        <option value="4">生活家居</option>
                        <option value="5">旅游度假</option>
                        <option value="6">地标建筑</option>
                        <option value="7">主题场景</option>
                        <option value="8">办公环境</option>
                        <option value="9">高科技研发</option>
                        <option value="10">互联网通信</option>
                        <option value="11">服装饰品</option>
                        <option value="12">商业金融</option>
                        <option value="13">农林牧渔</option>
                        <option value="14">运动竞技</option>
                        <option value="15">高雅艺术</option>
                        <option value="16">娱乐休闲</option>
                        <option value="17">教育教学</option>
                        <option value="18">医疗健康</option>
                        <option value="19">美容时尚</option>
                        <option value="20">交通运输</option>
                        <option value="21">餐饮美食</option>
                        <option value="22">机械加工</option>
                        <option value="23">静物装置</option>
                        <option value="24">旅游度假</option>
                        <option value="25">背景素材</option>
                    </select>
                </div>
            </div>
             <div class="form-group">
                <label class="col-sm-2 control-label">图片<span class="span-require">*</span></label>
                <div class="col-sm-8">
                        <input type="file" class="form-control required" name="img" required>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">图片关键字<span class="span-require">*</span></label>
                <div class="col-sm-8">
                    <input type="text" class="form-control required" name="name" id="name" placeholder="关键字用空格隔开 如：点研 科技 云计算" required>
                </div>
            </div>
           
            
            <div class="form-group btn-container">
                <div class="col-sm-offset-2 col-sm-8">
                    <button type="submit" class="btn btn-primary" id="btn-save">保存</button>
                    <button id="favicon-container" class="btn btn-link" type="button">查看图库</button>
                </div>
            </div>

        </form>

    </div>
</div>
</body>
<myfooter>
    <script src="${applicationScope.fileServer}/static/js/web/jquery.pic.js"></script>
    <script>
        $(function() {
            $('#favicon-container').PicDialog({
                aspectRatioW: 0,
                aspectRatioH: 0,
                webId: ${webId},
                success: function (doc) {

                }
            })

            $("#pidSelect").val('${pid}');
            if('${add}') {
                layer.msg('添加成功！', {icon: 1})
            }



//            $('#gallery-upload-form').ajaxForm(function(data) {
//                location.href = '/web/2/gallery?add=1&pid='+ $("#pidSelect").val()
//            })

            $("#gallery-upload-form").validate()


            $('#gallery-upload-form').ajaxForm({
                beforeSubmit: function() {
                    console.log('before submit...')
                    return true
                },
                success: function(data) {
                    location.href = '/web/${webId}/gallery?add=1&pid='+ $("#pidSelect").val()
                }
            })
        })

        function syncGallery() {
            $.get('/web/${webId}/gallery/sync', function (data) {
                layer.msg('successful...')
            })
        }


    </script>
</myfooter>
