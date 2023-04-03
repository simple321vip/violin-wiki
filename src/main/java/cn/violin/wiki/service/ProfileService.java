package cn.violin.wiki.service;

import cn.violin.common.config.DocsifyConf;
import cn.violin.common.entity.Tenant;
import cn.violin.wiki.entity.Profile;
import cn.violin.wiki.io.ProfileIn;
import cn.violin.wiki.vo.ProfileVo;
import lombok.RequiredArgsConstructor;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;

import static cn.violin.common.utils.CommonConstant.*;
import static cn.violin.wiki.utils.Constant.*;

@Service
@RequiredArgsConstructor
public class ProfileService {

    @Autowired
    private MongoTemplate mongoTemplate;

    private final Object lock = new Object();

    @Autowired
    private DocsifyConf docsifyConf;

    @Transactional
    public ProfileVo createProfile(ProfileIn input, Tenant tenant) throws Exception {
        Criteria criteria = Criteria.where(COLUMN_TENANT_ID).is(tenant.getTenantId());
        Query query = Query.query(criteria);
        long count = mongoTemplate.count(query, Profile.class);

        if (count != 0) {
            throw new Exception("创建 [Profile name = " + input.getName() + "] 失败， 已经存在Profile name!");
        }

        Profile profile = new Profile();
        profile.setOwner(tenant.getTenantId());
        profile.setContent(
            new Binary("个人主页编辑，请参照 [点击我](https://profilinator.rishav.dev/)".getBytes(StandardCharsets.UTF_8)));
        profile.setUpdateDateTime(LocalDateTime.now().format(UPDATE_DATETIME));
        mongoTemplate.save(profile);

        this.publishProfile(tenant);

        return ProfileVo.builder().name(input.getName())
            .content(new String(profile.getContent().getData(), StandardCharsets.UTF_8))
            .updateDatetime(profile.getUpdateDateTime()).build();
    }

    public ProfileVo getProfile(Tenant tenant) {
        Criteria criteria = Criteria.where(COLUMN_TENANT_ID).is(tenant.getTenantId());
        Query query = Query.query(criteria);
        Profile profile = mongoTemplate.findOne(query, Profile.class);

        if (Optional.ofNullable(profile).isPresent()) {
            return ProfileVo.builder().name(profile.getName())
                .content(new String(profile.getContent().getData(), StandardCharsets.UTF_8))
                .updateDatetime(profile.getUpdateDateTime()).build();
        }

        return null;
    }

    public ProfileVo getProfileName(Tenant tenant) {
        Criteria criteria = Criteria.where(COLUMN_TENANT_ID).is(tenant.getTenantId());
        Query query = Query.query(criteria);
        query.fields().include(PROFILE_NAME);
        Profile profile = mongoTemplate.findOne(query, Profile.class);

        if (profile == null) {
            return null;
        }

        return ProfileVo.builder().content(profile.getName()).build();
    }

    public boolean handleProfileName(ProfileIn input) {
        Criteria criteria = Criteria.where(PROFILE_NAME).is(input.getName());
        Query query = Query.query(criteria);
        Profile profile = mongoTemplate.findOne(query, Profile.class);

        return profile == null;
    }

    @Transactional
    public ProfileVo updateProfile(ProfileIn input, Tenant tenant) {

        Criteria criteria = Criteria.where(COLUMN_TENANT_ID).is(tenant.getTenantId());
        Query query = Query.query(criteria);
        String updateTime = LocalDateTime.now().format(UPDATE_DATETIME);
        Update update = Update.update(COLUMN_TENANT_ID, tenant.getTenantId())
            .set(PROFILE_CONTENT, new Binary(input.getContent().getBytes(StandardCharsets.UTF_8)))
            .set(LAST_UPDATE_DATETIME, updateTime);

        mongoTemplate.updateFirst(query, update, Profile.class);

        return ProfileVo.builder().content(input.getContent()).updateDatetime(updateTime).build();
    }

    @Transactional
    public void publishProfile(Tenant tenant) throws Exception {

        Criteria criteria = Criteria.where(COLUMN_TENANT_ID).is(tenant.getTenantId());
        Query query = Query.query(criteria);
        Profile profile = mongoTemplate.findOne(query, Profile.class);
        if (profile == null) {
            throw new Exception("发布 wiki 个人首页失败， 没有找到 wiki 首页");
        }

        String filePath = docsifyConf.getDOCSIFY_WORKSPACE() + profile.getName() + File.separator + "README.md";
        File backup = null;
        BufferedWriter writer;
        try {
            File file = new File(filePath);

            if (!file.exists()&& file.renameTo(backup)) {

            }

            backup = new File(filePath + "_bk");
            if (file.exists() && file.renameTo(backup)) {
                writer = new BufferedWriter(new FileWriter(filePath));
                writer.write(new String(profile.getContent().getData(), StandardCharsets.UTF_8));
                writer.close();
            }

        } catch (IOException e) {
            if (backup.renameTo(new File(filePath))){
                throw new Exception("发布 wiki 个人首页失败, 无法恢复原有wiki首页，请联系管理员");
            } else {
                e.printStackTrace();
            }
        }
    }

}
