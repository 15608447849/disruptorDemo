package bottle.transfer.imps;

import bottle.transfer.interfaces.FileUp;
import bottle.transfer.interfaces.IFileUpload;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class FileUploadDefault implements IFileUpload {

    private FileUp currentInvalid;
    private final int minCache = 3;
    private final int maxQueueSize = 10;
    private int index;
    private ReentrantLock lock = new ReentrantLock();

    @Override
    public FileUp getCacheFileUp(){

        try{
            lock.lock();
            FileUp fileUp;
            if (index < minCache){
                //如果缓存为空-创建
                fileUp = new FileUp();
            }else{
                fileUp = currentInvalid.next;
                currentInvalid.next = fileUp.next;
                fileUp.next = null;
                index--;
            }
            System.out.println("获取缓存:"+fileUp+" ,当前缓存数:"+index);
            return fileUp;
        }finally {
            lock.unlock();
        }
    }

    @Override
    public void putCacheFileUp(FileUp fileUp){
        try{
            lock.lock();
            if (fileUp == null) return;

            if (index == 0){
                //如果当前不存在任何缓存
                currentInvalid = fileUp;
                index++;
            } else{
                //收尾相连
                FileUp title = currentInvalid.next;
                currentInvalid.next = fileUp;
                if (title == null)
                {
                    fileUp.next = currentInvalid;
                }else{
                    fileUp.next = title;
                }

                //移动当前下标
                currentInvalid = fileUp;
                index++;
            }
            System.out.println("放入一个缓存:"+currentInvalid+ " 当前缓存数:"+index);
        }finally {
            lock.unlock();
        }

    }

    @Override
    public void watchCacheFileUp(long ideaTime) {
        try{
            lock.lock();
            if (index<minCache) return;
            FileUp next = currentInvalid.next;
            if (!next.isUse && (System.currentTimeMillis() - next.lastUseTime > ideaTime)){

                FileUp next_next = currentInvalid.next.next;

                currentInvalid.next = next_next;

                next.next = null;

                index--;

                System.out.println("清理一个缓存:"+ next + " 当前缓存数:"+index);

                watchCacheFileUp(ideaTime);
            }
        }finally {
            lock.unlock();
        }
    }

//    private static final long DATA_SLICE = 512;
//    @Override
//    public TransferSequence[] calculationFileSlice(long fileSize) {
//        ArrayList<TransferSequence> list = new ArrayList<>();
//        try{
//            long max = fileSize / DATA_SLICE;
//
//            for (long i = 0 ; i < max ;i++ ){
//                list.add(new TransferSequence(i * DATA_SLICE,DATA_SLICE));
//            }
//            long remainder = fileSize % DATA_SLICE;
//            if (remainder > 0) list.add(new TransferSequence(max*DATA_SLICE,remainder));
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        TransferSequence[] array = new TransferSequence[list.size()];
//        return list.toArray(array);
//    }

    @Override
    public  String  createEmptyFileAndBindTag(@NotNull File directory, @NotNull String path, @NotNull String name, long size, @NotNull HashMap<String, FileUp> map) {

        try {
//            System.out.println("当前正在处理任务数: "+ map.size());

            if (map.size() > maxQueueSize){
                return "wait";
            }

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
                    fileUp.isUse = true;
                map.put(fileUp.file.toString(),fileUp);
            }

            System.out.println("文件等待上传:"+file);
            return file.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void saveFileData(String tag, long start, @NotNull byte[] data, @NotNull HashMap<String, FileUp> map) {
        try{
            FileUp fileUp = map.get(tag);

            if (fileUp != null && fileUp.isUse) {
                fileUp.raf.seek(start);
                fileUp.raf.write(data);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void fileComplete(String tag, @NotNull HashMap<String, FileUp> map) {
        try{
            FileUp fileUp = map.get(tag);
            if (fileUp != null) {
                System.out.println("文件上传完成:"+fileUp.file);
                fileUp.isUse = false;
                fileUp.lastUseTime = System.currentTimeMillis();

                if (fileUp.raf!=null){
                    try{ fileUp.raf.close(); }catch (Exception ignored){ }
                    fileUp.raf = null;
                }
                fileUp.file = null;
                fileUp.next = null;
            }

            fileCompleteByClear(map);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void fileCompleteByClear(@NotNull HashMap<String, FileUp> map) {
        Iterator<Map.Entry<String,FileUp>> it = map.entrySet().iterator();
        FileUp fileUp;
        while (it.hasNext()){
            fileUp = it.next().getValue();
            if (!fileUp.isUse){
                it.remove();
                putCacheFileUp(fileUp);
            }
        }
    }


}
