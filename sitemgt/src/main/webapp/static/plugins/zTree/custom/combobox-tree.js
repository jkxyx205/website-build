/*
	author:Rick
	email:jkxyx205@163.com
*/
(function($) {
    var ComboboxTree = function(element, options) {
        this.$element = $(element);
        this.options = $.extend({},
        $.fn.comboboxTree.defaults, options); //合并参数
        this.init();
    };
	
	function writeText($tree,$input) {
		var _nodes = $tree.tree("getSelectedNode");
				var res = "";
				if(_nodes.length>0){
						for(var i=0;i<_nodes.length;i++){
							res += _nodes[i].name;
							if (i < _nodes.length-1) {
                                res +=  ",";
							}
						}
					}
		$input.val(res);
	}

    ComboboxTree.prototype = {
        constructor: ComboboxTree,
        init: function() {
			var $input = this.$element;
            var id =  this.$element[0].id;
			var treeId = id + "_zTree";
			var divId = id + "_div";
			var treeHtml = "<div id=\""+divId+"\" style=\"display: none; position: absolute; width:"+this.options.width+"px; border:1px solid #eee; border-top:none;overflow:auto; background-color:#fff;z-index: 9999;\"><ul class=\"ztree\" id=\""+treeId+"\" style=\"margin-top: 0; width: "+(this.options.width)+"px; height:"+this.options.height+"px; margin:0;padding:0;\"></ul></div>";
            $input.css({width: this.options.width, "cursor": "pointer"})
            $input.wrap('<div style="position: relative; display: inline-block;"></div>')
			$input.after(treeHtml);
            var $div = $input.next();
            $div.on('mousedown', function() {
                event.stopPropagation();    //  阻止事件冒泡
			})
			//bind event
			$input.on("click",function() {
				$(this).next().css({ left: "0", top: $(this).outerHeight()+ "px" }).slideDown("fast");
			});
			
			$('body').on("mousedown",function() {
					$div.fadeOut("fast");
			});

			//proxy tree event
			var settings = this.options.ztree.setting;
			
			if(!settings.callback) {
				this.options.ztree.setting.callback = {};
			}

			var select = this.options.ztree.setting.check == undefined ? true: false;
			if(!select) {
			    //如果开启触发，对于节点较多的树将会影响性能，因为所有被联动勾选的操作都会触发事件回调函数
				this.options.ztree.setting.check.autoCheckTrigger = false;
			}

			var eventName = (select ? "onClick" : "onCheck");

			var customMethod = this.options.ztree.setting.callback[eventName];
			
			var comboboxTree = this;
			this.options.ztree.setting.callback[eventName] = function(treeNode,treeId) {
				writeText(comboboxTree.$tree,$input);

				if (select) {//if not check，when click then close dropdown dialog
					$('body').trigger('mousedown')
				}

				if(customMethod)
					customMethod();
			}
			
			this.$tree = $("#"+treeId+"").tree(this.options.ztree);
			
			//init value
			if(this.options.ztree.values) {
				writeText(comboboxTree.$tree,$input);
			}
        },
        getTreeObject:function() {
            return this.$tree.tree('getTreeObject');
        },
		getSelectedIds:function() {
			return this.$tree.tree("getSelectedIds");
		}
    }

    $.fn.comboboxTree = function(options) {
        var args = arguments;
        var value;
        var chain = this.each(function() {
            data = $(this).data("comboboxTree");
            if (!data) {
                if (options && typeof options == 'object') { //初始化
                    return $(this).data("comboboxTree", data = new ComboboxTree(this, options));
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

    $.fn.comboboxTree.defaults = {
        width:196,
	    height:400,
		ztree: {
			async:false
		}
    };
})(jQuery);