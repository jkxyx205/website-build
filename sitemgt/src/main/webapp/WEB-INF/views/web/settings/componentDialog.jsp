<%--
  Created by IntelliJ IDEA.
  User: rick
  Date: 2017/8/15
  Time: 11:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="form" id="component">
    <div class="item-group top-bar">
        <div class="item-control">
            <div class="btn-group">
                <button type="button" :class="'btn-use ' + (type == 0 ? 'btn-primary':'btn-default')" @click="type = 0">新建控件</button>
                <button type="button" :class="'btn-use ' + (type == 1 ? 'btn-primary':'btn-default')" @click="type = 1">引用控件</button>
            </div>
        </div>
    </div>
    <div class="item-group" v-show="type == 0" style="margin-left: 15px; margin-bottom: 0">
        <div class="item-control">
            <ul class="inline-list">
                <li  v-for="s in styles" >
                    <label><input type="radio" v-model="contentType" :value="s.componentType.contentType" name="contentType">{{s.componentType.description}}</label>
                </li>
            </ul>
        </div>
    </div>
    <div class="item-group" style="height: calc(100% - 80px); margin-bottom: 0;" v-show="type == 0">
        <div class="image-list"  v-for="s in styles" v-show="contentType == s.componentType.contentType" style="height: 100%;">
            <ul class="design-img-container component-img-container">
                <li  v-for="item in s.componentList" :title="item.title">
                    <div :class="'design-img-box component-img-' + item.id" :data-component-id="item.id" :data-component-title="item.title" data-type="0">
                        <div class="design-img-box-mask"></div>
                        <button class="btn-use btn-primary btn-xs">使用</button>
                    </div>
                    <div v-text="item.title" class="image-box-label"></div>
                </li>
                <div style="clear: both;"></div>
            </ul>

        </div>
    </div>

    <div class="item-group" v-show="type == 1" style="margin-left: 15px; margin-bottom: 0;">
        <div class="item-control" style="padding: 0; position: relative;">
            <input id="page" type="text" readonly/>
            <i class="iconfont icon-arrowdropdown" style="position: absolute; right: 5px; top: 4px; cursor: pointer;" onclick="$('#page').click();"></i>
        </div>
    </div>
    <div class="item-group" style="height: calc(100% - 88px); margin-bottom: 0;" v-show="type == 1">
        <ul class="design-img-container component-img-container">
            <li  v-for="item in list" :title="item.title">
                <div :class="'design-img-box component-img-' + item.componentId" :data-component-id="item.id" :data-component-title="item.title" data-type="1">
                    <div class="design-img-box-mask"></div>
                    <button class="btn-use btn-primary btn-xs">使用</button>
                </div>
                <div v-text="item.label" class="image-box-label"></div>
            </li>
            <div style="clear: both;"></div>
        </ul>
    </div>

</div>

<script>
    var setting = {
        view: {
            showIcon: false
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        callback: {
            onClick: function() {
                var index = layer.load(2)

                var ids =  $("#page").comboboxTree("getSelectedIds");
                $.get('/web/${webId}/design/component/' + ids ,function(data) {
                    component.list = data.data
                    layer.close(index)
                })
            }
        }
    };

    var component = new Vue({
        el: '#component',
        data:  {
            contentType: '',
            list:${list},
            styles: ${styles},
            type: 0,
        },
        methods: {
        },
        mounted:function () {
            this.contentType = this.styles[0].componentType.contentType
        }
    })

    $(function () {
        $("#page").comboboxTree({
//            width:400,
            height:300,
            ztree: {
                setting:setting,
                values: '${selected}',
                zNodes:${zNodes},
                sync:true
            }
        });
    })
</script>