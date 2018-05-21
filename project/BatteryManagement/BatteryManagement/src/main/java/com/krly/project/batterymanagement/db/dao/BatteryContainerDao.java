package com.krly.project.batterymanagement.db.dao;


import com.krly.project.batterymanagement.db.entity.BatteryContainer;
import com.krly.project.batterymanagement.db.mapper.BatteryContainerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BatteryContainerDao {
    @Autowired
    private BatteryContainerMapper mapper;

    public BatteryContainer get(int id) {
        return mapper.selectByPrimaryKey(id);
    }
}
