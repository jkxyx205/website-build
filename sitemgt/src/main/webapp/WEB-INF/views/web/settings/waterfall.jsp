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
<body>
<div class="form form-setting" id="form">
    <div class="item-group">
        <div class="item-label">
            控件标题
        </div>
        <div class="item-control">
            <input type="text" v-model="componentDto.label">
            <label>显示标题栏<input type="checkbox" v-model="componentDto.optionsMap.labelDisplay" style="margin-left: 5px;"></label>
        </div>
    </div>
    <div class="item-group">
        <div class="item-label" style="vertical-align: top;">
            显示列数
        </div>
        <div class="item-control">
            <div class="column-selector-container">
                <ul>
                    <li></li>
                    <li></li>
                    <li></li>
                    <li></li>
                    <li></li>
                    <li></li>
                </ul>
            </div>
        </div>
    </div>
    <div class="item-group">
        <div class="item-label">
            外观设置
        </div>
        <div class="item-control">
            <label><input type="checkbox" v-model="componentDto.optionsMap.titleDisplay" style="margin-left: 5px;">显示标题概要</label>
            <label><input type="checkbox" v-model="componentDto.optionsMap.marginable" style="margin-left: 5px;">显示图片间空隙</label>
        </div>
    </div>

    <div class="item-group">
        <label><input type="checkbox" v-model="componentDto.optionsMap.lightBox">开启点击图片放大效果(Lightbox)</label>
    </div>
    <div class="item-group">
        <div class="item-tab">
            <ul>
                <li class="active"><a href="#a">控件样式</a></li>
                <li><a href="#b">动画设置</a></li>
            </ul>
            <div class="item-tab-content" id="a">
                <ul class="design-img-container component-img-container">
                    <li  v-for="item in components" :title="item.title" :class="{active: item.id == activeId}" @click="activeId = item.id">
                        <div :class="'design-img-box component-img-' + item.id" :data-component-id="item.id" :data-component-title="item.title">
                            <div class="design-img-box-mask"></div>
                            <%--<button class="btn-use btn-primary btn-xs">使用</button>--%>
                        </div>
                    </li>
                    <div style="clear: both;"></div>
                </ul>
            </div>
            <div class="item-tab-content" id="b" style="display: none;">
                    <!---->
                <div class="item-group">
                    <div class="item-label" style="font-weight: normal;">动画特效</div>
                    <div class="item-control">
                        <select v-model="componentDto.optionsMap.effect" style="width: 300px;">
                            <optgroup label="Attention Seekers">
                                <option value="bounce">bounce</option>
                                <option value="flash">flash</option>
                                <option value="pulse">pulse</option>
                                <option value="rubberBand">rubberBand</option>
                                <option value="shake">shake</option>
                                <option value="swing">swing</option>
                                <option value="tada">tada</option>
                                <option value="wobble">wobble</option>
                                <option value="jello">jello</option>
                            </optgroup>

                            <optgroup label="Bouncing Entrances">
                                <option value="bounceIn">bounceIn</option>
                                <option value="bounceInDown">bounceInDown</option>
                                <option value="bounceInLeft">bounceInLeft</option>
                                <option value="bounceInRight">bounceInRight</option>
                                <option value="bounceInUp">bounceInUp</option>
                            </optgroup>

                            <optgroup label="Bouncing Exits">
                                <option value="bounceOut">bounceOut</option>
                                <option value="bounceOutDown">bounceOutDown</option>
                                <option value="bounceOutLeft">bounceOutLeft</option>
                                <option value="bounceOutRight">bounceOutRight</option>
                                <option value="bounceOutUp">bounceOutUp</option>
                            </optgroup>

                            <optgroup label="Fading Entrances">
                                <option value="fadeIn">fadeIn</option>
                                <option value="fadeInDown">fadeInDown</option>
                                <option value="fadeInDownBig">fadeInDownBig</option>
                                <option value="fadeInLeft">fadeInLeft</option>
                                <option value="fadeInLeftBig">fadeInLeftBig</option>
                                <option value="fadeInRight">fadeInRight</option>
                                <option value="fadeInRightBig">fadeInRightBig</option>
                                <option value="fadeInUp">fadeInUp</option>
                                <option value="fadeInUpBig">fadeInUpBig</option>
                            </optgroup>

                            <optgroup label="Fading Exits">
                                <option value="fadeOut">fadeOut</option>
                                <option value="fadeOutDown">fadeOutDown</option>
                                <option value="fadeOutDownBig">fadeOutDownBig</option>
                                <option value="fadeOutLeft">fadeOutLeft</option>
                                <option value="fadeOutLeftBig">fadeOutLeftBig</option>
                                <option value="fadeOutRight">fadeOutRight</option>
                                <option value="fadeOutRightBig">fadeOutRightBig</option>
                                <option value="fadeOutUp">fadeOutUp</option>
                                <option value="fadeOutUpBig">fadeOutUpBig</option>
                            </optgroup>

                            <optgroup label="Flippers">
                                <option value="flip">flip</option>
                                <option value="flipInX">flipInX</option>
                                <option value="flipInY">flipInY</option>
                                <option value="flipOutX">flipOutX</option>
                                <option value="flipOutY">flipOutY</option>
                            </optgroup>

                            <optgroup label="Lightspeed">
                                <option value="lightSpeedIn">lightSpeedIn</option>
                                <option value="lightSpeedOut">lightSpeedOut</option>
                            </optgroup>

                            <optgroup label="Rotating Entrances">
                                <option value="rotateIn">rotateIn</option>
                                <option value="rotateInDownLeft">rotateInDownLeft</option>
                                <option value="rotateInDownRight">rotateInDownRight</option>
                                <option value="rotateInUpLeft">rotateInUpLeft</option>
                                <option value="rotateInUpRight">rotateInUpRight</option>
                            </optgroup>

                            <optgroup label="Rotating Exits">
                                <option value="rotateOut">rotateOut</option>
                                <option value="rotateOutDownLeft">rotateOutDownLeft</option>
                                <option value="rotateOutDownRight">rotateOutDownRight</option>
                                <option value="rotateOutUpLeft">rotateOutUpLeft</option>
                                <option value="rotateOutUpRight">rotateOutUpRight</option>
                            </optgroup>

                            <optgroup label="Sliding Entrances">
                                <option value="slideInUp">slideInUp</option>
                                <option value="slideInDown">slideInDown</option>
                                <option value="slideInLeft">slideInLeft</option>
                                <option value="slideInRight">slideInRight</option>

                            </optgroup>
                            <optgroup label="Sliding Exits">
                                <option value="slideOutUp">slideOutUp</option>
                                <option value="slideOutDown">slideOutDown</option>
                                <option value="slideOutLeft">slideOutLeft</option>
                                <option value="slideOutRight">slideOutRight</option>

                            </optgroup>

                            <optgroup label="Zoom Entrances">
                                <option value="zoomIn">zoomIn</option>
                                <option value="zoomInDown">zoomInDown</option>
                                <option value="zoomInLeft">zoomInLeft</option>
                                <option value="zoomInRight">zoomInRight</option>
                                <option value="zoomInUp">zoomInUp</option>
                            </optgroup>

                            <optgroup label="Zoom Exits">
                                <option value="zoomOut">zoomOut</option>
                                <option value="zoomOutDown">zoomOutDown</option>
                                <option value="zoomOutLeft">zoomOutLeft</option>
                                <option value="zoomOutRight">zoomOutRight</option>
                                <option value="zoomOutUp">zoomOutUp</option>
                            </optgroup>

                            <optgroup label="Specials">
                                <option value="hinge">hinge</option>
                                <option value="jackInTheBox">jackInTheBox</option>
                                <option value="rollIn">rollIn</option>
                                <option value="rollOut">rollOut</option>
                            </optgroup>
                        </select>
                    </div>
                </div>

                <div class="item-group">
                    <div class="item-label" style="font-weight: normal;">进入时间</div>
                    <div class="item-control" style="width: 300px;">
                        <div id="delay"></div>
                    </div>
                </div>
                <div class="item-group">
                    <div class="item-label" style="font-weight: normal;">持续时间</div>
                    <div class="item-control" style="width: 300px;">
                        <div id="duration"></div>
                    </div>
                </div>
                <!---->
            </div>
        </div>
    </div>
</div>



<%--<script src="${applicationScope.fileServer}/static/plugins/column-selector/jquery-column-selector.js"></script>--%>
<%--<script src="${applicationScope.fileServer}/static/plugins/noUiSlider/nouislider.min.js"></script>--%>
<%--<script src="${applicationScope.fileServer}/static/plugins/wNumb.js"></script>--%>
<script>

    var form = new Vue({
        el: '#form',
        data: {
            componentDto: ${componentDto},
            components: ${components},
            activeId:${componentDto}.componentId
        }
    })

$('.column-selector-container').selector({
    rowNum: form.componentDto.optionsMap.line,
    colNum: form.componentDto.optionsMap.col,
    selected: function(row, col) {
        form.componentDto.optionsMap.line = row
        form.componentDto.optionsMap.col = col
    }
})

//tab
$('.item-tab>ul li').on('click', function() {
    $(this).siblings().removeClass('active').find('a').css({'cursor': 'pointer'});
    $(this).addClass('active').find('a').css({'cursor': 'default'})

    $('.item-tab-content').hide()

    $($(this).addClass('active').find('a').attr('href')).show()
})

//
var sliderCfg = {
    step: 0.1,
    connect: [true, false],
    tooltips: [true],
    range: {
        'min': 0,
        'max': 10
    },
    format: wNumb({
        thousand: '.0',
        postfix: 's',
    })
}


var duration = document.getElementById('duration');

noUiSlider.create(duration, $.extend({}, {start: form.componentDto.optionsMap.listDuration}, sliderCfg));

duration.noUiSlider.on('update', function ( values, handle ) {
    form.componentDto.optionsMap.listDuration = parseFloat(values);
});

//
var delay = document.getElementById('delay');
noUiSlider.create(delay, $.extend({}, {start: form.componentDto.optionsMap.listDelay}, sliderCfg));

delay.noUiSlider.on('update', function ( values, handle ) {
    form.componentDto.optionsMap.listDelay = parseFloat(values);
});
</script>
