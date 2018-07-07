function pagination(pageId,obj) {
    var id = $(obj).parents('.cpn').attr('id')
    if (id) //specify id
        loadComponent(id, {page: pageId})
    else { //more
        layer.load(2)
        location.href = "?page="+pageId;
    }
}

function loadComponent(id, params) {
    var $component = $('#'+id)

    var index = layer.load(2)
    $.get('/web/'+webId+'/design/load/'+id, params, function (data) {
        $component.find('>:not(.cpn-operator)').remove()
        var $html  =$(data.data)
        $component.append($html)

        lazyLoad($html)

        layer.close(index)
    });
}

function lazyLoad(obj) {
    obj.find(".lazy").lazyload({
        effect : "fadeIn",
    });
}



function play(id) {
    layer.open({
        type: 1,
        title: false,
        area: 'auto',
        // closeBtn: 0,
        shadeClose: true,
        maxWidth: 1024,
        content: $('#'+id).html()
    });
}

;(function ($,w) {
   'use strict'

    var tpl = {
        init: function () {
            _init()
        }
    }

    function _init() {
        $('.drawer').drawer()

        $(".lazy").lazyload({
            effect: "fadeIn",
            threshold: 200,
        })
        new WOW().init();

        Holder.run({
            noBackgroundSize: true
        });



        //menu

        //add query event
        $('#header-container').on('click', '#btn-full-query', function(e) {
            var $keywords = $(this).prev()
            var value = $keywords.val()

            if (value) { //if has value, then execute query event
                $keywords.parents('form').submit()
                return
            }

            var style = $keywords.parent().data('style')

            if ('style1' === style) {
                var keywordsWidth = $keywords.css('width')

                $keywords.css({'width': 0, 'padding': '0 5px'}).show().animate({'width': keywordsWidth})

            } else if ('style2' === style || 'style3' === style) {
                $keywords.show('slow');
            }



        })

        $('section').on('click', function() {
            var $keywords = $('#header-container .search input')
            var style = $keywords.parent().data('style')

            if ('style1' === style) {
                if ("none" === $keywords.css('display')) return

                var keywordsWidth = $keywords.css('width')

                !$keywords.val() && $keywords.css({'padding': 0}).animate({'width': 0 }, function() {
                    $keywords.hide()
                    $(this).css({'width': keywordsWidth})
                })
            } else if ('style2' === style || 'style3' == style) {
                !$keywords.val() && $keywords.hide('slow')
            }

        })

    }
    window.tpl = tpl
})(jQuery,window)

$(function () {
    tpl.init()
    // $("body").niceScroll();
})



