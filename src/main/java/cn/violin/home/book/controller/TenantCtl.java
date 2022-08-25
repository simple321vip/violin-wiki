package cn.violin.home.book.controller;

import com.alibaba.fastjson.JSONObject;
import cn.violin.home.book.annotation.PassToken;
import cn.violin.home.book.annotation.UserLoginToken;
import cn.violin.home.book.entity.Tenant;
import cn.violin.home.book.io.GUserIn;
import cn.violin.home.book.service.TokenService;
import cn.violin.home.book.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@CrossOrigin
public class TenantCtl {

    private final TenantService tenantService;

    private final TokenService tokenService;

    @PostMapping("/authorize")
    @PassToken
    public JSONObject authorize(@RequestBody GUserIn userIn){
        JSONObject jsonObject = new JSONObject();
//        Optional userForBase = tenantService.findUserById(userIn.getUserId());
//        if (!userForBase.isPresent()) {
//            jsonObject.put("message","登录失败,用户不存在");
//        } else {
//            Tenant user = (Tenant) userForBase.get();
//            if (!user.getPassword().equals(userIn.getPassword())) {
//                jsonObject.put("message","登录失败,密码错误");
//            } else {
//                String token = tokenService.getToken(user);
//                jsonObject.put("token", token);
//                jsonObject.put("user", userForBase);
//            }
//        }
        return jsonObject;
    }

    @UserLoginToken
    @GetMapping("/getMessage")
    public String getMessage(){
        return "你已通过验证";
    }

}
