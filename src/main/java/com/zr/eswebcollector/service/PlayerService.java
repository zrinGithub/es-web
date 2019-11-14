package com.zr.eswebcollector.service;

import com.zr.eswebcollector.entity.Player;

import java.io.IOException;

public interface PlayerService {
    void initData();

    public boolean add(Player player) throws IOException;

    boolean importAll() throws IOException;
}
