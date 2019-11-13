package com.zr.eswebcollector.collector;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import org.jsoup.select.Elements;

public class HupuDataCollector extends BreadthCrawler {

    public HupuDataCollector(String crawlPath, boolean autoParse) {
        super(crawlPath, autoParse);
        /*start pages*/
        this.addSeed("https://soccer.hupu.com/g/players/", "index");
        setThreads(50);
        getConf().setTopN(100);
    }

    @Override
    public void visit(Page page, CrawlDatums next) {
        if (page.matchType("index")) {
            next.add(page.links("div.site_right li a"), "list");
        } else if (page.matchType("list")) {
            next.add(page.links("ul.player_name li a"), "detail");
        } else if (page.matchType("detail")) {
            Elements mainInfo = page.select("ul.player_detail");
            String cnName = mainInfo.select("li.center b").eq(0).text();
            String enName = mainInfo.select("li.center b").eq(1).text();
            String nation = mainInfo.select("li.center span").eq(0).text();
            //1985-01-07 | 25 岁
            String birth = mainInfo.select("li.center span").eq(1).text().split(" ")[0];
            String age = mainInfo.select("li.center span").eq(1).text().split(" ")[2];
            //未知    or  65kg
            String weight = mainInfo.select("li.center span").eq(2).text().replaceAll("kg","");
            String height = mainInfo.select("li").eq(2).select("span").eq(0).text().replaceAll("cm", "");
            String teamName = mainInfo.select("li").eq(2).select("span").eq(1).text();
            String position = mainInfo.select("li").eq(2).select("span").eq(2).text();
            String number = mainInfo.select("li").eq(2).select("span").eq(3).text();
            mainInfo.select("li").eq(2).select("span").eq(1).text();
            System.out.println(cnName + ":" + enName);
            return;
        }
    }

    public static void main(String[] args) {
        HupuDataCollector dataCollector = new HupuDataCollector("hupuData", true);
        // param: depth
        try {
            dataCollector.start(3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Out");
        //数据入库


    }
}
