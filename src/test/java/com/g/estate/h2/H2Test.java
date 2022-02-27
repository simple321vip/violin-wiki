package com.g.estate.h2;

import com.g.estate.controller.LocationController;
import com.g.estate.entity.Location;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class H2Test {

    @Autowired
    private LocationController controller;

    @Before
    public void setup() {
    }

    @Test
    public void testLocaiton() {
//        Configuration conf = new Configuration();
//        conf.configure("hibernate.cfg.xml");
//        SessionFactory factory = conf.buildSessionFactory();
//        Session session = factory.openSession();
//
//        Location location = session.load(Location.class, 11);
        controller.hello();
        controller.hello();
    }


}
