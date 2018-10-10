package bottle.transfer.imps;

import bottle.transfer.dts.TransferSequence;
import bottle.transfer.interfaces.FileUp;
import bottle.transfer.interfaces.IFileUpload;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

public class FileUploadDefault implements IFileUpload {

    private static final long DATA_SLICE = 512;

    private FileUp currentInvalid;


    private FileUp getCacheFileUp(){
        FileUp fileUp;
        if (currentInvalid == null){
            //如果缓存为空-创建
            fileUp = new FileUp();
        }else{
            fileUp = currentInvalid;
            //如果缓存不为空 把当前缓存返回 ,移动到下一个
            currentInvalid = currentInvalid.next;
        }
        return fileUp;
    }

    private void putCacheFileUp(FileUp fileUp){
        if (fileUp == null) return;

        if (currentInvalid == null){
            //如果当前不存在缓存
            currentInvalid = fileUp;
        }else{
            //移动到最后一个缓存
            if (currentInvalid.next == null){
                currentInvalid.next = fileUp;
            }else{
                currentInvalid = currentInvalid.next;
                putCacheFileUp(fileUp);
            }

        }
    }


    @Override
    public TransferSequence[] calculationFileSlice(long fileSize) {
        ArrayList<TransferSequence> list = new ArrayList<>();
        try{
            long max = fileSize / DATA_SLICE;

            for (long i = 0 ; i < max ;i++ ){
                list.add(new TransferSequence(i * DATA_SLICE,DATA_SLICE));
            }
            long remainder = fileSize % DATA_SLICE;
            if (remainder > 0) list.add(new TransferSequence(max*DATA_SLICE,remainder));

        }catch (Exception e){
            e.printStackTrace();
        }
        TransferSequence[] array = new TransferSequence[list.size()];
        return list.toArray(array);
    }

    @Override
    public String createEmptyFileAndBindTag(@NotNull File directory, @NotNull String path, @NotNull String name, long size, @NotNull HashMap<String, FileUp> map) {

        try {
            //创建文件夹目录
            StringBuffer sb = new StringBuffer(directory.getCanonicalPath());
            if (path.indexOf("/")==0) sb.append("/");
            sb.append(path);
            if (path.lastIndexOf("/") == 0) sb.append("/");
            directory = new File(sb.toString());
            if (!directory.exists()){
                directory.mkdirs();
            }
            //创建文件
            File file = new File(directory,name);

            //绑定
            if (!map.containsKey(file.toString())){
                FileUp fileUp = getCacheFileUp();
                fileUp.file = file;
                fileUp.raf = new RandomAccessFile(fileUp.file,"rw");
                fileUp.raf.setLength(size);
                fileUp.time = System.currentTimeMillis();
                map.put(fileUp.file.toString(),fileUp);
                System.out.println("文件准备上传:"+file+",绑定成功");
            }

            System.out.println("文件准备上传:"+file+",执行完成");
            return file.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }



    @Override
    public void watchFileComplete(@NotNull HashMap<String, FileUp> map, long time) {
        Iterator<Map.Entry<String,FileUp>> it = map.entrySet().iterator();
        FileUp fileUp;
        while (it.hasNext()){
            fileUp = it.next().getValue();

            if ((System.currentTimeMillis() - fileUp.time) >  time){
                System.out.println("上传文件完成:"+fileUp.file);
               it.remove();
                try{
                    fileUp.raf.close();
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    fileUp.raf = null;
                    fileUp.file = null;
                    try {
                        putCacheFileUp(fileUp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public boolean saveFileSliceData(String tag, long start, @NotNull byte[] data, @NotNull HashMap<String, FileUp> map) {
        try{
            FileUp fileUp = map.get(tag);
            if (fileUp != null) {
                fileUp.time = System.currentTimeMillis();
                fileUp.raf.seek(start);
                fileUp.raf.write(data);
                return true;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void settingComplete(String tag, @NotNull HashMap<String, FileUp> map,@NotNull Object lock) {
        try{
            FileUp fileUp = map.get(tag);
            if (fileUp != null) {
                fileUp.time = -1;
            }
            synchronized (lock){
                lock.notify();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
