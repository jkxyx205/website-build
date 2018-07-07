<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<link rel="stylesheet" href="${applicationScope.fileServer}/static/plugins/uploadify5/uploadifive.css">

<style>
    body .layui-layer-btn {
        border-top: 1px solid #e7e7eb;
    }

    body .layui-layer-btn .layui-layer-btn0,
    body .layui-layer-btn .layui-layer-btn2 {
        background-color: #5985ff;
        border-color: #5985ff;
        color: #fff;
        font-size: 12px;
        border-radius: 0;
    }

    .container-pic, .container-crop {
        height: 100%;
    }

    .menu-left {
        float: left;
        width: 120px;
        height: 100%;
        border-right: 1px solid #e7e7eb;
        overflow: auto;
    }

    .menu-left ul li {
        list-style: none;
        float: left;
        width: 100%;

    }

    .menu-left > ul > li.active>a {
        background-color: #f4f5f9;
    }

    .menu-left a {
        display: block;
        text-align: center;
    }

    .menu-left > ul>li > a {
        line-height: 40px;
        /*font-size: 14px;*/
    }

    .menu-left .second-level a {
        line-height: 30px;
    }

    .menu-left .second-level li.active>a {
        color: #5985ff;
    }

    .content-right {
        padding-left: 120px;
        height: 100%;
    }

    .content-right>div {
        height: 100%;
    }

    .upload-container {
        position: relative;
        height: 50px;
        /*line-height: 50px;*/
        padding: 0px 14px 0px 26px;
        border-bottom: 1px solid #e7e7eb;
    }



    .btn-upload-class {
        overflow: hidden;
        background-color: #5985ff;
        color: #fff;
        padding: 8px 16px;
        float: right;
        height: auto!important;
        line-height: 1!important;
        width: auto !important;
        margin: 11px 15px 0 0;

    }

    .btn-upload-class input {
        height: 100%;
        width: 100%;
        right: 0 !important;
        top: 0 !important;
    }




    .pic-list {
        padding: 10px 0 0 26px;
        height: calc(100% - 50px);
        overflow: auto;
    }

    .pic-box {
        float: left;
        width: 117px;
        margin: 0 10px 10px 0;
        border: 1px solid #e7e7eb;
        background-image: url(${applicationScope.fileServer}/static/images/img-bg.jpg);
    }

    .pic-box-img {
        position: relative;
        height: 117px;
        background-repeat: no-repeat;
        background-size: cover;
        cursor: pointer;
    }

    .pic-box-label {
        display: block;
        line-height: 30px;
        padding: 0 5px;
        background-color: #fff;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
    }


    .pic-box-selected {
        position: absolute;
        top: 0;
        left: 0;
        background: #000 url('${applicationScope.fileServer}/static/images/icon_card_selected.png') no-repeat 50% 50%;
        height: 100%;
        width: 100%;
        opacity: .6;
    }

    .pic-box-b {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height:100%;
        line-height: 30px;
        background-color: #000;
        opacity: 0;
        text-align: right;
    }

    .pic-box-b:hover {
        opacity: .6;
    }

    .pic-box-selected + .pic-box-b:hover {
        background-color: transparent;
        opacity: 1;
    }

    .uploadifive-queue {
        background: #F5F5F5;
        z-index: 2;
        position: absolute;
        top: 40px;
        width: 400px;
        right: 15px;
    }

    .multi-count {
        position: absolute;
        left: 38px;
        bottom: 20px;
    }

</style>


<div class="container-pic" id="container-pic">
    <div class="menu-left" id="menu-left">
        <ul>
            <li class="active" data-category="1" data-target="#panel-upload"><a href="javascript:;">站点视频</a></li>
            <li data-category="0" data-target="#panel-img">
                <a href="javascript:;">网络地址</a>
            </li>
        </ul>
    </div>
    <div class="content-right" id="content-right">
        <div class="panel-upload" id="panel-upload">



            <div class="upload-container">
                <form id="form-local" class="form-inline pull-left" style="margin-bottom: 0; padding-top: 11px;" method="get" action="/web/${webId}/content/api/picList">
                    <div class="form-group" style="position: relative">
                        <input type="text" class="form-control" name="name" id="query_name2" placeholder="搜索" style="width: 200px;">
                        <input type="hidden" name="page" value="1">
                        <input type="hidden" name="webId" value="${webId}">
                        <input type="hidden" name="categoryId" value="3">
                        <button type="submit" style="position: absolute;right:0;top: 3px;  border: none; background-color: transparent; outline: none">
                            <i class="iconfont icon-search"></i>
                        </button>
                    </div>
                </form>


                <input name="pic_upload" type="file" id="pic_upload">
            </div>
            <div class="pic-list" id="pic-list" data-page="1" data-scroll="0">
                <c:forEach var="p" items="${list}">
                    <div class="pic-box"  data-src="${p.id}" data-path="${p.urlPath}">
                        <div class="pic-box-img" style="background-image: url('${applicationScope.fileServer}/tpl/init/1x1/video.png');">
                            <div class="pic-box-s"></div>
                            <div class="pic-box-b">
                                    <a href="javascript:;" data-path="${p.urlPath}">
                                        <i class="iconfont icon-screen_expand" style="margin-right: 5px; color: #fff;"></i>
                                    </a>
                                <span class="pic-box-label" style="color: #fff; position: absolute; left: 0; bottom: 0; width: 100%; background-color: transparent; text-align: left; " title="${p.name}">${p.name}</span>
                            </div>
                        </div>
                        <span class="pic-box-label" style="display: none" title="${p.name}">${p.name}</span>
                    </div>
                </c:forEach>
            </div>
        </div>


        <div class="panel-img" id="panel-img">
            <form style="padding: 26px;">
                <textarea class="form-control" rows="10" placeholder="请输入视频地址" id="url-fill"></textarea>
            </form>
        </div>


    </div>
</div>

<script>

</script>
<%--<script src="${applicationScope.fileServer}/static/plugins/uploadify5/jquery.uploadifive.min.js"></script>--%>
<%--<script src="${applicationScope.fileServer}/static/plugins/Jcrop/js/jquery.Jcrop.min.js"></script>--%>
<%--<script src="${applicationScope.fileServer}/static/plugins/iscroll/iscroll.js"></script>--%>


