package cn.violin.wiki.service;

import cn.violin.wiki.ViolinWikiApplication;
import cn.violin.common.entity.Tenant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest( classes = ViolinWikiApplication.class)
public class BlogViewServiceTest {

    @Autowired
    private BlogViewService blogViewService;

    @Test
    public void Test_publishAll() throws Exception {

        Tenant tenant = new Tenant();
        tenant.setTenantId("3272499474");
        blogViewService.publishAll(tenant);
    }

}
