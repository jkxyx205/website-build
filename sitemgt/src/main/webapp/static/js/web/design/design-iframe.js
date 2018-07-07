;(function($, window, page) {
    'use strict'
    var design = {
        needSave: false,
        layoutOperatorHTML:'<div class="container-operator"><div class="insert-operator"><i class="iconfont icon-charu"></i><span>插入布局</span></div><div class="delete-operator"><i class="iconfont icon-empty"></i><span>删除布局</span></div></div>',//容器插入删除
        placeHolderHTML: '<div class="cpn-placeholder" title="插入控件"><span>+</span></div>',//添加HTML
        cpnOperatorHTML: '<div class="panel-mask"></div><div class="cpn-operator"><span class="label-set">设置</span><span class="label-drag">拖动</span><span class="label-edit">编辑</span><span class="label-remove">删除</span></div>', //操作bar按钮
        layoutAddOperatorHTML: '<div class="section-operator"><div class="append-operator"><i class="iconfont icon-charu"></i><span>添加布局</span></div></div>', //底部添加容器
        parent: window.parent,
        pushSave: function() {
            // $('body').getNiceScroll().resize()
            if (!this.needSave) {
                this.needSave = true
                $(window.parent).on("beforeunload", function(e) {
                    return true;
                });
            }
        },
        lazyLoad: function(obj) {
            obj.find(".lazy").lazyload({
                effect : "fadeIn"
            })
        },
        init: function () {
            console.log('...[iframe] preparing page => ' + page.id)
            _initEvent();  //初始化事件
            _initSort()  //初始化排序功能
            _initOperatorBtn() //初始化操作按钮

            if(window.parent.design.readStatus)
                this.setReadStatus(window.parent.design.readStatus)

            _changeMenuLink()

            window.parent.design.iframeReady()
        },
        setReadStatus: function (readStatus) {
            if(readStatus) {//只读模式
                $('body').addClass('readStatus')
                if($('#layout .cpn').length == 0)
                    $('#layout').append('<div id="cpn-empty-node">本页面正在建设中...</div>')

            } else {
                $('body').removeClass('readStatus')
                $('#cpn-empty-node').remove()
            }
        },
        resetLayout: function (layoutId, callback) {
            if($('div.cpn').length > 0) {
                layer.confirm('重置页面布局，原页面里的控件将被清除，您可以通过【历史控件-回收站】将它们找回。确认重置页面布局吗？', {
                    icon: 0,
                    area: '500px',
                    btn: ['确定','取消'] //按钮
                }, function(index){
                    layer.close(index);
                    execute();
                }, function(index){
                    layer.close(index);
                });
            } else {
                execute();
            }

            function execute() {
                var index = layer.load(2)
                $.post('/web/'+parent.site.id+'/design/layout/update/'+page.id + '/' + layoutId, function(data){
                    var $layout = $(data.data)
                    $('section').html('').append($layout)
                    _initOperatorBtn()
                    layer.close(index)
                    callback && callback()
                })
            }

        },
        updateBlock: function (blockId, callback) {
            $.post("/web/"+parent.site.id+"/design/block/update/"+blockId, function () {
                //link
                _updateLink(parent.site.headerId, parent.site.footerId, parent.site.styleId, blockId)
                callback && callback()
            })
        },
        updateHeader: function (headerId, callback) {
            $.post('/web/'+parent.site.id+'/design/header/update/'+headerId, function(data){
                var $html = $(data.data)
                $('#header-container').html($html)
                design.lazyLoad($html)
                _updateLink(headerId, parent.site.footerId, parent.site.styleId, parent.site.blockId)
                callback && callback()
            })
        },
        updateFooter: function (footerId, callback) {
            $.post('/web/'+parent.site.id+'/design/footer/update/'+footerId, function(data){
                $('#footer-container').html(data.data)
                _updateLink(parent.site.headerId, footerId, parent.site.styleId, parent.site.blockId)
                callback && callback()
            })
        },
        updateStyle: function (styleId, callback) {
            $.post('/web/'+parent.site.id+'/design/theme/update/'+ styleId, function(data){
                _updateLink(parent.site.headerId, parent.site.footerId, styleId, parent.site.blockId)
                callback && callback()
            })
        },
        save: function (callback) {
                if(!this.needSave){
                    callback && callback()
                    return;
                }

                var html = $('#layout').prop('outerHTML');
                var $layout = $(html);
                $layout.find('.cpn').empty();
                $layout.find('.cpn-placeholder, .cpn-operator, .container-operator, .section-operator').remove();

                $.ajax({
                    type: "POST",
                    url: '/web/'+parent.site.id+'/design/update/'+page.id+'',
                    data: {html: $layout.html()},
                    success: function () {
                        design.needSave = false
                        $(window.parent).unbind("beforeunload")
                        callback && callback()
                    },
                    dataType: 'json',
                    global: false
                });
        }
    }
    var layoutConfig = {
        id: "right-side-dialog",
        type: 1,
        title: false,
        closeBtn: 0,
        shadeClose: true,
        offset: 'rt',
        anim: 7,
        shade: .6,
        resize:false
    }

    /***
     * 绑定事件
     * @private
     */
    function _initEvent() {
        //设置控件
        $('#layout').delegate('.cpn-operator span[class="label-set"]','click', function(event) {

            var id = $(this).parents('.cpn').attr('id')
            var $component = $('#'+id)
            var label = $('#' + id + '>.panel').data('label')
            // var index = layer.load(2)

            $.get('/web/'+parent.site.id+'/design/settingDialog/' + id, function(str){
                var width = 160
                var dialogWidth = ((width + 17) * 4 + 15 + 30) + "px"
                // layer.close(index)
                layer.open($.extend({},{
                    area: [dialogWidth, '100%'],
                    btn: ['保存', '取消'],
                    btnAlign: 'l',
                    content: str,
                    yes: function(index, layero){
                                    var params = {}

                                    $.each(form.componentDto.optionsMap, function(i, val) {
                                        params[i] = val
                                    });

                                    params.id = form.componentDto.id
                                    params.label = form.componentDto.label

                                    params.componentId = form.activeId
                                    params.contentType = form.componentDto.contentType

                                    $.post('/web/'+parent.site.id+'/design/settings/add/'+page.id+'/' + id, params, function(data){

                                        $component.find('>:not(.cpn-operator):not(.panel-mask)').remove()

                                        var $html  =$(data.data)

                                        $component.append($html)

                                        design.lazyLoad($html)
                                        // $('body').getNiceScroll().resize()
                                        layer.close(index)

                                    })


                    },
                    success: function(layero, index){
                        $("#right-side-dialog").niceScroll();
                    }
                },layoutConfig));
            });

;
        })


        //删除控件
        $('#layout').delegate('.cpn-operator span[class="label-remove"]','click', function() {
            var $this = $(this)
            var id = $this.parents('.cpn').attr('id')
            var label = $('#' + id + '>.panel').data('label')

            layer.confirm('确定删除控件【'+label+'】吗？', {icon: 0, title:'提示'}, function(index){
                // layer.load(2)
                $this.parents('div[class~=cpn]:first').hide('slow', function () {
                    $(this).remove()
                })
                // layer.closeAll()
                layer.close(index)
                design.pushSave()
            });
        })

        //编辑控件内容
        $('#layout').delegate('.cpn-operator span[class="label-edit"]','click', function() {
            layer.load(2)
            var id = $(this).parents('.cpn').attr('id')
            console.log('...[iframe] edit component content pageName => ' + page.name + ',=> ' + id)
            design.save(function () {
                parent.location.href="/web/"+parent.site.id+"/content?pageName="+page.name+"&id=" + id
            })
        })

        //编辑页脚
        $('#footer-container').delegate('.cpn-operator span[class="label-edit"]','click', function() {
            layer.load(2)
            console.log('...[iframe] edit foot pageName => ' + page.name )
            design.save(function () {
                parent.location.href="/web/"+parent.site.id+"/content?pageName="+page.name+"&id=-1"
            })
        })



        //添加控件
        $('#layout').delegate('.cpn-placeholder','click', function() {
            var $this = $(this)
            // var index = layer.load(2)
            $.get('/web/'+parent.site.id+'/design/componentDialog', function(str){
                var width = 160
                var dialogWidth = ((width + 17) * 5 + 15) + "px"

                layer.open($.extend({},{
                    area: [dialogWidth, '100%'],
                    content: str,
                    // btnAlign: 'l',
                    success: function(layero, index){
                        // $("#right-side-dialog .component-img-container").niceScroll();

                        $('#right-side-dialog').delegate('.design-img-box','click', function() {
                            // index = layer.load(2)
                            var componentId = $(this).data('component-id')
                            var componentTitle = $(this).data('component-title')
                            var componentType = $(this).data('type')
                            console.log('...[iframe] add  component => ' + componentId)

                            $.get('/web/'+parent.site.id+'/design/add/'+ page.id + '/'+componentType+'/' + componentId, {"title": componentTitle}, function(str){
                                var $cpnHTML = $(str);
                                $this.before($cpnHTML)
                                if (component.contentType  === ';403;') { //布局控件
                                    $cpnHTML.find('.cpn-parent').append(design.placeHolderHTML)
                                } else {
                                    $cpnHTML.prepend(design.cpnOperatorHTML); //添加操作bar
                                    design.lazyLoad($cpnHTML)
                                    layer.close(index)
                                }
                                design.pushSave()

                            })
                        })
                    }
                },layoutConfig));
            });
        })


        //容器插入添加
        $('#layout').delegate('.insert-operator, .append-operator','click', function() {
            var width = 99
            var dialogWidth = ((width + 15) * 4 + 15) + "px"

            var $container = $(this).parents('.container, .container-fluid')
            $container =  $container.length > 0 ? $container: $(this).parent();

            layer.open($.extend({},{
                area: [dialogWidth, '100%'],
                content: '<ul class="design-img-container layout-img-container">'+
                '<li data-layout-id="101" class="design-img-box" style="background-position: 0 0;"><div class="design-img-box-mask"></div><button class="btn-use btn-primary btn-xs">使用</button></li>'+
                '<li data-layout-id="102" class="design-img-box" style="background-position: -99px 0;"><div class="design-img-box-mask"></div><button class="btn-use btn-primary btn-xs">使用</button></li>'+
                '<li data-layout-id="103" class="design-img-box" style="background-position: -198px 0;"><div class="design-img-box-mask"></div><button class="btn-use btn-primary btn-xs">使用</button></li>'+
                '<li data-layout-id="104" class="design-img-box" style="background-position: -297px 0;"><div class="design-img-box-mask"></div><button class="btn-use btn-primary btn-xs">使用</button></li>'+
                '<li data-layout-id="105" class="design-img-box" style="background-position: -396px 0;"><div class="design-img-box-mask"></div><button class="btn-use btn-primary btn-xs">使用</button></li>'+
                '<li data-layout-id="106" class="design-img-box" style="background-position: -495px 0;"><div class="design-img-box-mask"></div><button class="btn-use btn-primary btn-xs">使用</button></li>'+
                '<li data-layout-id="107" class="design-img-box" style="background-position: -594px 0;"><div class="design-img-box-mask"></div><button class="btn-use btn-primary btn-xs">使用</button></li>'+
                '<li data-layout-id="108" class="design-img-box" style="background-position: -693px 0;"><div class="design-img-box-mask"></div><button class="btn-use btn-primary btn-xs">使用</button></li>'+
                '<li data-layout-id="109" class="design-img-box" style="background-position: -792px 0;"><div class="design-img-box-mask"></div><button class="btn-use btn-primary btn-xs">使用</button></li>'+
                '</ul>',
                success: function(layero, index) {
                    $('#right-side-dialog .design-img-box').on('click', function () {
                        // layer.load(2)
                        var layoutId = $(this).data('layout-id')
                        console.log('...[iframe] insert/add  layout => ' + layoutId)

                        $.post('/web/'+parent.site.id+'/design/layout/insert/'+page.id + '/' + layoutId, function(data){
                            var $layout = $(data.data)
                            _initLayout($layout)
                            $container.before($layout)
                            _initSort()
                            // layer.closeAll()
                            layer.close(index)
                            design.pushSave()
                        })

                    })
                }
            },layoutConfig));
        })

        //删除容器
        $('#layout').delegate('.delete-operator','click', function(event) {
            var $container = $(this).parents('.container, .container-fluid')

            if($container.find('div.cpn').length > 0) {
                layer.confirm('删除容器，容器里的控件将被清除，您可以通过【历史控件-回收站】将它们找回。确认删除容器吗？', {
                    icon: 0,
                    area: '500px',
                    btn: ['确定','取消'] //按钮
                }, function(index){
                    layer.close(index);
                    execute();
                }, function(index){
                    layer.close(index);
                });
            } else {
                execute();
            }

            function execute() {
                $container.hide('slow', function() {
                    $(this).remove()
                })
                design.pushSave()
            }

        })
    }

    function _updateLink(headerId, footerId, styleId, blockId) {
        var style = styleId + '/sass/'+ blockId + ".css";

        $('#headerCSSLink').attr('href',fileServer + '/tpl-css/header/'+headerId+'/'+ style)
        $('#footerCSSLink').attr('href',fileServer + '/tpl-css/footer/'+footerId+'/' + style)
        $('#styleCSSLink').attr('href', fileServer + '/tpl-css/style/' + style)
    }


    function _initSort() {
        _layoutSort()
        _cpnSort()


    }

    function _layoutSort() {
        //布局位置排序
        $( "section" ).sortable({
            cancel: ".portlet-toggle",
            items: ".container, .container-fluid",
            placeholder: "portlet-placeholder",
            opacity: 0.6, //拖动时，透明度为0.6
            revert: true, //释放时，增加动画
            stop:function(event, ui) {
                design.pushSave()
            }
        }).disableSelection();
    }

    function _cpnSort() {
        //控件位置排序
        $(".cpn-parent").sortable({
            connectWith: ".cpn-parent",
            items: ".cpn, .cpn-placeholder",
            handle: ".panel-mask, .label-drag",
            cancel: ".portlet-toggle",
            placeholder: "portlet-placeholder",
            opacity: 0.6, //拖动时，透明度为0.6
            revert: true, //释放时，增加动画
            stop:function(event, ui) {
                var id = ui.item.attr('id')
                var obj = "swiper_" + id
                var obj2 = "masonry_" + id
                if (window[obj]) {//轮播控件
                    window[obj].emit('resize');
                } else if(window[obj2]) {//瀑布流
                    window[obj2].masonry()
                }

                design.pushSave()
            }
        }).disableSelection();
    }


    function _initOperatorBtn() {
        $('section>.container, section>.container-fluid').prepend(design.layoutOperatorHTML);
        $('#layout').append(design.layoutAddOperatorHTML)
        $('.cpn-parent').append(design.placeHolderHTML)
        $('.cpn, #footer-container').prepend(design.cpnOperatorHTML);
    }

    function _initLayout($layout) {
        $layout.prepend(design.layoutOperatorHTML);
        $layout.find('.cpn-parent').append(design.placeHolderHTML)
    }
    
    function _changeMenuLink() {
        $('.drawer-nav a[href^="/"]').each(function () {
            // this["data-href"] = '/web/'+webId+'/design/page' + this.href.substring(this.href.lastIndexOf('/'));
            this.link = '/web/'+parent.site.id+'/design/page' + this.href.substring(this.href.lastIndexOf('/'))
            // this.href = this.link
            // this.target='_self'

            this.href = "javascript:;"
            $(this).on('click', function () {
                layer.load(2)
                var _a = this;
                design.save(function () {
                    location.href = _a.link
                })
            })
        })

        $('.logo a[href^="/"], a.drawer-brand[href^="/"]').each(function () {
            this.link = '/web/'+parent.site.id+'/content?pageName=index'

            this.href = "javascript:;"
            $(this).on('click', function () {
                layer.load(2)
                var _a = this;
                design.save(function () {
                    parent.location.href = _a.link
                })
            })
        })
    }


    window.design = design



})(jQuery, window, page)


$(function () {
    layer.config({
        skin: 'site-class'
    })

    $( document ).ajaxStart(function(e) {
        this.ajaxIndex = layer.load(2)
    });

    $(document).ajaxComplete(function() {
        layer.close(this.ajaxIndex)
    });

    design.init()
})