package com.zr.eswebcollector.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zr.eswebcollector.collector.HupuDataCollector;
import com.zr.eswebcollector.dao.PlayerDao;
import com.zr.eswebcollector.entity.Player;
import com.zr.eswebcollector.service.PlayerService;
import com.zr.eswebcollector.util.SpringContextHolder;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.zr.eswebcollector.collector.HupuDataCollector.players;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Resource
    private RestHighLevelClient client;

    @Resource
    private PlayerDao dao;

    private static final String ES_INDEX = "es_alias";


    /**
     * 爬虫数据入库
     */
    @Override
    public void initData() {
        HupuDataCollector dataCollector = new HupuDataCollector("hupuData", true);
        // param: depth
        try {
            dataCollector.start(3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //数据入库
        for (Player player : players) {
            try {
                dao.insert(player);
            } catch (Exception e) {
            }
        }
        players.clear();
    }

    /**
     * 新增
     *
     * @param player
     * @return
     * @throws IOException
     */
    @Override
    public boolean add(Player player) throws IOException {
        String jsonString = JSONObject.toJSONString(player);
        Map map = JSONObject.parseObject(jsonString, Map.class);
        IndexRequest request = new IndexRequest(ES_INDEX).id(player.getId() + "").source(map);
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        System.out.println(JSONObject.toJSON(response));
        return false;
    }

    @Override
    public boolean importAll() throws IOException {
        List<Player> players = dao.selectAll();
        for (Player player : players) {
            add(player);
        }
        return true;
    }
}
