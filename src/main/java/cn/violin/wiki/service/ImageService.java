package cn.violin.wiki.service;

import cn.violin.common.config.DocsifyConf;
import cn.violin.common.entity.Tenant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
@RequiredArgsConstructor
public class ImageService {

    @Autowired
    private DocsifyConf docsifyConf;

    public String doUploadImage(MultipartFile file, String wikiId, Tenant tenant) {

//        String wikiWorkSpace = docsifyConf.getDOCSIFY_WORKSPACE() + tenant.getWikiName() + File.separator;
//        //
//        String originalFilename =file.getOriginalFilename();
//        String suffix = trueFileName .substring(trueFileName .lastIndexOf("."));
//        String fileName = trueFileName+ UUID.randomUUID()+suffix;
//
//        // 上传到又拍云
//        UpYun upYun=new UpYun("red-ghost-storage","redghost","xxxxxxxxxxxx密码");
//        String dirPath="/work/mdPics/"+articleId.toString()+"/";
//        System.out.println(dirPath);
//
//        //  为每个文章创建了一个文件夹
//        boolean mkDir = upYun.mkDir(dirPath);
//        if(mkDir){
//            System.out.println(dirPath+fileName);
//            upYun.writeFile(dirPath+fileName,file.getBytes(),false);
//            String url="http://red-ghost-storage.test.upcdn.net"+dirPath+fileName;
//            // 注意最后一定要返回上传完的url，前端的回调函数中需要使用
//            map.put("url",url);
//            return R.ok(map);
//        }
        return "";
    }

}
