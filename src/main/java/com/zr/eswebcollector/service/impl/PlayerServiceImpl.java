package com.zr.eswebcollector.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zr.eswebcollector.collector.HupuDataCollector;
import com.zr.eswebcollector.dao.PlayerDao;
import com.zr.eswebcollector.entity.Player;
import com.zr.eswebcollector.service.PlayerService;
import com.zr.eswebcollector.util.SpringContextHolder;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.zr.eswebcollector.collector.HupuDataCollector.players;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Resource
    private RestHighLevelClient client;

    @Resource
    private PlayerDao dao;

    private static final String ES_INDEX = "hupudata_v1.0";

    private static final int OFFSET = 0;

    private static final int COUNT = 1000;

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
        return true;
    }

    /**
     * 获取
     * @param id
     * @return
     * @throws IOException
     */
    @Override
    public Map<String, Object> getPlayer(String id) throws IOException {
        GetRequest getRequest = new GetRequest(ES_INDEX, id);
        GetResponse response = client.get(getRequest, RequestOptions.DEFAULT);
        return response.getSource();
    }

    /**
     * 更新
     * @param player
     * @param id
     * @return
     * @throws IOException
     */
    @Override
    public boolean updatePlayer(Player player, String id) throws IOException {
        String jsonString = JSONObject.toJSONString(player);
        Map map = JSONObject.parseObject(jsonString, Map.class);
        UpdateRequest request = new UpdateRequest(ES_INDEX, id).doc(map);
        UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
        System.out.println(JSONObject.toJSON(response));
        return true;
    }

    /**
     * 删除
     * @param id
     * @return
     * @throws IOException
     */
    @Override
    public boolean deletePlayer(String id) throws IOException {
        DeleteRequest request = new DeleteRequest(ES_INDEX, id);
        client.delete(request, RequestOptions.DEFAULT);
        return true;
    }

    /**
     * 按照查询删除
     * @return
     * @throws IOException
     */
    @Override
    public boolean deleteAllPlayer() throws IOException {
        DeleteByQueryRequest request = new DeleteByQueryRequest(ES_INDEX);
        BulkByScrollResponse response = client.deleteByQuery(request, RequestOptions.DEFAULT);
        return true;
    }


    /**
     * 添加数据到ES
     *
     * @return
     * @throws IOException
     */
    @Override
    public boolean importAll() throws IOException {
        List<Player> players = dao.selectAll();
        for (Player player : players) {
            add(player);
        }
        return true;
    }

    /**
     * text搜索
     *
     * @param field
     * @param value
     * @return
     * @throws IOException
     */
    @Override
    public List<Player> match(String field, String value) throws IOException {
        SearchRequest request = new SearchRequest(ES_INDEX);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchQuery(field, value));
        builder.from(OFFSET);
        builder.size(COUNT);

        request.source(builder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        SearchHit[] hits = response.getHits().getHits();
        List<Player> playerList = new LinkedList<>();
        for (SearchHit hit : hits) {
            Player player = JSONObject.parseObject(hit.getSourceAsString(), Player.class);
            playerList.add(player);
        }
        return playerList;
    }

    /**
     * 根据队名查询
     *
     * @param field
     * @param value
     * @return
     * @throws IOException
     */
    @Override
    public List<Player> queryByTeam(String field, String value) throws IOException {
        SearchRequest searchRequest = new SearchRequest(ES_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termQuery(field, value));
        searchSourceBuilder.from(OFFSET);
        searchSourceBuilder.size(COUNT);
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(JSONObject.toJSON(response));

        SearchHit[] hits = response.getHits().getHits();
        List<Player> playerList = new LinkedList<>();
        for (SearchHit hit : hits) {
            Player player = JSONObject.parseObject(hit.getSourceAsString(), Player.class);
            playerList.add(player);
        }

        return playerList;
    }

    /**
     * 前缀查询
     *
     * @param field
     * @param value
     * @return
     * @throws IOException
     */
    @Override
    public List<Player> queryPrefix(String field, String value) throws IOException {
        SearchRequest searchRequest = new SearchRequest(ES_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.prefixQuery(field, value));
        searchSourceBuilder.from(OFFSET);
        searchSourceBuilder.size(COUNT);
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(JSONObject.toJSON(response));

        SearchHit[] hits = response.getHits().getHits();
        List<Player> playerList = new LinkedList<>();
        for (SearchHit hit : hits) {
            Player player = JSONObject.parseObject(hit.getSourceAsString(), Player.class);
            playerList.add(player);
        }

        return playerList;
    }
}
