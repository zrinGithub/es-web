package com.zr.eswebcollector.dao;

import com.zr.eswebcollector.entity.Player;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PlayerDao {
    int insert(Player player);
}
