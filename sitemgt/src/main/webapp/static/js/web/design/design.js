;(function ($, w, site) {
    'use strict'

    var design = {
        saving: false, //保存中
        fullActive: false,
        readStatus: false, //默认为编辑模式 //TODO
        iframe: window.frames["iframe"],
        headerTpl: false, //状态：是否需要获取 页眉数据
        footerTpl: false, //状态：是否需要获取 页脚数据
        styleTpl: false, //状态：是否需要获取 风格数据
        init: function () {
            console.log('[design] preparing site => '+ site.id)

            layer.load(2);

            _eventBind()
            _changeLink()

        },
        save: function(callback) {
            if (design.saving) return

            console.log('saving.....')
            design.saving = true

            //change icon

            $('#save').children('i').removeClass('icon-save').addClass('icon-loading')

            this.iframe.design.save(function () {
                console.log('saved.....')
                design.saving = false
                callback && callback()
            })
        },
        monitor: function() {
            setInterval(function () {
                if(!design.saving) {
                    $('#save').children('i').removeClass('icon-loading').addClass('icon-save')
                }
            }, 1000)

            setInterval(function () {
                if(!design.saving) {
                    design.save()
                }
            }, 60000)
        },
        iframeReady: function () {
            console.log('[design] ready.....')
            this.getHeaderTplHTML()
            this.getFooterTplHTML()
            this.getStyleTplHTML()
            layer.closeAll('loading')
            this.monitor()
        },
        getHeaderTplHTML: function () {
            if (!design.headerTpl) {
                $.ajax({
                    url: '/web/'+site.id+'/design/api/tpl/'+site.styleId+'/'+site.headerId+'/'+site.footerId+'/'+site.blockId+'?mode=header',
                    type: 'get',
                    dataType: 'json',
                    async: false,
                    success: function (data) {
                        var HTMLArr = []
                        HTMLArr.push('<div class="design-img-container header-img-container">')

                        data.forEach(function (theme) {
                            HTMLArr.push('<div data-header-id="'+theme.headerId+'" class="design-img-box"><img src="'+fileServer+'/tpl/header/'+5+'-'+theme.styleId+'-'+theme.headerId+'-'+theme.blockId + '.png" alt="" /><div class="design-img-box-mask"></div><button class="btn btn-primary btn-sm">使用</button></div>')
                        })

                        HTMLArr.push('</div>')

                        design.headerTPLHTML = HTMLArr.join('')
                        design.headerTpl = true
                    }
                });
            }

            return design.headerTPLHTML;
        },
        getFooterTplHTML: function () {
            if (!design.footerTplTpl) {
                $.ajax({
                    url: '/web/'+site.id+'/design/api/tpl/'+site.styleId+'/'+site.headerId+'/'+site.footerId+'/'+site.blockId+'?mode=footer',
                    type: 'get',
                    dataType: 'json',
                    async: false,
                    success: function (data) {
                        var HTMLArr = []
                        HTMLArr.push('<div class="design-img-container">')

                        data.forEach(function (theme) {
                            HTMLArr.push('<div data-footer-id="'+theme.footerId+'" class="design-img-box"><img src="'+fileServer+'/tpl/footer/'+5+'-'+theme.styleId+'-'+theme.footerId+'-'+theme.blockId + '.png" alt="" /><div class="design-img-box-mask"></div><button class="btn btn-primary btn-sm">使用</button></div>')
                        })

                        HTMLArr.push('</div>')

                        design.footerTPLHTML = HTMLArr.join('')
                        design.footerTplTpl = true
                    }
                });
            }

            return design.footerTPLHTML;
        },
        getStyleTplHTML: function () {
            if (!design.styleTpl) {
                $.ajax({
                    url: '/web/'+site.id+'/design/api/tpl/'+site.styleId+'/'+site.headerId+'/'+site.footerId+'/'+site.blockId+'?mode=style',
                    type: 'get',
                    dataType: 'json',
                    async: false,
                    success: function (data) {
                        var HTMLArr = []
                        HTMLArr.push('<div class="swiper-container" id="style-swiper-container"><div class="swiper-wrapper design-img-container">')

                        data.forEach(function (theme) {
                            HTMLArr.push('<div data-style-id="'+theme.styleId+'" class="swiper-slide design-img-box"><img src="'+fileServer+'/tpl/style/'+5+'-'+theme.styleId+'-'+theme.headerId+'-'+theme.footerId+'-'+theme.blockId + '.png" alt="" /><div class="design-img-box-mask"></div><button class="btn btn-primary">使用</button></div>')
                        })
                        HTMLArr.push('</div><div class="swiper-button-next"></div><div class="swiper-button-prev"></div></div>')

                        design.styleTPLHTML = HTMLArr.join('')
                        design.styleTpl = true
                    }
                });
            }

            return design.styleTPLHTML;
        }
    }


    function _eventBind() {
        _readStatus()
        _toggleFullEvent()
        _saveBtn()
        _colorChoose()
        _dialogHeader()
        _dialogFooter()
        _dialogLayout()
        _dialogStyle()
    }

    function _changeLink() {


        $('a[href^="/"]').each(function () {
            this.link = this.href
            this.href = "javascript:;"

            $(this).on('click', function() {
                var _a = this
                design.save(function () {
                     if('_blank' === _a.target) {
                         newWin(_a.link)
                     } else {
                         location.href = _a.link
                     }

                })
            })
        })
    }

    function _readStatus() {

        $("#status")
            .switchbutton({
                checkedLabel: '编辑',
                uncheckedLabel: '预览'
            })
            .change(function(){
                    design.readStatus = !design.readStatus
                    design.iframe.design.setReadStatus(design.readStatus)
            });


        // $('#status').switchButton({
        //     on_label: '编辑',
        //     off_label: '编辑',
        //     labels_placement: "left"
        // }).on('change', function () {
        //     design.readStatus = !design.readStatus
        //     design.iframe.design.setReadStatus(design.readStatus)
        // })


        // $('#status').on('click', function (event) {
        //
        //     design.readStatus = !design.readStatus
        //     design.iframe.design.setReadStatus(design.readStatus)
        //
        // })

    }

    function _toggleFullEvent() {
        $('#design-bar-full').on('click', function() {
            execute()
        })

        $(document).on('keyup', function(event) {
            var e = event || window.event //|| arguments.callee.caller.arguments[0];
            if(e && (e.keyCode==27 || e.keyCode==96) && design.fullActive) execute()
        })

        function execute() {
            if(!design.fullActive) {
                $('#inner-page').addClass('full-active')
            } else {
                $('#inner-page').removeClass('full-active')
            }
            design.fullActive = !design.fullActive
        }
    }



    function _saveBtn() {
        $('#save').on('click', function () {
            design.save()
        })
    }

    function _colorChoose() {
        $('#c' + site.blockId).addClass('active')


        $('#color-panel a').on('click', function() {
            var blockId = $(this).data('block-id')

            if(blockId == site.blockId) return

            $(this).addClass('active')
            $('#c' + site.blockId).removeClass('active')

            site.blockId = blockId

            console.log('[design] update blockId => '+ blockId)


            design.iframe.design.updateBlock(blockId, function() {
                design.headerTpl = false
                design.footerTpl = false
                design.styleTpl = false

                layer.closeAll();
            })
        })

    }

    function _dialogHeader() {
        $('#dialogHeader').on('click', function() {
            var top = $('#design-container').offset().top + 'px';
            layer.open({
                id:'headerTpl',
                type: 1,
                title: false,
                anim: 5,
                closeBtn: 0,
                shadeClose: true,
                area: ['1200px', '500px'],
                resize:false,
                offset: top,
                content: design.getHeaderTplHTML(),
                success: function(layero, index) {
                    // $("#headerTpl").niceScroll();
                    $('#headerTpl .design-img-box').on('click', function () {
                        // layer.load(2)
                        layer.close(index);
                        var headerId = $(this).data('header-id')
                        // console.log('[design] update headerId => '+ headerId)

                        site.headerId = headerId
                        design.iframe.design.updateHeader(headerId, function() {
                            layer.closeAll();
                        })
                    })
                }
            });

        })
    }

    function _dialogFooter() {
        $('#dialogFooter').on('click', function() {
            var top = $('#design-container').offset().top + 'px';
            layer.open({
                type: 1,
                title: false,
                closeBtn: 0,
                anim: 5,
                id:'footerTpl',
                shadeClose: true,
                area: ['1200px', '500px'],
                resize:false,
                offset: top,
                content: design.getFooterTplHTML(),
                success: function (layero, index) {
                    // $("#footerTpl").niceScroll();
                    $('#footerTpl .design-img-box').on('click', function () {
                        // layer.load(2)
                        layer.close(index);
                        var footerId = $(this).data('footer-id')
                        console.log('[design] update footerId => '+ footerId)

                        site.footerId = footerId
                        design.iframe.design.updateFooter(footerId, function() {
                            layer.closeAll();
                        })
                    })
                }
            });

        })
    }

    function _dialogLayout() {
        $('#dialogLayout').on('click', function() {
            var top = $('#design-container').offset().top + 'px';
            layer.open({
                type: 1,
                id:'layout-img-container',
                title: false,
                closeBtn: 0,
                shadeClose: true,
                anim: 5,
                area: '1200px',
                resize:false,
                offset: top,
                content: '<ul class="design-img-container layout-img-container">'+
                '<li data-layout-id="1" class="design-img-box" style="background-position: 0 0;"><div class="design-img-box-mask"></div><button class="btn btn-primary btn-xs">重置</button></li>'+
                '<li data-layout-id="2" class="design-img-box" style="background-position: -99px 0;"><div class="design-img-box-mask"></div><button class="btn btn-primary btn-xs">重置</button></li>'+
                '<li data-layout-id="3" class="design-img-box" style="background-position: -198px 0;"><div class="design-img-box-mask"></div><button class="btn btn-primary btn-xs">重置</button></li>'+
                '<li data-layout-id="4" class="design-img-box" style="background-position: -297px 0;"><div class="design-img-box-mask"></div><button class="btn btn-primary btn-xs">重置</button></li>'+
                '<li data-layout-id="5" class="design-img-box" style="background-position: -396px 0;"><div class="design-img-box-mask"></div><button class="btn btn-primary btn-xs">重置</button></li>'+
                '<li data-layout-id="6" class="design-img-box" style="background-position: -495px 0;"><div class="design-img-box-mask"></div><button class="btn btn-primary btn-xs">重置</button></li>'+
                '<li data-layout-id="7" class="design-img-box" style="background-position: -594px 0;"><div class="design-img-box-mask"></div><button class="btn btn-primary btn-xs">重置</button></li>'+
                '<li data-layout-id="8" class="design-img-box" style="background-position: -693px 0;"><div class="design-img-box-mask"></div><button class="btn btn-primary btn-xs">重置</button></li>'+
                '<li data-layout-id="9" class="design-img-box" style="background-position: -792px 0;"><div class="design-img-box-mask"></div><button class="btn btn-primary btn-xs">重置</button></li>'+
                '<li data-layout-id="10" class="design-img-box" style="background-position: -891px 0;"><div class="design-img-box-mask"></div><button class="btn btn-primary btn-xs">重置</button></li>'+
                '<div class="clearfix"></div></ul>',
                success: function (layero, index) {
                    $('#layout-img-container .design-img-box').on('click', function() {
                        _resetLayout($(this).data('layout-id'))
                    })
                    
                    function _resetLayout(layoutId) {
                            layer.close(index);
                            design.iframe.design.resetLayout(layoutId, function() {

                            })
                    }
                }
            });

        })

    }

    function _dialogStyle() {
        $('#dialogStyle').on('click', function() {
            var top = $('#design-container').offset().top + 'px';
            layer.open({
                type: 1,
                title: false,
                id:'styleTpl',
                anim: -1,
                // closeBtn: 0,
                shadeClose: true,
                area: '1200px',
                resize:false,
                offset: top,
                content: design.getStyleTplHTML(),
                success: function(layero, index){
                    _initSwiper()

                $('#styleTpl .design-img-box').on('click', function () {
                    // layer.load(2)
                    var styleId = $(this).data('style-id')
                    layer.close(index);
                    console.log('[design] update styleId => '+ styleId)

                    site.styleId = styleId
                    design.iframe.design.updateStyle(styleId, function() {
                        design.headerTpl = false
                        design.footerTpl = false
                        design.styleTpl = false

                    })
                })


                },

            });

        })

        function _initSwiper() {
            new Swiper('#style-swiper-container', {
                loop: true,
                effect: 'coverflow',
                grabCursor: true,
                centeredSlides: true,
                slidesPerView: 'auto',
                coverflowEffect: {
                    rotate: 10,
                    stretch: -50,
                    depth: 400,
                    modifier: 1,
                    slideShadows : true,
                },
                navigation: {
                    nextEl: '.swiper-button-next',
                    prevEl: '.swiper-button-prev',
                },
                on: {
                    slideChange: function () {
                        // var $slide = $(this.slides[this.realIndex]);
                        // _this.styleId = $slide.data('style-id')
                    }
                }
            });
        }

    }

    window.design = design

})(jQuery, window, site)


$(function() {
    design.init()
})