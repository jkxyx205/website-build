<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
    .container-page>ul ul {
        display: none;
    }

    .container-page div.radio:hover {
        background-color: #f4f7ff;
    }

    .container-page div.page.active {
        background-color: #5985ff;
        color: #fff!important;
    }

    .container-page div.page.active i {
        color: #fff!important;
    }

    body .site-class .layui-layer-btn .layui-layer-btn0.disabled,
    body .layui-layer-btn .layui-layer-btn0.disabled {
        background-color: #ccc;
        border:1px solid #ccc;
    }

    .pagenation label {
        display: inline-block;
        cursor: pointer;
    }

    .container-article .heighlight {
        color: #5985ff;
    }
</style>
<div class="container-article">
    <form class="form-inline" action="/web/${webId}/content/article/list" method="get" style="margin-bottom: 1em;">
        <input type="hidden" value="1" name="article-page" id="article-page">
        <div class="form-group" style="position: relative;">
            <input type="text" class="form-control title" name="article-title" id="article-title" placeholder="标题" style="width: 200px;">
            <button type="submit" style="position: absolute;right:0;top: 3px;  border: none; background-color: transparent; outline: none">
                <i class="iconfont icon-search"></i>
            </button>
        </div>

        <%--<button type="submit" class="btn btn-primary" style="margin-left: 5px;">搜索</button>--%>
    </form>
    <div style="height: 360px; overflow: auto;" id="container-article-list">
        <table class="table table-hover">
            <tr>
                <th>标题</th>
                <th>发布日期</th>
            </tr>
            <c:forEach var="a" items="${jqGrid.rows}">
                <tr>
                    <td>
                        <label style="cursor: pointer;">
                        <input type="radio" name="articleRadio" value="/article/${a.id}" data-title="${a.title}">&nbsp;&nbsp;${a.title}
                        </label>
                    </td>
                    <td>${a.publishDate}</td>
                </tr>
            </c:forEach>
        </table>
    </div>
    <div id="pagenation" class="pagenation pull-right" style="margin-top: 20px;">
        <!-- <label><i class="iconfont icon-more"></i></label> -->
        <span>${jqGrid.page}/${jqGrid.total}</span>
        <c:if test="${jqGrid.page < jqGrid.total}">
            <label data-page="2"><i class="iconfont icon-more"></i></label>
        </c:if>
    </div>
</div>
<script src="${applicationScope.fileServer}/static/js/template.js"></script>
<script>

    var $aform = $('.container-article form')

    $aform.ajaxForm(function(data) {
                //heighlight
       var title = $('#article-title').val()
       
        data.rows.forEach(function(a) {
            if(title)
                a.formatTitle = a.title.replace(title, '<span class="heighlight">'+title+'</span>')
            else
                a.formatTitle = a.title
        })


        template.defaults.escape = false //必须在此处设置配置，这是个坑。为什么不能在文档加载的时候，设置？？ 否则会报Uncaught ReferenceError: template is not defined

        var html = template('article-list', data);
        var html2 = template('pagenation-bar', data);

        $('#container-article-list').html(html)
        $('#pagenation').html(html2)

        $('.layui-layer-btn0').addClass('disabled')
        $('#article-page').val(1)

    })

    $('#pagenation').delegate('label', 'click', function() {
        var page = $(this).data('page')

        $('#article-page').val(page)

        $aform.submit()
    })

</script>

<script id="article-list" type="text/html">
     <table class="table table-hover">
            <tr>
                <th>标题</th>
                <th>发布日期</th>
            </tr>
           {{each rows}}
            <tr>
                <td>
                    <label style="cursor: pointer;">
                        <input type="radio" name="articleRadio" value="/article/{{$value.id}}" data-title="{{$value.title}}">
                        &nbsp;&nbsp; {{$value.formatTitle}}
                    </label>
                </td>
                <td>{{$value.publishDate}}</td>
            </tr>
             {{/each}}
             {{if records == 0}}
            <tr>
                <td colspan="2" style="text-align: center;">暂无数据</td> 
            </tr>
            {{/if}}
        </table>
</script>
<script id="pagenation-bar" type="text/html">
    {{if page > 1}}
        <label data-page="{{page-1}}"><i class="iconfont icon-prev"></i></label>
    {{/if}}
        <span>{{page}}/{{total}}</span>
    {{if page < total}}
        <label data-page="{{page+1}}"><i class="iconfont icon-more"></i></label>
    {{/if}}
</script>
