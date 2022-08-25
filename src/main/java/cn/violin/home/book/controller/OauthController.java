package cn.violin.home.book.controller;

import cn.violin.home.book.entity.Tenant;
import cn.violin.home.book.service.TenantService;
import com.alibaba.fastjson.JSONObject;
import cn.violin.home.book.config.BaiduConf;
import cn.violin.home.book.vo.UserInfoVo;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@CrossOrigin
@RequestMapping("/api/v1")
public class OauthController {

    @Value("${server.auth.redirect-ip}")
    public String REDIRECT_IP;

    @Autowired
    private BaiduConf BaiduConf;

    @Autowired
    private TenantService tenantService;

    @GetMapping("/getBaiDuCode")
    public ResponseEntity<String> getBaiDuCode() {

        return ResponseEntity.ok(String.valueOf(BaiduConf));
    }

    @GetMapping("/authorize/baidu")
    public void qrAuthorize(@RequestParam(value = "code") String code, HttpServletResponse resp) throws IOException {
        StringBuilder authorizeUrl = new StringBuilder();
        authorizeUrl.append(BaiduConf.getAccessToken())
                .append("?").append("grant_type=authorization_code")
                .append("&").append("code=" + code)
                .append("&").append("client_id=" + BaiduConf.getAppKey())
                .append("&").append("client_secret=").append(BaiduConf.getSecretKey())
                .append("&").append("redirect_uri=").append(BaiduConf.getRedirectUri());
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGetToken = new HttpGet(authorizeUrl.toString());
        CloseableHttpResponse response = httpClient.execute(httpGetToken);
        if (response.getStatusLine().getStatusCode() == 200) {

            StringBuilder redirect = new StringBuilder();
            redirect.append(REDIRECT_IP + "?token=");
            HttpEntity entity = response.getEntity();
            JSONObject object = JSONObject.parseObject(EntityUtils.toString(entity));
            String accessToken = object.getString("access_token");
            redirect.append(accessToken);
            System.out.println(accessToken);
            Tenant tenant = tenantService.getTenant(accessToken);
            tenantService.checkAndUpdate(tenant);
            resp.sendRedirect(redirect.toString());
        }
    }

    @GetMapping("/user_info")
    public ResponseEntity<UserInfoVo> getUInfo(@RequestParam(value = "token") String token) throws IOException {
        Tenant tenant = tenantService.getTenantFromTTenant(token);
        UserInfoVo build = UserInfoVo.builder()
                .uk(tenant.getId())
                .baiduName(tenant.getAccount())
                .netdiskName(tenant.getStorageAccount())
                .avatarUrl(tenant.getAvatarUrl())
                .build();
        return new ResponseEntity<>(build, HttpStatus.OK);
    }
}
