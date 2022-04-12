package com.g.estate.controller;

import com.alibaba.fastjson.JSONObject;
import com.g.estate.annotation.PassToken;
import com.g.estate.annotation.UserLoginToken;
import com.g.estate.entity.User;
import com.g.estate.io.GUserIn;
import com.g.estate.service.TokenService;
import com.g.estate.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vi")
@CrossOrigin
public class UserController {

    private final UserService userService;

    private final TokenService tokenService;

    @PostMapping("/authorize")
    @PassToken
    public JSONObject authorize(@RequestBody GUserIn userIn){
        JSONObject jsonObject = new JSONObject();
        Optional userForBase = userService.findUserById(userIn.getUserId());
        if (!userForBase.isPresent()) {
            jsonObject.put("message","登录失败,用户不存在");
        } else {
            User user1 = (User) userForBase.get();
            if (!user1.getPassword().equals(userIn.getPassword())) {
                jsonObject.put("message","登录失败,密码错误");
            } else {
                String token = tokenService.getToken(user1);
                jsonObject.put("token", token);
                jsonObject.put("user", userForBase);
            }
        }
        return jsonObject;
    }

    @UserLoginToken
    @GetMapping("/getMessage")
    public String getMessage(){
        return "你已通过验证";
    }

}
