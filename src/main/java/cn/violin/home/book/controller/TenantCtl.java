package cn.violin.home.book.controller;

import com.alibaba.fastjson.JSONObject;
import cn.violin.home.book.annotation.PassToken;
import cn.violin.home.book.annotation.UserLoginToken;
import cn.violin.home.book.service.TokenService;
import cn.violin.home.book.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@CrossOrigin
public class TenantCtl {

    private final TenantService tenantService;

    private final TokenService tokenService;

    @UserLoginToken
    @GetMapping("/getMessage")
    public String getMessage(){
        return "你已通过验证";
    }

}
