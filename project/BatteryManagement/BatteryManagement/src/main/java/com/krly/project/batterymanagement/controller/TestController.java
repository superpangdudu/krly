package com.krly.project.batterymanagement.controller;

import com.krly.project.batterymanagement.db.dao.BatteryContainerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test")
public class TestController {
    @Autowired
    private BatteryContainerDao batteryContainerDao;

    @RequestMapping("/hello")
    @ResponseBody
    public String hello() {
        return "hello";
    }
}
