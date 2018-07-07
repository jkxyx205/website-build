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

<head>
    <link rel="stylesheet" href="${applicationScope.fileServer}/static/css/content/article-list.css">
</head>
<body>
<div class="content-header">
    <h5>${label}</h5>
</div>
<div class="content-body">
    <form class="form-inline" action="${pageContext.request.contextPath}/web/${webId}/content/article/list/${categoryId}">
        <input type="hidden" value="1" name="page">
        <input type="hidden" value="${label}" name="label">
        <div class="form-group">
            <label for="title">标题</label>
            <input type="text" class="form-control" id="title" name="title" value="${article.title}">
        </div>
        <div class="form-group">
            <label>状态</label>
            <select class="form-control" name="status">
            <option value="">全部</option>
            <option value="0" <c:if test="${article.status eq '0'}">selected</c:if>>草稿箱</option>
            <option value="1" <c:if test="${article.status eq '1'}">selected</c:if>>已发布</option>
            </select>
        </div>
        <div class="form-group pull-right">
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/web/${webId}/content/article/add/${categoryId}?aspectRatioW=${aspectRatioW}&aspectRatioH=${aspectRatioH}&label=${label}">新增</a>
        </div>
        <div class="form-group">
            <button type="submit" class="btn btn-default">查询</button>
        </div>

    </form>

    <table class="table table-hover">
        <tr>
            <%--<th>序号</th>--%>
            <th>标题</th>
            <%--<th>作者</th>--%>
            <%--<th>是否置顶</th>--%>
            <th>状态</th>
            <th>发布日期</th>
            <th class="text-right">操作</th>
        </tr>
        <c:forEach var="a" items="${jqGrid.rows}" varStatus="loop">
            <tr>
                <%--<td class="text-center">${jqGrid.pageRows * (jqGrid.page-1) + loop.index + 1}</td>--%>
                <td ><a href="${pageContext.request.contextPath}/web/${webId}/content/article/edit/${categoryId}/${a.id}?aspectRatioW=${aspectRatioW}&aspectRatioH=${aspectRatioH}&label=${label}">${a.title}</a>
                    <c:if test="${a.stickTop eq 1}">
                        <span class="label label-success">置顶</span>
                    </c:if>
                </td>
                <%--<td class="text-center">${a.author}</td>--%>
                <%--<td class="text-center"><c:if test="${a.stickTop eq 0}">否</c:if><c:if test="${a.stickTop eq 1}">是</c:if></td>--%>
                <td><c:if test="${a.status eq 0}">草稿</c:if><c:if test="${a.status eq 1}">已发布</c:if></td>
                <td> <fmt:formatDate pattern = "yyyy-MM-dd" value = "${a.publishDate}"/></td>
                <td class="text-right">
                    <%--<a href="${pageContext.request.contextPath}/portal/${webId}/article/${a.id}" target="_blank">预览</a>--%>

                        <c:if test="${a.stickTop eq 1}">
                            <a class="stick_top" href="javascript:;" onclick="stickTop(${a.id}, 1)"><i class="iconfont icon-top" title="置顶到顶部"></i></a>
                            <a href="javascript:;" onclick="stickTop(${a.id}, 0)">取消置顶</a>
                        </c:if>

                        <c:if test="${a.stickTop eq 0}">
                            <a href="javascript:;" onclick="stickTop(${a.id}, 1)">置顶</a>
                        </c:if>
                        <span class="text-explode">|</span>
                    <a href="${pageContext.request.contextPath}/web/${webId}/content/article/edit/${categoryId}/${a.id}?aspectRatioW=${aspectRatioW}&aspectRatioH=${aspectRatioH}&label=${label}">编辑</a>
                        <span class="text-explode">|</span>
                    <a href="javascript:;" onclick="del(${a.id},'${a.title}', this);">删除</a></td>
            </tr>
        </c:forEach>
    </table>
    <nav class="pull-right pagination-box">
        <div class="pagination-info tip-info">共有${jqGrid.total}条，每页显示：20条</div>
        <ul class="pagination pagination-sm">
            <c:if test="${jqGrid.page <= 1}">
                <li class="disabled"><a>&laquo;</a></li>
            </c:if>
            <c:if test="${jqGrid.page > 1}">
                <li>
                    <a href="${pageContext.request.contextPath}/web/${webId}/content/article/list/${categoryId}?page=${jqGrid.page-1}&label=${label}&title=${article.title}&status=${article.status}" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a>
                </li>
            </c:if>


            <c:forEach var="i" begin="1" end="${jqGrid.total}">
            <li <c:if test="${jqGrid.page == i}">class="active"</c:if> ><a href="${pageContext.request.contextPath}/web/${webId}/content/article/list/${categoryId}?page=${i}&label=${label}&title=${article.title}&status=${article.status}">${i}</a></li>
            </c:forEach>

            <c:if test="${jqGrid.page >= jqGrid.total}">
                <li class="disabled"><a>&raquo;</a></li>
            </c:if>
            <c:if test="${jqGrid.page < jqGrid.total}">
            <li>
                <a href="${pageContext.request.contextPath}/web/${webId}/content/article/list/${categoryId}?page=${jqGrid.page+1}&label=${label}&title=${article.title}&status=${article.status}" aria-label="Previous"><span aria-hidden="true">&raquo;</span></a>
            </li>
            </c:if>


        </ul>
    </nav>
</div>
<myfooter>
    <script>

         function del(id,title,node) {
             var url = 　"${pageContext.request.contextPath}/web/${webId}/content/article/delete/" + id;

             layer.confirm('确定删除图文【'+title+'】？', {
                 btn: ['确定','取消'] //按钮
             }, function(){
                 $.get(url,function() {
                     //
                     $(node).parents('tr').hide('slow');

                     layer.msg('删除成功', {icon: 1});
                 })

             }, function(){

             });
         }

         function stickTop(id, value) {
//             console.log(categoryId + ' : ' + value)
             var url = 　"${pageContext.request.contextPath}/web/${webId}/content/article/stickTop/" + id + "/" + value;

             $.post(url, function (data) {
                 location.reload()
             })

         }


    </script>
</myfooter>
</body>
