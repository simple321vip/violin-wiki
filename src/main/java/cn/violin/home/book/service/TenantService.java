package cn.violin.home.book.service;

import cn.violin.home.book.entity.Tenant;
import cn.violin.home.book.utils.RedisUtils;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * nothing
 */
@Service
@Slf4j
public class TenantService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RedisUtils redis;


    /**
     * this method is to query the third party login user is legal and exists.
     * to return a Optional object to controller that will set redirect uri to front side.
     * @param tenant the third party BAIDUで登録するユーザー
     * @return Optional
     */
    public Optional<Tenant> checkAndUpdate(Tenant tenant, String token) {

        Criteria criteria = Criteria.where("id").is(tenant.getId());
        Query query = Query.query(criteria);

        Tenant tenantEntity = mongoTemplate.findOne(query, Tenant.class);
        log.info("tenantEntity:", tenantEntity);
        if (tenantEntity != null) {
            redis.set(tenantEntity.getId(), token, 1, TimeUnit.DAYS);
        }
        return Optional.ofNullable(tenantEntity);
    }

    /**
     * use token to select tenant information from the third party of baidu
     * @param token token
     * @return the tenant information
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
     * query token from t_tenant
     * @param token token
     * @return the result of query result
     */
    public Tenant getTenantFromTTenant(String token) {

        String id = "3272499474";
        System.out.println(token);

        Criteria criteria = Criteria.where("id").is(id);

        Query query = Query.query(criteria);

        return mongoTemplate.findOne(query, Tenant.class);
    }

    /**
     * to delete token for tenant id.
     *
     * @param id tenant id
     * @return status logout status
     */
    public boolean reToken(String id) {
        // System.out.println(redis.get(id));
        return redis.delete(id);
    }

}
