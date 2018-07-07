;(function($) {
    'use strict'

    var LinkDialog = function(element, options) {
        this.$element = $(element)
        this.options = options
        this.initStatus = false;
        this.init()
        this.pageHTML = []
        this.articleHTML = []
        this.lk = {
            typeIndex: 0,
            typeName: '页面',
            href:'',
            title: ''
        }
    };

     LinkDialog.prototype = {
        constructor: LinkDialog,
        init: function() {
            _event(this.$element, this)
        }
    }

    function _event($element, linkDialog) {
        $element.on('click', function() {
            if (!linkDialog.initStatus) {//getData from server
                $.when($.get('/web/'+linkDialog.options.webId+'/content/pageDialog'),
                       $.get('/web/'+linkDialog.options.webId+'/content/articleDialog'))
                    .done(function(pageData, articleData) {
                        // layer.load(2)
                        var pageHTML = []
                        pageHTML.push('<div class="container-page">')

                        iteratorHTML(pageData[0])

                        
                        function iteratorHTML(d) {
                            if (d.length === 0) return

                            pageHTML.push('<ul>')
                            d.forEach(function(item) {

                                var t = item.menu.linkType


                                var sl = item.subMenu.length > 0 ? '<span class="tree-node" style="width: 15px; display: inline-block;">+</span>' : '<span style="width: 15px; display: inline-block;"></span>'

                                var iconType = (t == 1? 'page': '')
                                var radioHTML = (t == 1? '<input type="radio" style="visibility: hidden" name="pageRadios" value="/'+item.page.name+'" data-title="'+item.menu.title+'">': '')

                                pageHTML.push('<li class="link'+item.menu.linkType+'"> '+
                                    '<div class="radio '+iconType+'">'+
                                    '<label>'+
                                    ''+radioHTML+''+
                                    ''+sl+''+item.menu.title+'<i class="iconfont icon-'+iconType+'" style="color: #5884fd;position: relative; left: 5px;top: 2px;"></i>'+
                                    '</label>'+
                                    ' </div>'+
                                    '')
                                iteratorHTML(item.subMenu)
                                pageHTML.push('</li>')
                            })

                            pageHTML.push('</ul>')
                        }

                        pageHTML.push('</div>')
                        //end


                        linkDialog.pageHTML = pageHTML.join('')
                        linkDialog.articleHTML = articleData[0]
                        linkDialog.initStatus = true
                        openLinkDialog()

                    })
            } else {
                openLinkDialog()
            }


            function openLinkDialog() {
                var tabId = 'tab-'+ linkDialog.$element.attr('id')

                layer.tab({
                        area: ['600px', '600px'],
                        btn: ['确定','取消'],
                        // closeBtn: 0,
                        id: tabId,
                        btnAlign: 'c',
                        tab: [{
                            title: '页面',
                            content: linkDialog.pageHTML
                        }, {
                            title: '图文列表',
                            content: linkDialog.articleHTML
                        }],
                        success: function() {

                            $('#'+tabId+' ~ .layui-layer-btn .layui-layer-btn0').addClass('disabled')

                            
                            $("#"+tabId).delegate('input[type=radio]', 'click', function() {
                                // linkDialog.lk.href = $(this).val()
                                // linkDialog.lk.title = $(this).data('title')
                                $('#'+tabId+' ~ .layui-layer-btn .layui-layer-btn0').removeClass('disabled')
                            })


                            $('.tree-node').on('click', function () {
                                var text = $(this).text()
                                if (text === '-') {
                                    $(this).text('+')
                                } else {
                                    $(this).text('-')
                                }

                                $(this).parents('.radio').next().toggle()
                            })

                            $('.container-page div.page').on('click', function () {
                                $('.container-page div.page').removeClass('active')
                                $(this).addClass('active')
                            })

                            // layer.closeAll('loading')
                        },
                        change: function (index) {
                            //remove all checked
                            $("#"+tabId+" input[type=radio]").prop("checked",false);
                            $('#'+tabId+' ~ .layui-layer-btn .layui-layer-btn0').addClass('disabled')

                            linkDialog.lk.typeIndex = index

                            linkDialog.lk.typeName = $("#"+tabId+" layui-layer-title>span:eq("+index+")").text()
                            linkDialog.lk.href = ''

                            $('.container-page div.page').removeClass('active')


                        },
                        yes: function (index, layero) {
                            var $checked = $('#'+tabId + ' input[type=radio]:checked')
                            var href = $checked.val()


                            if(!href) {
                                layer.msg('请选择一个内部链接', {icon: 2})
                                return
                            }

                            linkDialog.lk.href = href
                            linkDialog.lk.title = $checked.data('title')


                            layer.close(index)
                            linkDialog.options.success && linkDialog.options.success(linkDialog.lk)
                        }
                    });
             }

        })
    }

    var linkDialog;

    $.fn.LinkDialog = function(options) {
        var args = arguments;
        var value;
        var chain = this.each(function() {
            var data = $(this).data("LinkDialog");
            if (!data) {
                if (options && typeof options == 'object') { //初始化
                    return $(this).data("LinkDialog", linkDialog = data = new LinkDialog(this, options));
                }
            } else {
                if (typeof options == 'string') {
                    if (data[options] instanceof Function) { //调用方法
                        var property = options; [].shift.apply(args);
                        value = data[property].apply(data, args);
                    } else { //获取属性
                        return value = data.options[options];
                    }
                }
            }

        });

        if (value !== undefined) {
            return value;
        } else {
            return chain;
        }

    };


})(jQuery)