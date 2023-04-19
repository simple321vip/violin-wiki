package cn.violin.wiki.service;

import static cn.violin.wiki.utils.Constant.COLUMN_TENANT_ID;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.violin.common.config.PersistentVolumeClaimConfig;
import cn.violin.common.entity.Tenant;
import cn.violin.wiki.entity.Profile;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WikiImageService {

    @Autowired
    private PersistentVolumeClaimConfig persistentVolumeClaimConfig;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * wiki用画像保存処理
     * 
     * @param uploadFile アップロード ファイル
     * @param tenant テナント情報
     * @return アップロード ファイルパス
     */
    public String doUploadImage(MultipartFile uploadFile, Tenant tenant) {

        // wiki type 查询
        Query profileQuery = new Query();

        // 默认 组合ID 查询。
        profileQuery.addCriteria(Criteria.where(COLUMN_TENANT_ID).is(tenant.getTenantId()));

        Profile profile = mongoTemplate.findOne(profileQuery, Profile.class);
        String filePath = "";

        if (profile != null) {
            String namespace = profile.getName();
            File profileDir = new File(persistentVolumeClaimConfig.getNGINX_PVC() + namespace);
            if (!profileDir.isDirectory() && profileDir.mkdirs()) {
                String uploadFileName = uploadFile.getOriginalFilename();
                String saveFileName =
                    UUID.randomUUID().toString() + uploadFileName.substring(uploadFileName.indexOf("."));
                try {
                    uploadFile.transferTo(new File(profileDir, saveFileName));
                    filePath = "https://www.violin-home.cn/static/" + profile.getName() + File.separator + saveFileName;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return filePath;
    }

}
