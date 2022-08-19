package cn.violin.home.book.task;

import cn.violin.home.book.entity.BlogInfo;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Getter
@Setter
public class FileExportTask implements Runnable{

    private BlogInfo blogVo;

    private String workspace;

    private Object lock;

    /**
     * コンストラクタ
     * @param blogVo blogVo
     * @param workspace workspace
     * @param lock lock
     */
    public FileExportTask(BlogInfo blogVo, Object lock, String workspace) {
        this.blogVo = blogVo;
        this.workspace = workspace;
        this.lock = lock;
    }

    @Override
    public void run() {
        File folder = new File(this.workspace + this.blogVo.getBtId());
        synchronized (this.lock) {
            if (!folder.exists()) {
                folder.mkdirs();
            }
        }
        BufferedWriter writer;
        try {
            String filePath = this.workspace + this.blogVo.getBtId() + File.separator + this.blogVo.getBid() + ".md";
            File file = new File(filePath);
            writer = new BufferedWriter(new FileWriter(filePath + "_bk"));
            writer.write(new String(this.blogVo.getContent().getData(), StandardCharsets.UTF_8));
            writer.close();

            if (file.exists()) {
                file.delete();
            }
            File newFile = new File(filePath + "_bk");
            newFile.renameTo(file);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
