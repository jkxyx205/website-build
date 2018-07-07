(function($) {

    var Selector = function(element, options) {
        this.$element = $(element);
        this.options = $.extend({}, $.fn.selector.defaults, options); //合并参数
        this.init();
    };

    Selector.prototype = {
        constructor: Selector,
        init: function() {
            _selectColumn(this, this.options.rowNum, this.options.colNum)
            _bindEvent(this)
        }
    }

    function _bindEvent(el) {
        el.$element.on('mouseover', 'li', function() {
            var rIndex = $(this).parent().index() + 1;
            var cIndex = $(this).index() + 1
            _selectColumn(el, rIndex, cIndex)

        }).on('mouseout', 'li', function() {
            _selectColumn(el, el.options.rowNum, el.options.colNum)

        }).on('click', 'li', function() {
            var rIndex = $(this).parent().index() + 1;
            var cIndex = $(this).index() + 1
            _selectColumn(el, rIndex, cIndex)
            el.options.rowNum = rIndex;
            el.options.colNum = cIndex;

            el.options.selected && el.options.selected(rIndex, cIndex)
        })

    }

    function _selectColumn(el, rnum, cnum) {

        el.$element.find('ul').each(function(i, el) {
            if (i < rnum) {
                $(this).find('li').each(function(j, el) {
                    if (j < cnum) {
                        $(this).addClass('active')
                    } else {
                        $(this).removeClass('active')
                    }
                })
            } else {
                $(this).find('li').removeClass('active')
            }
        })
    }

    $.fn.selector = function(options) {
        var args = arguments;
        var value;
        var chain = this.each(function() {
            data = $(this).data("selector");
            if (!data) {
                if (options && typeof options == 'object') { //初始化
                    return $(this).data("selector", data = new Selector(this, options));
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

    $.fn.selector.defaults = {
        rowNum: 0,
        colNum: 0
    };
})(jQuery);