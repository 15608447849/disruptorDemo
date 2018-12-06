package bottle.transfer.interfaces;

import bottle.transfer.dts.TransferSequence;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.List;

public interface IFileUpload {

    /**
     * 根据 指定文件目录,文件名,文件大小 创建空文件,并存入文件上传任务map,绑定文件随机存储对象,返回文件标识
     */
    String createEmptyFileAndBindTag(@NotNull File directory,@NotNull String path, @NotNull String name, long size, @NotNull HashMap<String,FileUp> map);


    /**
     * 保存文件片段数据
     */
    void saveFileData(String tag,long start,@NotNull byte[] data,@NotNull HashMap<String,FileUp> map);

    /**
     * 文件上传完成
     */
    void fileComplete(String tag,@NotNull HashMap<String,FileUp> map);


    /**
     * 监听文件上传是否完成
     */
    void fileCompleteByClear(@NotNull HashMap<String,FileUp> map);

    /**
     * 获取缓存
     */
    FileUp getCacheFileUp();

    /**
     * 放入缓存
     */
    void putCacheFileUp(FileUp fileUp);
    /**
     * 检查文件上传缓存载体对象是否过期
     */
    void watchCacheFileUp(long ideaTime);
}
