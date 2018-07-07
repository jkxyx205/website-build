//菜单
(function($){
    var $siteBodyLeft = $('#site-body-left');
    var $siteBodyRight = $('#site-body-right');
    var $expandIcon = $('#menu-expend i');
    var  $siteDetail = $('.side-bar-detail');
    var global = {
        menuExpandWidth: 130,
        menuExpand: true,
        initEvent: function() {
            $('#account').on('click', function() {
                $('#more-items').toggle()

                if(this.className.indexOf('active') > -1)
                    $(this).removeClass('active')
                else
                    $(this).addClass('active')
            })

            $('#site-container').on('click', function() {
                $('#more-items').hide()
                $('#account').removeClass('active')
            })

            $('#menu-expend').on('click', function() {
                if(!global.menuExpand) {
                    $siteBodyLeft.css("width", global.menuExpandWidth)
                    $siteBodyRight.css("width", "calc(100% - "+global.menuExpandWidth+"px)")

                    $expandIcon.removeClass('icon-expend').addClass('icon-shrinkbar')
                    $siteDetail.css("margin-left", -500)

                } else {
                    $siteBodyLeft.css("width", 50)
                    $siteBodyRight.css("width", "calc(100% - 50px)")

                    $expandIcon.removeClass('icon-shrinkbar').addClass('icon-expand')
                    $siteDetail.css("margin-left", '0')
                }

                global.menuExpand = !global.menuExpand

            })
            

        }
    }


    global.initEvent()

})(jQuery);

