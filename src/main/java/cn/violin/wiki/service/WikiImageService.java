package cn.violin.wiki.service;

import cn.violin.common.entity.Tenant;
import cn.violin.wiki.config.PersistentVolumeClaimConfig;
import cn.violin.wiki.dao.ProfileRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WikiImageService {

    @Autowired
    private PersistentVolumeClaimConfig persistentVolumeClaimConfig;

    @Autowired
    private ProfileRepo profileRepo;

    /**
     * wiki用画像保存処理
     *
     * @param uploadFile アップロード ファイル
     * @param tenant     テナント情報
     * @return アップロード ファイルパス
     */
    public String doUploadImage(MultipartFile uploadFile, Tenant tenant) throws Exception {

        var optional = profileRepo.findById(tenant.getTenantId());
        if (optional.isEmpty()) {
            throw new Exception("租户上下文不存在，请联系管理员。");
        }
        var profile = optional.get();

        String filePath = "";

        String namespace = profile.getName();
        File profileDir = new File(persistentVolumeClaimConfig.getNGINX_PVC() + namespace);
        if (!profileDir.isDirectory() && profileDir.mkdirs()) {
            String uploadFileName = uploadFile.getOriginalFilename();
            String saveFileName =
                    UUID.randomUUID() + uploadFileName.substring(uploadFileName.indexOf("."));
            try {
                uploadFile.transferTo(new File(profileDir, saveFileName));
                filePath = "https://www.violin-home.cn/static/" + profile.getName() + File.separator + saveFileName;
            } catch (IOException e) {

            }
        }

        return filePath;
    }

}
