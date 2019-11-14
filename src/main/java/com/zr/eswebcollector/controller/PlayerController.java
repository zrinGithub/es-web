package com.zr.eswebcollector.controller;

import com.zr.eswebcollector.entity.Player;
import com.zr.eswebcollector.service.PlayerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
public class PlayerController {
    @Resource
    private PlayerService playerService;

    @GetMapping("init")
    public String initData() {
        playerService.initData();
        return "success insert players data";
    }

    @GetMapping("importAll")
    public String importAll(){
        try {
            playerService.importAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "success";
    }


}
