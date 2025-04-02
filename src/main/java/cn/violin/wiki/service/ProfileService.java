package cn.violin.wiki.service;

import cn.violin.common.entity.Tenant;
import cn.violin.wiki.config.PersistentVolumeClaimConfig;
import cn.violin.wiki.dao.ProfileRepo;
import cn.violin.wiki.entity.Profile;
import cn.violin.wiki.form.ProfileForm;
import cn.violin.wiki.vo.ProfileVo;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

import static cn.violin.common.utils.CommonConstant.UPDATE_DATETIME;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class ProfileService {

    @Autowired
    private ProfileRepo profileRepo;

    @Autowired
    private PersistentVolumeClaimConfig persistentVolumeClaimConfig;

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public ProfileVo create(ProfileForm profileForm, Tenant tenant) throws Exception {

        var optional = profileRepo.findById(tenant.getTenantId());
        if (optional.isPresent()) {
            throw new Exception("创建 [Profile name = " + profileForm.getName() + "] 失败， 已经存在Profile name!");
        }

        // プロファイル　フォルダ　作成
        if (!this.buildProfileFolder(profileForm)) {
            throw new Exception("创建 [Profile name folder = " + profileForm.getName() + "] 失败， 已经存在Profile Profile name folder!");
        }

        // プロファイル　README.md　作成
        Profile profile = new Profile();
        profile.setName(profileForm.getName());
        profile.setTenantId(tenant.getTenantId());
        profile.setContent("个人主页编辑，请参照 [点击我](https://profilinator.rishav.dev/)");
        profile.setUpdateTime(LocalDateTime.now().format(UPDATE_DATETIME));

        profileRepo.save(profile);

        this.publishProfile(tenant);

        return this.read(tenant);
    }

    public ProfileVo read(Tenant tenant) throws Exception {
        var optional = profileRepo.findById(tenant.getTenantId());
        if (optional.isEmpty()) {
            throw new Exception("租户上下文不存在，请联系管理员。");
        }
        var profile = optional.get();
        return ProfileVo.builder()
                .name(profile.getName())
                .content(profile.getContent())
                .updateDatetime(profile.getUpdateTime())
                .build();
    }

    public int handleProfileName(ProfileForm profileForm) {
        var exist = profileRepo.exists(Example.of(Profile.builder().name(profileForm.getName()).build()));
        if (exist) {
            return 0;
        }
        return 1;
    }

    @Transactional
    public ProfileVo updateProfile(ProfileForm profileForm, Tenant tenant) throws Exception {
        var optional = profileRepo.findById(tenant.getTenantId());
        if (optional.isEmpty()) {
            throw new Exception("租户上下文不存在，请联系管理员。");
        }
        var profile = optional.get();
        profile.setContent(profileForm.getContent());
        profile.setUpdateTime(LocalDateTime.now().format(UPDATE_DATETIME));

        profileRepo.save(profile);

        return this.read(tenant);
    }

    /**
     * 当 租户 第一次 创建wiki主页时候，为用户创建专属的 namespace，
     *
     * @param tenant tenant
     * @throws Exception x
     */
    @Transactional(rollbackFor = Exception.class)
    public void publishProfile(Tenant tenant) throws Exception {

        var optional = profileRepo.findById(tenant.getTenantId());
        if (optional.isEmpty()) {
            throw new Exception("租户上下文不存在，请联系管理员。");
        }
        var profile = optional.get();

        String filePath = persistentVolumeClaimConfig.getDOCSIFY_PVC() + profile.getName() + File.separator + "README.md";
        File backup;
        BufferedWriter writer;
        try {
            File file = new File(filePath);

            if (!file.exists()) {
                writer = new BufferedWriter(new FileWriter(filePath));
                writer.write(profile.getContent());
                writer.close();
                return;
            }

            backup = new File(filePath + "_bk");
            if (file.renameTo(backup)) {
                writer = new BufferedWriter(new FileWriter(filePath));
                writer.write(profile.getContent());
                writer.close();
                backup.delete();
            }

        } catch (IOException e) {
            throw new Exception("发布 wiki 个人首页失败, 无法恢复原有wiki首页，请联系管理员");
        }
    }

    private boolean buildProfileFolder(ProfileForm profileForm) {
        String namespace = persistentVolumeClaimConfig.getDOCSIFY_PVC() + profileForm.getName();
        File file = new File(namespace);
        if (!file.exists()) {
            return file.mkdirs();
        }
        return false;
    }

}
