package com.zr.eswebcollector.collector;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import com.zr.eswebcollector.dao.PlayerDao;
import com.zr.eswebcollector.entity.Player;
import com.zr.eswebcollector.util.SpringContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class HupuDataCollector extends BreadthCrawler {

    public static List<Player> players = new ArrayList<>();

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
            String weight = mainInfo.select("li.center span").eq(2).text().replaceAll("kg", "");
            String height = mainInfo.select("li").eq(2).select("span").eq(0).text().replaceAll("cm", "");
            String teamName = mainInfo.select("li").eq(2).select("span").eq(1).text();
            String position = mainInfo.select("li").eq(2).select("span").eq(2).text();
            String number = mainInfo.select("li").eq(2).select("span").eq(3).text();
            mainInfo.select("li").eq(2).select("span").eq(1).text();

            Player player = new Player();
            player.setCnName(cnName);
            player.setEnName(enName);
            player.setNation(nation);
            try {
                player.setBirth(birth.equals("未知") ? null : new SimpleDateFormat("yyyy-MM-dd").parse(birth));
            } catch (Exception e) {
            }
            player.setAge(StringUtils.isNumeric(age) ? Integer.valueOf(age) : null);
            try {
                player.setWeight(new BigDecimal(weight));
                player.setHeight(new BigDecimal(height));
            } catch (Exception e) {
            }
            player.setTeamName(teamName);
            player.setPosition(position);
            player.setNumber(StringUtils.isNumeric(number) ? Integer.valueOf(number) : null);
            players.add(player);
        }
    }

    public static void main(String[] args) throws Exception {
        HupuDataCollector dataCollector = new HupuDataCollector("hupuData", true);
        dataCollector.start(3);
    }
}
