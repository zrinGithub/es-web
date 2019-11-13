package com.zr.eswebcollector.service.impl;

import com.zr.eswebcollector.collector.HupuDataCollector;
import com.zr.eswebcollector.dao.PlayerDao;
import com.zr.eswebcollector.entity.Player;
import com.zr.eswebcollector.service.PlayerService;
import com.zr.eswebcollector.util.SpringContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.zr.eswebcollector.collector.HupuDataCollector.players;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Resource
    private PlayerDao dao;

    @Override
    public void initData(){
        HupuDataCollector dataCollector = new HupuDataCollector("hupuData", true);
        // param: depth
        try {
            dataCollector.start(3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //数据入库
        for(Player player:players){
            try{
                dao.insert(player);
            }catch (Exception e){}
        }
        players.clear();
    }
}
