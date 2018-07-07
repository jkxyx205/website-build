;!(function($) {
    'use strict'

    var rows = 20

    document.write('<script src="/static/plugins/uploadify5/jquery.uploadifive.min.js"></script>')

    var VideoDialog = function(element, options) {
        this.$element = $(element);
        this.options = $.extend({},$.fn.VideoDialog.defaults, options); //合并参数
        this.selected = null;
        this.init();
    };

    VideoDialog.prototype = {
        constructor: VideoDialog,
        init: function() {
            this.$element.css('cursor', 'pointer')
            this._registerEvent()
        },
        _initUI: function() {

            this.$panel = {
                "containerPic": $('#container-pic'),
                "panelUpload": $('#panel-upload'),
                "panelImg": $('#panel-img')
            }

            this.$panel.panelImg.hide()

            this.selected = null;


            var videoDialog = this

            videoDialog.loading = false

            $("div.lazy").lazyload({
                // effect : "fadeIn"
            });

            $('#pic-list').scroll(function() { //加载本地图片
                var page = $(this).data('page')
                var st = $(this).scrollTop()
                var scroll = $(this).data('scroll')

                if (page === -1 || st < scroll || videoDialog.loading) return


                var $this = $(this)

                if($(this)[0].scrollHeight - st - $(this).height() < 100){ //到达底部100px时,加载新内容
                    videoDialog.loading = true
                    
                    ++page

                    console.log('loading page => ' + page)
                }
            })

        },
        _getPathBySrc:function(id) {
            return $('.pic-box[data-src='+id+']').data('path')
        },
        _data2HTML: function(data, name) {
                var html = []
                data.forEach(function(pic) {
                            html.push('<div class="pic-box"  data-src="'+pic.id+'" data-path="'+pic.urlPath+'"> ' +
                                ' <div class="pic-box-img"  style="background-image: url('+fileServer+'/tpl/init/1x1/video.png);">' +
                                ' <div class="pic-box-s"></div>' +
                                '<div class="pic-box-b">' +
                        '<a href="javascript:;"  data-path="'+pic.urlPath+'">' +
                        '   <i class="iconfont icon-screen_expand" style="margin-right: 5px; color: #fff;"></i>' +
                        '   </a>' +
                                ' <span class="pic-box-label" style="color: #fff; position: absolute; left: 0; bottom: 0; width: 100%; background-color: transparent; text-align: left; " title="'+pic.name+'">'+pic.name+'</span>'+
                        '   </div>' +
                                ' </div>' +
                                // ' <div class="pic-box-label" title="'+pic.name+'">'+pic.name+'</div>' +
                                ' </div>')
                })

                html = html.join("")


                return html;

                
        },
        _registerEvent: function () {
            var videoDialog = this

            this.$element.on('click', function() {
                    $.get('/web/'+videoDialog.options.webId+'/content/videoDialog', function(content) {
                        layer.open({
                            type: 1,
                            id: 'videoDialog',
                            area: ['800px', '600px'],
                            shade: .5,
                            shadeClose: true,
                            title: false,
                            resize: false,
                            content: content,
                            btn:['确定', '取消'],
                            btnAlign: 'c',
                            yes: function(index, layero) { //完成
                                var html = $('#url-fill').val()
                                if(!videoDialog.selected && !html) {
                                    layer.msg('请选择视频或填写正确的网络地址！', {icon: 2})
                                    return
                                }

                                if (videoDialog.selected) {
                                    var auHTML = ''
                                    if (videoDialog.options.autoplay)
                                        auHTML = 'autoplay="autoplay"'

                                    var embed = []
                                    embed.push('<video controls="controls" src="'+videoDialog._getPathBySrc(videoDialog.selected)+'" width="480px"  '+auHTML+' style="max-width: 100%; height: auto;">')
                                    embed.push('</video>')
                                    html = embed.join('')
                                }

                                //TODO check
                                videoDialog.options.success &&videoDialog.options.success(html)


                                layer.close(index)
                            },
                            success: function() {
                                videoDialog._initUI()

                                //本地图片 和 视频 切换
                                $('#menu-left >ul > li').on('click', function () {
                                    $(this).addClass('active').siblings().removeClass('active')
                                    $($(this).data('target')).show().siblings().hide()

                                    $(this).siblings().children('ul').toggle()
                                    $(this).children('ul').show()

                                    // url-fill
                                    videoDialog.selected = null
                                    $('.pic-box-selected').removeClass('pic-box-selected')

                                })

                                $('#pic-list').delegate('a', 'click', function() {
                                    var embed = []
                                    embed.push('<video width="480" controls autoplay>')
                                    embed.push('<source src="'+$(this).data("path")+'" type="video/mp4"/>')
                                    embed.push('</video>')
                                    var html = embed.join('')
                                    layer.open({
                                        type: 1,
                                        title: false,
                                        shadeClose: true,
                                        area: 'auto',
                                        maxWidth: 1024,
                                        content: html
                                    });

                                })


                                $('#form-local').ajaxForm(function(data) {
                                    if(data.length == 0) {
                                        $('#pic-list').html('<div style="text-align: center; line-height: 40px;">暂无数据</div>')
                                    } else {
                                        $('#pic-list').html(videoDialog._data2HTML(data, $('#query_name2').val())).data('page', data.length < rows ? -1 : 1).data('scroll', 0).scrollTop(0).
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
                                    'fileSizeLimit' : '50MB',
                                    'uploadLimit'  : 20,
                                    'queueSizeLimit' : 20,
                                    'fileType'     : 'video',
                                    'buttonClass': 'btn-upload-class',
                                    'uploadScript'     : '/upload/file?u_folder=site/'+videoDialog.options.webId+'&webId='+videoDialog.options.webId+'&u_categoryId=3',
                                    'onUploadComplete' : function(file, data) {
                                        data = JSON.parse(data)
                                        for (var i = 0; i< data.length; i++) {
                                            var pic = data[i]

                                            var $picList = $('#pic-list')

                                            $picList.prepend(videoDialog._data2HTML(data))
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

                                    if(videoDialog.options.multiSelect) {   //多选
                                        
                                        if ($.inArray(src, videoDialog.selected) > -1) { //取消选择
                                            videoDialog.selected.splice($.inArray(src, videoDialog.selected),1);
                                             $(this).find('.pic-box-s').removeClass('pic-box-selected')   
                                        } else { //添加选择
                                            if(videoDialog.selected.length >= videoDialog.options.multiMaxCount) {
                                                layer.msg('图片数量不能超过'+videoDialog.options.multiMaxCount+'！', {icon: 2})
                                                return
                                            }

                                            videoDialog.selected.push(src)
                                            $(this).find('.pic-box-s').addClass('pic-box-selected')   
                                        }

                                        $('#pic-box-selected-count').text(videoDialog.selected.length)

                                    } else {
                                        $('.pic-box-selected').removeClass('pic-box-selected')
                                        $(this).find('.pic-box-s').addClass('pic-box-selected')

                                        if ($.inArray(src, videoDialog.selected) === -1) {
                                            videoDialog.selected = [src]
                                        }
                                    }

                                    console.log(videoDialog.selected)
                                })


                            }
                         });  
                    })
               
            })
        }
    }


    $.fn.VideoDialog = function(options) {
        var args = arguments;
        var value;
        var chain = this.each(function() {
            var data = $(this).data("videoDialog");
            if (!data) {
                if (options && typeof options == 'object') { //初始化
                    return $(this).data("videoDialog", data = new VideoDialog(this, options));
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

    $.fn.VideoDialog.defaults = {

    };

})(jQuery);