package cn.violin.home.book.service;

import cn.violin.home.book.entity.Tenant;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static cn.violin.home.book.utils.Constant.AUTHORITY_LEVEL_TWO;


/**
 *
 */
@Service
public class TenantService {

    @Autowired
    private MongoTemplate mongoTemplate;


    /**
     *
     * @param
     */
    public void checkAndUpdate(Tenant tenant) {

        Criteria criteria = Criteria.where("id").is(tenant.getId());
        Query query = Query.query(criteria);

        Tenant tenantEntity = mongoTemplate.findOne(query, Tenant.class);

        if (tenantEntity != null) {

        } else {
            tenantEntity = Tenant.builder()
                    .id(tenant.getId())
                    .account(tenant.getAccount())
                    .storageAccount(tenant.getStorageAccount())
                    .authority(AUTHORITY_LEVEL_TWO).build();
            mongoTemplate.insert(tenantEntity);
        }
    }

    /**
     *
     * @param token
     * @return
     * @throws IOException
     */
    public Tenant getTenant(String token) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String uInfoUrl = "https://pan.baidu.com/rest/2.0/xpan/nas?method=uinfo&access_token=" +
                token;
        HttpGet httpGetUInfo = new HttpGet(uInfoUrl);
        CloseableHttpResponse response = httpClient.execute(httpGetUInfo);
        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity uInfoEntity = response.getEntity();
            JSONObject uInfoObject = JSONObject.parseObject(EntityUtils.toString(uInfoEntity));
            Tenant tenant = Tenant.builder()
                    .id(uInfoObject.getString("uk"))
                    .account(uInfoObject.getString("baidu_name"))
                    .storageAccount(uInfoObject.getString("netdisk_name"))
                    .avatarUrl(uInfoObject.getString("avatar_url"))
                    .build();
            return tenant;
        } else {
            return null;
        }
    }

    /**
     *
     * @param token
     * @return
     */
    public Tenant getTenantFromTTenant(String token) {

        String id = "3272499474";

        Criteria criteria = Criteria.where("id").is(id);

        Query query = Query.query(criteria);

        Tenant tenant = mongoTemplate.findOne(query, Tenant.class);

        return tenant;
    }

}
