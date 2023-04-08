package cn.violin.wiki.controller;

import cn.violin.common.annotation.CurrentUser;
import cn.violin.common.entity.Tenant;
import cn.violin.wiki.io.ProfileIn;
import cn.violin.wiki.service.ProfileService;
import cn.violin.wiki.vo.ProfileVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/api/v1/author")
@CrossOrigin
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @PostMapping(value = "/profile", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<ProfileVo> createProfile(@Valid @RequestBody() ProfileIn input, @CurrentUser Tenant tenant) throws Exception {
        ProfileVo vo = profileService.createProfile(input, tenant);
        return new ResponseEntity<>(vo, HttpStatus.OK);
    }

    @PutMapping(value = "/profile", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<ProfileVo> updateProfile(@Valid @RequestBody() ProfileIn input, @CurrentUser Tenant tenant) {
        ProfileVo vo = profileService.updateProfile(input, tenant);
        return new ResponseEntity<>(vo, HttpStatus.OK);
    }

    @GetMapping(value = "/profile", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<ProfileVo> getProfile(@CurrentUser Tenant tenant) {

        ProfileVo vo = profileService.getProfile(tenant);
        return new ResponseEntity<>(vo, HttpStatus.OK);
    }

    @GetMapping(value = "/profile/name", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<ProfileVo> getProfileName(@CurrentUser Tenant tenant) {

        ProfileVo vo = profileService.getProfileName(tenant);
        return new ResponseEntity<>(vo, HttpStatus.OK);
    }

    @PostMapping(value = "/profile/judge", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<Integer> handleProfileName(@Valid @RequestBody() ProfileIn input, @CurrentUser Tenant tenant) {

        int flag = profileService.handleProfileName(input);
        return new ResponseEntity<>(flag, HttpStatus.OK);
    }

    @PutMapping(value = "/profile/publish", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<Void> publishProfile(@CurrentUser Tenant tenant) throws Exception {
        profileService.publishProfile(tenant);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
