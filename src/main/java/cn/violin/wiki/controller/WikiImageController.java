package cn.violin.wiki.controller;

import cn.violin.common.annotation.CurrentUser;
import cn.violin.common.entity.Tenant;
import cn.violin.wiki.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api/v1/image")
public class WikiImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping(value = "/wiki", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<Void> uploadImg(MultipartFile file, String wikiId, @CurrentUser Tenant tenant) {

        imageService.doUploadImage(file, wikiId, tenant);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
