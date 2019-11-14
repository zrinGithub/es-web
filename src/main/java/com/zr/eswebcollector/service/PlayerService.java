package com.zr.eswebcollector.service;

import com.zr.eswebcollector.entity.Player;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface PlayerService {
    void initData();

    boolean add(Player player) throws IOException;

    Map<String, Object> getPlayer(String id) throws IOException;

    boolean updatePlayer(Player player, String id) throws IOException;

    boolean deletePlayer(String id) throws IOException;

    boolean deleteAllPlayer() throws IOException;

    boolean importAll() throws IOException;

    List<Player> match(String field, String value) throws IOException;

    List<Player> queryByTeam(String field, String value) throws IOException;

    List<Player> queryPrefix(String field, String value) throws IOException;
}
