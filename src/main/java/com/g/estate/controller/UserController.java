package com.g.estate.controller;

import com.alibaba.fastjson.JSONObject;
import com.g.estate.annotation.PassToken;
import com.g.estate.annotation.UserLoginToken;
import com.g.estate.entity.User;
import com.g.estate.service.TokenService;
import com.g.estate.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/record")
@CrossOrigin
public class UserController {

    private final UserService userService;

    private final TokenService tokenService;

    @RequestMapping("/login")
    public String login(){
        return "ddd";
    }

    @PostMapping("/authorize")
    @PassToken
    public JSONObject authorize(@RequestBody User user){
        JSONObject jsonObject = new JSONObject();
        User userForBase = userService.findByUsername(user);
        if (userForBase == null) {
            jsonObject.put("message","登录失败,用户不存在");
        } else {
            if (!userForBase.getPassword().equals(user.getPassword())) {
                jsonObject.put("message","登录失败,密码错误");
            } else {
                String token = tokenService.getToken(userForBase);
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
