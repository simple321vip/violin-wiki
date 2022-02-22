package com.g.estate.h2;

import com.g.estate.controller.LocationController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class H2Test {

    @Autowired
    private LocationController controller;

    @Before
    public void setup() {}

    @Test
    public void testLocaiton() {

        controller.hello();
    }

}
