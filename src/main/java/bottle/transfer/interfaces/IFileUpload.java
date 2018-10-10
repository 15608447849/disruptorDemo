package bottle.transfer.interfaces;

import bottle.transfer.dts.TransferSequence;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.List;

public interface IFileUpload {



    /**
     * 根据文件大小计算文件分片数据
     */
    TransferSequence[] calculationFileSlice(long fileSize);

    /**
     * 根据 指定文件目录,文件名,文件大小 创建空文件,并存入文件上传任务map,绑定文件随机存储对象,返回文件标识
     */
    String createEmptyFileAndBindTag(@NotNull File directory,@NotNull String path, @NotNull String name, long size, @NotNull HashMap<String,FileUp> map);


    /**
     * 监听文件上传是否完成
     */
    void watchFileComplete(@NotNull HashMap<String,FileUp> map,long time);

    /**
     * 保存文件片段数据
     */
    boolean saveFileSliceData(String tag,long start,@NotNull byte[] data,@NotNull HashMap<String,FileUp> map);

    /**
     * 文件上传完成
     */
    void settingComplete(String tag,@NotNull HashMap<String,FileUp> map,@NotNull Object lock);


}
