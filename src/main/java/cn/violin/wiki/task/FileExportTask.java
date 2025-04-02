package cn.violin.wiki.task;

import cn.violin.wiki.entity.Wiki;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Getter
@Setter
public class FileExportTask implements Runnable {

    private Wiki blogVo;

    private String workspace;

    private Object lock;

    /**
     * コンストラクタ
     *
     * @param blogVo    blogVo
     * @param workspace workspace
     * @param lock      lock
     */
    public FileExportTask(Wiki blogVo, Object lock, String workspace) {
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
            writer.write(this.blogVo.getContent());
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
