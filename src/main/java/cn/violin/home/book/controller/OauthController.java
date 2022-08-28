package cn.violin.home.book.controller;

import cn.violin.home.book.annotation.PassToken;
import cn.violin.home.book.entity.Tenant;
import cn.violin.home.book.service.TenantService;
import com.alibaba.fastjson.JSONObject;
import cn.violin.home.book.config.BaiduConf;
import cn.violin.home.book.vo.UserInfoVo;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Controller
@CrossOrigin
@RequestMapping("/api/v1")
@Slf4j
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
    @PassToken
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

            log.info(accessToken);
            Tenant tenant = tenantService.getTenant(accessToken);
            log.info(tenant.toString());

            Optional<Tenant> optional = tenantService.checkAndUpdate(tenant, accessToken);

            if (optional.isPresent()) {
                resp.sendRedirect(redirect.toString());
            } else {
                resp.sendRedirect(REDIRECT_IP + "?token=");
            }

        }
    }

    @GetMapping("/user_info")
    @PassToken
    public ResponseEntity<UserInfoVo> getUInfo(@RequestParam(value = "token") String token) {

        Tenant tenant = tenantService.getTenantFromTTenant(token);

        UserInfoVo build = UserInfoVo.builder()
                .id(tenant.getTenantId())
                .account(tenant.getAccount())
                .baiduName(tenant.getAccount())
                .netdiskName(tenant.getStorageAccount())
                .avatarUrl(tenant.getAvatarUrl())
                .build();
        return new ResponseEntity<>(build, HttpStatus.OK);
    }

    @GetMapping("/logout/{id}")
    public ResponseEntity<Void> logout(@PathVariable(value = "id") String id) {

        boolean result = tenantService.reToken(id);

        if (result) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }
}
