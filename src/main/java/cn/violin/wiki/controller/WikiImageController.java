package cn.violin.wiki.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.violin.common.annotation.CurrentUser;
import cn.violin.common.entity.Tenant;
import cn.violin.wiki.service.WikiImageService;

@RequestMapping("/api/v1/author")
public class WikiImageController {

    @Autowired
    private WikiImageService wikiImageService;

    @PostMapping(value = "/image", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<String> uploadImg(MultipartFile file, @CurrentUser Tenant tenant) {

        String path = wikiImageService.doUploadImage(file, tenant);
        return new ResponseEntity<>(path, HttpStatus.OK);
    }
}
