package com.yodean.site.web.tpl.component.utils;

/**
 * Created by rick on 2017/11/23.
 */
public final class ComponentHTMLSnippet {
    public static final String PLACEHOLDER = "#YODEAN#";

    public static final String CLASS = "#CLASS#";

    public static final String SINGLE = "<div class=\"panel #if(!$options.marginDisplay) marginDisplayFalse #end\" data-label=\"$label\">\n" +
            "              #set($listDuration = $options.listDuration + \"s\")\n" +
            "              #set($listDelay = $options.listDelay + \"s\")\n" +
            "    #if($options.labelDisplay)\n" +
            "     <div class=\"panel-title wow $options.effect\" data-wow-duration=\"$listDuration\" data-wow-delay=\"$listDelay\">\n" +
            "              <span class=\"label\">$label</span>\n" +
            "       </div>\n" +
            "    #end\n" +
            "  <div class=\"panel-body single-line-$options.line single-col-$options.col #if(!$options.labelDisplay) paddingDisplayFalse #end "+CLASS+"\">\n" +
            "             <div class=\"wow $options.effect\" data-wow-duration=\"$listDuration\" data-wow-delay=\"$listDelay\">\n" +
            "              <div class=\"content-box\">\n" +
            "                  <div class=\"wrapper\">\n" +
            "                  "+PLACEHOLDER+"\n" +
            "                  </div>\n" +
            "              </div>\n" +
            "            </div>\n" +
            "  </div>\n" +
            "</div>";

    public static final String LIST = "<style>$options.css</style><div data-label=\"$label\" class=\"panel swiper-panel\">\n" +
            "     #set($listDuration = $options.listDuration + \"s\")\n" +
            "     #set($listDelay = $options.listDelay + \"s\")\n" +
            "     #if($options.labelDisplay)\n" +
            "     <div class=\"panel-title wow $options.effect\" data-wow-duration=\"$listDuration\" data-wow-delay=\"$listDelay\">\n" +
            "                #if(!$options.paging && $options.total > 1)\n" +
            "                <div class=\"pull-right more\">\n" +
            "                   <a href=\"/list/$options.cpnId\" target=\"_blank\"><span class=\"more-text\">查看更多</span><i class=\"iconfont icon-more1\"></i></a>\n" +
            "                </div>\n" +
            "                #end\n" +
            "                <span class=\"label\">$label</span>\n" +
            "      </div>\n" +
            "      #end\n" +
            "      <div class=\"panel-body single-line-$options.line single-col-$options.col #if(!$options.labelDisplay) paddingDisplayFalse #end "+CLASS+"\">\n" +
            "          <div class=\"swiper-container\" id=\"$time\">\n" +
            "            <div class=\"#if($options.listType == 1) swiper-wrapper #end #if($options.listType == 0) row #end\">\n" +
            "              #foreach( $c in $list )\n" +
            "                #set($listDelay = $options.listDelay + \"s\")\n" +
            "              #if($velocityCount%6 == 0) #set($theme=\"social\") #elseif($velocityCount%6 == 1) #set($theme=\"sky\") #elseif($velocityCount%6 == 2) #set($theme=\"vine\") #elseif($velocityCount%6 == 3) #set($theme=\"lava\") #elseif($velocityCount%6 == 4) #set($theme=\"gray\") #elseif($velocityCount%6 == 5) #set($theme=\"industrial\") #end" +
            "               <div data-wow-duration=\"$listDuration\" data-wow-delay=\"$listDelay\" class=\"wow $options.effect #if($options.listType == 1) swiper-slide #end #if($options.listType == 0) col-xs-6 col-sm-4 col-md-$options.mdCol #end\">\n" +
            "\n" +
//            "              <div class=\"content-box\">\n" +
//            "                  <div class=\"wrapper\">\n" +
            "                  "+PLACEHOLDER+"\n" +
//            "                  </div>\n" +
//            "              </div>\n" +
            "\n" +
            "              </div>\n" +
            "               \n" +
            "\n" +
            "            #set($options.listDelay = $options.listDelay + 0.1)\n" +
            "\n" +
            "\n" +
            "              #end\n" +
            "            </div>\n" +
            "            #if($options.listType == 1)\n" +
            "                #if($options.sliderPage)\n" +
            "                 <!-- Add Pagination -->\n" +
            "                <div class=\"swiper-pagination swiper-pagination-white\"></div>\n" +
            "                #end\n" +
            "                #if($options.sliderArrow)\n" +
            "                <!-- Add Arrows -->\n" +
            "                <div class=\"swiper-button-next swiper-button-white\"></div>\n" +
            "                <div class=\"swiper-button-prev swiper-button-white\"></div>\n" +
            "                #end\n" +
            "            #end\n" +
            "        </div>\n" +
            "        <!-- paging-->\n" +
            "         #if($options.listType == 0 && $options.paging && $options.total > 1)\n" +
            "          #set($start = 1)\n" +
            "          #set($end = $options.total)\n" +
            "          #set($range = [$start..$end])\n" +
            "             <div class=\"page-container content-box\">\n" +
            "                <ul class=\"pagination\">\n" +
            "                    #if($options.page > 1) \n" +
            "                    <li><a href=\"javascript:;\" onclick=\"pagination($options.page - 1,this)\">&laquo;</a>\n" +
            "                    #end\n" +
            "                        #foreach($i in $range)\n" +
            "                           <li #if($options.page == $i) class=\"active\" #end><a href=\"javascript:;\" onclick=\"pagination($i,this)\">$i</a></li>\n" +
            "                        #end\n" +
            "                    #if($options.page < $options.total)\n" +
            "                    <li><a href=\"javascript:;\" onclick=\"pagination($options.page+1,this)\">&raquo;</a>\n" +
            "                    #end\n" +
            "                </ul>\n" +
            "            </div>\n" +
            "              <!--共$options.records条记录-->\n" +
            "        #end         \n" +
            "        <!--paging end-->\n" +
            " #if(!$options.paging && $options.total > 1)\n" +
            "<div class=\"more-position-bottom\">\n" +
            "                  <a href=\"/list/$options.cpnId\" class=\"btn wow $options.effect\" data-wow-duration=\"$listDuration\" data-wow-delay=\"$listDelay\">查看更多</a>\n" +
            "              </div>\n" +
            "    </div>\n" +
            "  #end\n" +
            "</div>\n" +
            "#if($options.listType == 1)\n" +
            "<script>\n" +
            "$(function() {\n" +
            "  swiper_$options.cpnId = new Swiper('#$time', {\n" +
            "        pagination: {\n" +
            "            el: '#$time .swiper-pagination',\n" +
            "            clickable: true\n" +
            "        },\n" +
            "        navigation: {\n" +
            "          nextEl: '#$time .swiper-button-next',\n" +
            "          prevEl: '#$time .swiper-button-prev'\n" +
            "        },\n" +
            "        on:{\n" +
            "            slideNextTransitionEnd: function(){\n" +
            "             $(window).resize()\n" +
            "            },\n" +
            "        },\n" +
            "        #if($options.sliderAutoplay > 0)\n" +
            "        autoplay: {\n" +
            "                delay: $options.sliderAutoplay,\n" +
            "                stopOnLastSlide: false,\n" +
            "                disableOnInteraction: true,\n" +
            "            },   \n" +
            "        #end\n" +
            "        speed:$options.sliderSpeed,\n" +
            "        slidesPerColumn: $options.line,\n" +
            "        slidesPerView: $options.col, \n" +
            "        slidesPerGroup: $options.col,\n" +
            "        loopFillGroupWithBlank: true,\n" +
            "        spaceBetween: $options.sliderSpace\n" +
            "    });\n" +
            "})\n" +
            "   \n" +
            "</script>\n" +
            "#end\n" +
            "\n";
}
