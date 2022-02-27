package com.g.estate.controller;

import com.g.estate.dao.LocationRepo;
import com.g.estate.entity.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class LocationController {

    @Autowired
    private LocationRepo locationRepo;

    @ResponseBody
    @RequestMapping("/location")
    public List<Location> hello() {
        System.out.println("h2 test");
        return locationRepo.getLocationsByType("");
    }

    @ResponseBody
    @RequestMapping("/location/one")
    public Location getOne() {
        System.out.println("h2 test");
        return locationRepo.getLocationsById("11");
    }


}
