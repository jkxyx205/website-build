;!(function($) {
    'use strict'

    var rows = 20

    document.write('<link rel="stylesheet" href="/static/plugins/lightBox/css/lightbox.min.css"><script src="/static/plugins/uploadify5/jquery.uploadifive.min.js"></script><script src="/static/plugins/Jcrop/js/jquery.Jcrop.min.js"></script><script src="/static/plugins/lightBox/js/lightbox.min.js"></script>')

    var PicDialog = function(element, options) {
        this.$element = $(element);
        this.options = $.extend({},$.fn.PicDialog.defaults, options); //合并参数
        this.selected = [] //图片ids
        this.jcrop = null
        this.galleryInitStatus = false
        this.init();
    };

    PicDialog.prototype = {
        constructor: PicDialog,
        init: function() {
            this.$element.css('cursor', 'pointer')
            this._registerEvent()
        },
        isAutoSelect: function() {//是否是自由裁剪
            if(this.options.aspectRatioW === 0 
                || this.options.aspectRatioH == 0) {
                return true
            }

            return false
        },
        _initUI: function() {
            this.galleryInitStatus = false
            if (this.options.multiSelect)
                $('.layui-layer-btn').append('<div class="multi-count" id="multi-count"><span class="pic-box-selected-count" id="pic-box-selected-count">0</span>/<span id="multiMaxCount"></span></div>')

            this.$btn = {
                "prve": $('#picDialog~.layui-layer-btn .layui-layer-btn1'),
                "next": $('#picDialog~.layui-layer-btn .layui-layer-btn0'),
                "finish": $('#picDialog~.layui-layer-btn .layui-layer-btn2')
            }

            this.$panel = {
                "containerPic": $('#container-pic'),
                "containerCrop": $('#container-crop'),
                "panelUpload": $('#panel-upload'),
                "panelImg": $('#panel-img')
            }

            this.$panel.panelImg.hide()

            if (this.options.multiSelect) {//多选
                this.$btn.prve.hide()
                this.$btn.next.hide()
                $('#multiMaxCount').text(this.options.multiMaxCount)


            } else { //单选
                this.$btn.prve.hide()
                this.$btn.finish.hide()
                $('#multi-count').hide()

            }

            this.selected = []


            var picDialog = this

            picDialog.loading = false

            $("div.lazy").lazyload({
                // effect : "fadeIn"
            });

            $('#pic-list').scroll(function() { //加载本地图片
                var page = $(this).data('page')
                var st = $(this).scrollTop()
                var scroll = $(this).data('scroll')

                if (page === -1 || st < scroll || picDialog.loading) return


                var $this = $(this)

                if($(this)[0].scrollHeight - st - $(this).height() < 100){ //到达底部100px时,加载新内容
                    picDialog.loading = true
                    
                    ++page

                    console.log('loading page => ' + page)

                    var name = $('#query_name2').val()

                    picDialog._getGallery(1, null, name, page, function(html, length) {
                        $this.append(html)

                        $this.data('page', length < rows ? -1: page)
                        $this.data('scroll', st)
                        picDialog.loading = false


                        $this.find("div.lazy").lazyload({
                            threshold: 300,
                            // effect : "fadeIn"
                        })
                    })

                }
            })

               $('#pic-list2').scroll(function() { //加载图库图片
                var page = $(this).data('page')
                var st = $(this).scrollTop()
                var scroll = $(this).data('scroll')

                if (page === -1 || st < scroll || picDialog.loading) return


                var $this = $(this)

                if($(this)[0].scrollHeight - st - $(this).height() < 100){ //到达底部100px时,加载新内容
                    picDialog.loading = true
                    
                    ++page

                    console.log('loading page => ' + page)

                    var pid = $('#pid').val()
                    var name = $('#query_name').val()

                    picDialog._getGallery(0, pid, name, page, function(html, length) {
                        $this.append(html)
                        $this.data('page', length < rows ? -1: page)
                        $this.data('scroll', st)
                        picDialog.loading = false

                        $this.find("div.lazy").lazyload({
                            threshold: 300,
                            // effect : "fadeIn"
                        })
                    })

                }
            })


        },
        _getPathBySrc:function(id) {
            return $('.pic-box[data-src='+id+']').data('path')
        },
        _stopCrop: function() {
            this.jcrop.disable()
            this.jcrop.release()
            $('#btn-crop').text('启用裁剪').data('status', false).removeClass('btn-default').addClass('btn-primary')
            $('#jcrop-aspect-ratio').hide()
            if(this.isAutoSelect()) {
                this.jcrop.setOptions({
                    aspectRatio: 0
                })

                $('#btn-auto').addClass('btn-primary').siblings().removeClass('btn-primary')
            }

        },
        _getGallery: function(categoryId, pid, name, page, callback) {
            var picDialog = this

            $.get('/web/'+this.options.webId+'/content/api/picList', {"categoryId": categoryId, "page": page, "name": name, "pid": pid}, function (data) {
                callback && callback(picDialog._data2HTML(data, name), data.length)
            })

        },
        _data2HTML: function(data, name) {
                var html = []
                data.forEach(function(pic) {
                            html.push('<div class="pic-box"  data-src="'+pic.id+'" data-path="'+pic.urlThumbnailPath+'"> ' +
                                ' <div class="pic-box-img lazy" data-original="'+pic.urlThumbnailSmallPath+'" style="background-image: url('+fileServer+'/tpl/init/1x1/0.jpg);">' +
                                ' <div class="pic-box-s"></div>' +
                                '<div class="pic-box-b">' +
                        '<a href="'+pic.urlThumbnailPath+'" data-lightbox="pic-lightbox'+pic.id+'" data-title="'+pic.name+'">' +
                        '   <i class="iconfont icon-screen_expand" style="margin-right: 5px; color: #fff;"></i>' +
                        '   </a>' +
                                ' <span class="pic-box-label" style="color: #fff; position: absolute; left: 0; bottom: 0; width: 100%; background-color: transparent; text-align: left; " title="'+pic.name+'">'+pic.name+'</span>'+
                        '   </div>' +
                                ' </div>' +
                                // ' <div class="pic-box-label" title="'+pic.name+'">'+pic.name+'</div>' +
                                ' </div>')
                })

                html = html.join("")

                // if (typeof name != 'undefined' && name) {
                //     html = html.replace(new RegExp(name,'g'), '<span style="color: #5985ff;">'+name+'</span>')
                // }

                


                if(this.options.multiSelect) {
                    var $html = $('<div>'+html+'</div>')
                    this.selected.forEach(function(src) {
                        $html.find('.pic-box[data-src='+src+'] .pic-box-s').addClass('pic-box-selected')
                    })
                    html = $html.html()
                }

                return html;

                
        },
        _registerEvent: function () {
            var picDialog = this

            this.$element.on('click', function() {
                    $.get('/web/'+picDialog.options.webId+'/content/picDialog', function(content) {
                        layer.open({
                            type: 1,
                            id: 'picDialog',
                            area: ['800px', '600px'],
                            shade: .5,
                            shadeClose: true,
                            title: false,
                            resize: false,
                            content: content,
                            btn:['下一步', '上一步', '完成'],
                            btnAlign: 'c',
                            yes: function(index, layero) { //下一步
                                if(picDialog.selected.length ===0) {
                                    layer.msg('请选择图片！', {icon: 2})
                                    return
                                }
                                var index = layer.load(2)
                                picDialog.jcrop.setImage(picDialog._getPathBySrc(picDialog.selected[0]), function () {
                                    picDialog.$panel.containerPic.hide()
                                    picDialog.$panel.containerCrop.show()

                                    picDialog.$btn.next.hide()
                                    picDialog.$btn.prve.show()
                                    picDialog.$btn.finish.show()
                                    layer.close(index)
                                })

                            },
                            btn2: function(index, layero) { //上一步
                                picDialog.$panel.containerPic.show()
                                picDialog.$panel.containerCrop.hide()

                                picDialog.$btn.next.show()
                                picDialog.$btn.prve.hide()
                                picDialog.$btn.finish.hide()

                                picDialog._stopCrop()
                                return false
                            },
                            btn3: function(index, layero) { //完成


                                var cropParam = {}

                                cropParam.aspectRatioW = picDialog.options.aspectRatioW
                                cropParam.aspectRatioH = picDialog.options.aspectRatioH


                                if (picDialog.options.multiSelect) {//批量使用图片
                                    if (picDialog.selected.length === 0) {
                                        layer.msg('请选择图片！', {icon: 2})
                                        return false
                                    }

                                    cropParam.ids = picDialog.selected.join(';')

                                    $.post('/download/multi_auto_crop', cropParam,  function (data) {
                                        picDialog.options.success &&picDialog.options.success(data)
                                        layer.close(index)
                                    })
                                } else { //单个使用图片
                                    var status = $('#btn-crop').data('status')
                                    if (status) {
                                        console.log('启用裁剪...')

                                        cropParam.x = parseInt(picDialog.jcrop.tellSelect().x < 0 ? 0 : picDialog.jcrop.tellSelect().x)
                                        cropParam.y = parseInt(picDialog.jcrop.tellSelect().y < 0 ? 0 : picDialog.jcrop.tellSelect().y)
                                        cropParam.w = parseInt(picDialog.jcrop.tellSelect().w)
                                        cropParam.h = parseInt(picDialog.jcrop.tellSelect().h)

                                        $.post('/download/manual_crop/' + picDialog.selected[0], cropParam,  function (data) {
                                            picDialog.options.success &&picDialog.options.success(data)
                                            layer.close(index)
                                        })



                                    } else {
                                        console.log('未使用裁剪...')
                                        $.post('/download/auto_crop/' + picDialog.selected[0], cropParam,  function (data) {
                                            picDialog.options.success &&picDialog.options.success(data)
                                            layer.close(index)
                                        })

                                    }
                                }
                            },
                            success: function() {
                                picDialog._initUI()

                                //本地图片 和 图库 切换
                                $('#menu-left >ul > li').on('click', function () {
                                    $(this).addClass('active').siblings().removeClass('active')
                                    $($(this).data('target')).show().siblings().hide()

                                    $(this).siblings().children('ul').toggle()
                                    $(this).children('ul').show()

                                    if (!picDialog.options.multiSelect) {
                                        picDialog.selected = []
                                        $('.pic-box-selected').removeClass('pic-box-selected')
                                    }

                                    if (!picDialog.galleryInitStatus) {
                                        picDialog.galleryInitStatus = true
                                        $('ul.second-level li.active').click()
                                    }

                                })

                                //图库分类选择
                                $('#menu-left ul.second-level li').on('click', function () {
                                    $(this).addClass('active').siblings().removeClass('active')

                                    if (!picDialog.options.multiSelect) {
                                        picDialog.selected = []
                                        $('.pic-box-selected').removeClass('pic-box-selected')
                                    }

                                    var pid = $(this).data('pid')
                                    $('#pid').val(pid)
                                    $('#query_name').val('')

                                    picDialog._getGallery(0,pid, null, 1, function(html, length) {
                                        $('#pic-list2').html(html).data('page', length < rows ? -1 : 1).data('scroll', 0).scrollTop(0).
                                        find("div.lazy").lazyload({
                                            threshold: 300,
                                            // effect : "fadeIn"
                                        })
                                    })

                                })

                                $('#form-gallery').ajaxForm(function(data) {
                                    if(data.length == 0) {
                                        $('#pic-list2').html('<div style="text-align: center; line-height: 40px;">暂无数据</div>')
                                    } else {
                                        $('#pic-list2').html(picDialog._data2HTML(data, $('#query_name').val())).data('page', data.length < rows ? -1 : 1).data('scroll', 0).scrollTop(0).
                                        find("div.lazy").lazyload({
                                            threshold: 300,
                                            // effect : "fadeIn"
                                        })
                                    }
                                })


                                $('#form-local').ajaxForm(function(data) {
                                    if(data.length == 0) {
                                        $('#pic-list').html('<div style="text-align: center; line-height: 40px;">暂无数据</div>')
                                    } else {
                                        $('#pic-list').html(picDialog._data2HTML(data, $('#query_name2').val())).data('page', data.length < rows ? -1 : 1).data('scroll', 0).scrollTop(0).
                                        find("div.lazy").lazyload({
                                            threshold: 300,
                                            // effect : "fadeIn"
                                        })
                                    }
                                })



                                //文件上传
                                $('#pic_upload').uploadifive({
                                    'fileObjName': 'u_files',
                                     height: 'auto',
                                    'auto'       : true,
                                    'buttonText':'本地上传',
                                    'multi'        : true,
                                    removeCompleted: true,
                                    'fileSizeLimit' : '5MB',
                                    'uploadLimit'  : 20,
                                    'queueSizeLimit' : 20,
                                    'fileType'     : 'image',
                                    'buttonClass': 'btn-upload-class',
                                    'uploadScript'     : '/upload/file?u_folder=site/'+picDialog.options.webId+'&webId='+picDialog.options.webId+'&u_categoryId=1',
                                    'onSelect':function() {

                                    },
                                    'onUploadComplete' : function(file, data) {
                                        data = JSON.parse(data)
                                        for (var i = 0; i< data.length; i++) {
                                            var pic = data[i]

                                            var $picList = $('#pic-list')

                                            $picList.prepend(picDialog._data2HTML(data))
                                                .find("div.lazy").lazyload({
                                                threshold: 300,
                                                // effect : "fadeIn"
                                            })

                                        }

                                        $picList.data('scroll', 0)
                                        $picList.scrollTop(0)

                                        var page = $picList.data('page')
                                        if (page != -1) {//重新计算页码
                                            var total = page + data.length
                                            $picList.data('page', Math.ceil(total/rows))
                                        }
                                    }
                                });


                                //图片选择
                                $('#content-right').on('click', '.pic-box', function(e) {
                                    if (e.target && e.target.tagName === 'I') return

                                    var src = $(this).data('src')

                                    if(picDialog.options.multiSelect) {   //多选
                                        
                                        if ($.inArray(src, picDialog.selected) > -1) { //取消选择
                                            picDialog.selected.splice($.inArray(src, picDialog.selected),1);
                                             $(this).find('.pic-box-s').removeClass('pic-box-selected')   
                                        } else { //添加选择
                                            if(picDialog.selected.length >= picDialog.options.multiMaxCount) {
                                                layer.msg('图片数量不能超过'+picDialog.options.multiMaxCount+'！', {icon: 2})
                                                return
                                            }

                                            picDialog.selected.push(src)
                                            $(this).find('.pic-box-s').addClass('pic-box-selected')   
                                        }

                                        $('#pic-box-selected-count').text(picDialog.selected.length)

                                    } else {
                                        $('.pic-box-selected').removeClass('pic-box-selected')
                                        $(this).find('.pic-box-s').addClass('pic-box-selected')

                                        if ($.inArray(src, picDialog.selected) === -1) {
                                            picDialog.selected = [src]
                                        }
                                    }

                                    console.log(picDialog.selected)
                                })

                                if (!picDialog.options.multiSelect) { //单选
                                        // if (picDialog.jcrop === null)
                                            picDialog.initJcrop()


                                        //图片裁剪
                                        $('#btn-crop').on('click', function() {
                                            var status = $(this).data('status')

                                            picDialog.jcrop.setSelect([0, 0, picDialog.jcrop.getBounds()[0], picDialog.jcrop.getBounds()[1]]);

                                            if (!status) {//启动裁剪
                                                picDialog.jcrop.enable()
                                                
                                                $(this).text('取消裁剪').removeClass('btn-primary').addClass('btn-default')
                                                
                                                //是否自由裁剪 
                                                if (picDialog.isAutoSelect())
                                                    $('#jcrop-aspect-ratio').show()


                                            } else {//取消裁剪
                                                picDialog._stopCrop()
                                            }

                                            $(this).data('status', !status)
                                            
                                        })

                                        if (picDialog.isAutoSelect()) {
                                            //设置裁剪比例
                                            $('#jcrop-aspect-ratio').on('click', 'button', function() {
                                                $(this).siblings().removeClass('btn-primary')

                                                $(this).addClass('btn-primary')


                                                var aspectRatio = $(this).data('area').split('x')
                                                
                                                var aspectRatioW = parseInt(aspectRatio[0])
                                                var aspectRatioH = parseInt(aspectRatio[1])

                                                picDialog.jcrop.setOptions({
                                                    aspectRatio: aspectRatioW/aspectRatioH
                                                })

                                                // picDialog.jcrop.focus();
                                            })
                                        }
                                }

                                

                               
                            }
                         });  
                    })
               
            })


        },
        initJcrop: function() {
            var picDialog = this

            $('#img-target').Jcrop({
                aspectRatio: this.options.aspectRatioW / (this.options.aspectRatioH == 0 ? 1 : this.options.aspectRatioH),
                allowSelect: false,
                bgOpacity: 0.5,
                keySupport: true,
                bgColor: 'white',
                addClass: 'jcrop-light',
                boxWidth: 300,    // 设置框的最大宽度
                boxHeight: 300,    // 设置框的最大宽度
                onSelect: function (obj) {
                    // _this.point.x = parseInt(obj.x<0 ? 0 : obj.x)
                    // _this.point.y = parseInt(obj.y<0 ? 0 : obj.y)
                    // _this.point.w = parseInt(obj.w)
                    // _this.point.h = parseInt(obj.h)
                },
            }, function () {
                picDialog.jcrop = this
                this.setOptions({ bgFade: true });
                this.ui.selection.addClass('jcrop-selection');
                this.disable()
            });
        }
    }


    $.fn.PicDialog = function(options) {
        var args = arguments;
        var value;
        var chain = this.each(function() {
            var data = $(this).data("picDialog");
            if (!data) {
                if (options && typeof options == 'object') { //初始化
                    return $(this).data("picDialog", data = new PicDialog(this, options));
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

    $.fn.PicDialog.defaults = {
         aspectRatioW: 0,
         aspectRatioH: 0,
         multiSelect: false,
         multiMaxCount: 20
    };

})(jQuery);