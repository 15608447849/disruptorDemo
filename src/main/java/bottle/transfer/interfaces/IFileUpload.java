package bottle.transfer.interfaces;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.List;

public interface IFileUpload {

     public static class Slice{
        public long start;
        public long size;

         public Slice(long start, long size) {
             this.start = start;
             this.size = size;
         }
     }

    public static class FileUp{

         public long cTime;
         public File file;
         public RandomAccessFile raf;
         public boolean isError;
    }

    /**
     * 设置文件主存储的主目录
     * 如果不存在则创建
     * 成功返回 true
     */
    boolean settingMainDirectory(@NotNull File file);

    /**
     * 根据 指定文件目录,文件名,文件大小 创建空文件,并存入文件上传任务map,绑定文件随机存储对象,返回文件标识
     */
    long createEmptyFileAndBindTag(@NotNull String path, @NotNull String name, long size, @NotNull HashMap<Long,FileUp> map);


    /**
     * 根据文件大小计算文件分片数据
     */
    List<Slice> calculationFileSlice(long fileSize);

    /**
     * 监听文件上传是否完成
     */
    void watchUploadFileComplete(@NotNull HashMap<Long,FileUp> map,long time);


    /**
     * 保存文件片段数据
     */
    boolean saveFileSliceData(long tag,long start,@NotNull byte[] data,@NotNull HashMap<Long,FileUp> map);

    /**
     * 文件上传完成
     */
    void uploadOver(long tag,@NotNull HashMap<Long,FileUp> map);


}
