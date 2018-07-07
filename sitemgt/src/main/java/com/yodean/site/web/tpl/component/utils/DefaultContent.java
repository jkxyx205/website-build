package com.yodean.site.web.tpl.component.utils;

import com.yodean.site.web.content.entity.Article;
import com.yodean.site.web.content.entity.Pic;
import com.yodean.site.web.content.entity.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rick on 2017/10/26.
 */
public class DefaultContent {

//    default/0.jpg
//    private static final String DEfa
    private static  final String DEFAULT_IMG__PATH = "tpl/init/";

    private static  final String DEFAULT_IMG_0 = "0.jpg";  //logo默认图
    private static  final String DEFAULT_IMG_1 = "1.jpg";
    private static  final String DEFAULT_IMG_2 = "2.jpg";
    private static  final String DEFAULT_IMG_3 = "3.jpg";
    private static  final String DEFAULT_IMG_4 = "4.jpg";
    private static  final String DEFAULT_IMG_5 = "5.jpg";
    private static  final String DEFAULT_IMG_6 = "6.jpg";

    public static Article getDefaultArticle(int aspectRatioW, int aspectRatioH) {

        return new Article("【图文】主标题", DEFAULT_IMG__PATH + ""+aspectRatioW+"x"+aspectRatioH+"/"+DEFAULT_IMG_0+"",
                "【图文】概述");
    }


    public static List<Article> getDefaultArticles(int aspectRatioW, int aspectRatioH, int num) {
        List<Article> list = new ArrayList<Article>(6);

        list.add(new Article("世界上最美的12种颜色的沙滩，你爱哪一种？", DEFAULT_IMG__PATH + ""+aspectRatioW+"x"+aspectRatioH+"/"+DEFAULT_IMG_1+"",
                "阳光+沙滩=一个海岛的标配　　阳光都是温暖的阳光　　沙滩可是不一样的沙滩啊　　12种颜色的沙滩，你爱哪一种？　　白沙滩　..."));

        list.add(new Article("旅行是对夏天最好的告白。", DEFAULT_IMG__PATH + ""+aspectRatioW+"x"+aspectRatioH+"/"+DEFAULT_IMG_2+"",
                "很多时候我们喜欢夏天　　可能是因为在学生时代　　最长的暑假总是伴随着烈日蝉鸣而来　　也许天气炎热　　但只要有风扇和西瓜　..."));

        list.add(new Article("世界上最美的12种颜色的沙滩，你爱哪一种？", DEFAULT_IMG__PATH + ""+aspectRatioW+"x"+aspectRatioH+"/"+DEFAULT_IMG_3+"",
                "人的一生，不知要走过多少桥，在桥上跨过多少山与水，欣赏过多少桥的山光水色，领略过多少桥的画意诗情。”——茅以升　　1、..."));

        list.add(new Article("世界上最美的12种颜色的沙滩，你爱哪一种？", DEFAULT_IMG__PATH + ""+aspectRatioW+"x"+aspectRatioH+"/"+DEFAULT_IMG_4+"",
                "人的一生，不知要走过多少桥，在桥上跨过多少山与水，欣赏过多少桥的山光水色，领略过多少桥的画意诗情。”——茅以升　　1、..."));

        list.add(new Article("世界上最美的12种颜色的沙滩，你爱哪一种？", DEFAULT_IMG__PATH + ""+aspectRatioW+"x"+aspectRatioH+"/"+DEFAULT_IMG_5+"",
                "人的一生，不知要走过多少桥，在桥上跨过多少山与水，欣赏过多少桥的山光水色，领略过多少桥的画意诗情。”——茅以升　　1、..."));

        list.add(new Article("世界上最美的12种颜色的沙滩，你爱哪一种？", DEFAULT_IMG__PATH + ""+aspectRatioW+"x"+aspectRatioH+"/"+DEFAULT_IMG_6+"",
                "人的一生，不知要走过多少桥，在桥上跨过多少山与水，欣赏过多少桥的山光水色，领略过多少桥的画意诗情。”——茅以升　　1、..."));

        num = list.size() < num ? list.size() : num;

        return list.subList(0, num);
    }






    public static Pic getDefaultPic(int aspectRatioW, int aspectRatioH) {
        return new Pic("【图片】主标题",DEFAULT_IMG__PATH + ""+aspectRatioW+"x"+aspectRatioH+"/"+DEFAULT_IMG_0+"", "【图片】概述");
    }

    public static List<Pic> getDefaultPics(int aspectRatioW, int aspectRatioH, int num) {
        List<Pic> list = new ArrayList<Pic>(6);

        list.add(new Pic("相遇从来不是偶然", DEFAULT_IMG__PATH + ""+aspectRatioW+"x"+aspectRatioH+"/"+DEFAULT_IMG_1+"","我希望有一个人会懂我，即使我什么都没说"));
        list.add(new Pic("太阳日记", DEFAULT_IMG__PATH + ""+aspectRatioW+"x"+aspectRatioH+"/"+DEFAULT_IMG_2+"","阳光温热，岁月静好，你若不来我岂敢老去"));
        list.add(new Pic("盛夏", DEFAULT_IMG__PATH + ""+aspectRatioW+"x"+aspectRatioH+"/"+DEFAULT_IMG_3+"","如果风口浪尖上你学不会站立，那么你的存在就占据了太多的空间。"));
        list.add(new Pic("墦索 的插画 植物", DEFAULT_IMG__PATH + ""+aspectRatioW+"x"+aspectRatioH+"/"+DEFAULT_IMG_4+"","不满怀期待，不心存猜想，不问也不索取。只是顺其自然，因为注定的事情，它必然会发生"));
        list.add(new Pic("点点", DEFAULT_IMG__PATH + ""+aspectRatioW+"x"+aspectRatioH+"/"+DEFAULT_IMG_5+"","走一步、算一步，过一天、算一天，这就是现在的我 。"));
        list.add(new Pic("相遇从来不是偶然", DEFAULT_IMG__PATH + ""+aspectRatioW+"x"+aspectRatioH+"/"+DEFAULT_IMG_6+"","用漫不经心的态度，过随遇而安的生活。"));

        num = list.size() < num ? list.size() : num;
        return list.subList(0, num);
    }


    public static Video getDefaultVideo(int aspectRatioW, int aspectRatioH) {
        return new Video("【视频】主标题", DEFAULT_IMG__PATH + ""+aspectRatioW+"x"+aspectRatioH+"/"+DEFAULT_IMG_0+"", "","【视频】概述");
    }

    public static List<Video> getDefaultVideos(int aspectRatioW, int aspectRatioH, int num) {
        List<Video> list = new ArrayList<Video>(6);

        list.add(new Video("相遇从来不是偶然", DEFAULT_IMG__PATH + ""+aspectRatioW+"x"+aspectRatioH+"/"+DEFAULT_IMG_1,"<embed src=\"http://player.video.qiyi.com/1ed90eab405e27901c417daa93c5aa6c/0/0/v_19rre1szfs.swf-albumId=819383300-tvId=819383300-isPurchase=0-cnId=5\" allowFullScreen=\"true\" quality=\"high\" width=\"480\" height=\"350\" align=\"middle\" allowScriptAccess=\"always\" type=\"application/x-shockwave-flash\"></embed>", "我希望有一个人会懂我，即使我什么都没说"));
        list.add(new Video("太阳日记", DEFAULT_IMG__PATH + ""+aspectRatioW+"x"+aspectRatioH+"/"+DEFAULT_IMG_2, "<embed src=\"https://imgcache.qq.com/tencentvideo_v1/playerv3/TPout.swf?max_age=86400&v=20161117&vid=x019908am6g&auto=0\" allowFullScreen=\"true\" quality=\"high\" width=\"480\" height=\"400\" align=\"middle\" allowScriptAccess=\"always\" type=\"application/x-shockwave-flash\"></embed>","阳光温热，岁月静好，你若不来我岂敢老去"));
        list.add(new Video("盛夏", DEFAULT_IMG__PATH + ""+aspectRatioW+"x"+aspectRatioH+"/"+DEFAULT_IMG_3, "<iframe height=498 width=510 src='http://player.youku.com/embed/XMjg4NDUxMDEzMg==' frameborder=0 'allowfullscreen'></iframe>", "如果风口浪尖上你学不会站立，那么你的存在就占据了太多的空间。"));
        list.add(new Video("墦索 的插画 植物", DEFAULT_IMG__PATH + ""+aspectRatioW+"x"+aspectRatioH+"/"+DEFAULT_IMG_4, "<embed src=\"http://player.video.qiyi.com/1ed90eab405e27901c417daa93c5aa6c/0/0/v_19rre1szfs.swf-albumId=819383300-tvId=819383300-isPurchase=0-cnId=5\" allowFullScreen=\"true\" quality=\"high\" width=\"480\" height=\"350\" align=\"middle\" allowScriptAccess=\"always\" type=\"application/x-shockwave-flash\"></embed>", "不满怀期待，不心存猜想，不问也不索取。只是顺其自然，因为注定的事情，它必然会发生"));
        list.add(new Video("点点", DEFAULT_IMG__PATH + ""+aspectRatioW+"x"+aspectRatioH+"/"+DEFAULT_IMG_5,"<embed src=\"http://player.video.qiyi.com/1ed90eab405e27901c417daa93c5aa6c/0/0/v_19rre1szfs.swf-albumId=819383300-tvId=819383300-isPurchase=0-cnId=5\" allowFullScreen=\"true\" quality=\"high\" width=\"480\" height=\"350\" align=\"middle\" allowScriptAccess=\"always\" type=\"application/x-shockwave-flash\"></embed>","走一步、算一步，过一天、算一天，这就是现在的我 。"));
        list.add(new Video("相遇从来不是偶然", DEFAULT_IMG__PATH + ""+aspectRatioW+"x"+aspectRatioH+"/"+DEFAULT_IMG_6,"<embed src=\"http://player.video.qiyi.com/1ed90eab405e27901c417daa93c5aa6c/0/0/v_19rre1szfs.swf-albumId=819383300-tvId=819383300-isPurchase=0-cnId=5\" allowFullScreen=\"true\" quality=\"high\" width=\"480\" height=\"350\" align=\"middle\" allowScriptAccess=\"always\" type=\"application/x-shockwave-flash\"></embed>","用漫不经心的态度，过随遇而安的生活。"));

        num = list.size() < num ? list.size() : num;
        return list.subList(0, num);
    }



    public static List<Pic> getDefaultCarouselPic() {
        List<Pic> list = new ArrayList<Pic>(1);
        list.add(new Pic("苏州点研智能软件", DEFAULT_IMG__PATH + "14x5/"+DEFAULT_IMG_2+"","Provide customers with the best quality solutions and technical services"));
        return list;
    }


}
