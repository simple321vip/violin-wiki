package cn.violin.home.book.service;

import cn.violin.home.book.ViolinBookApplication;
import cn.violin.home.book.entity.Tenant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest( classes = ViolinBookApplication.class)
public class BlogViewServiceTest {

    @Autowired
    private BlogViewService blogViewService;

    @Test
    public void Test_publishAll() {

        Tenant tenant = new Tenant();
        tenant.setTenantId("3272499474");
        blogViewService.publishAll(tenant);
    }

}
