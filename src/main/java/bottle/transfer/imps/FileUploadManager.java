package bottle.transfer.imps;

import bottle.transfer.interfaces.FileUp;
import bottle.transfer.interfaces.IFileUpload;

import java.io.File;
import java.util.HashMap;

public class FileUploadManager extends Thread {


    void saveFileData(String tag, long start, byte[] bytes) {
        build.getFileUpload().saveFileData(tag,start,bytes,build.getMap());
    }

    private static String str(String v,String def){
        if (v == null || v.length() == 0) return def;
        return v;
    }

    String createEmptyFileAndBindTag(String path, String name, long size) {
        return build.getFileUpload().createEmptyFileAndBindTag(
                build.getDirectory(),
                str(path,"/default/"),
                str(name,System.nanoTime()+".unknown"),
                size,
                build.getMap());
    }

    void fileComplete(String tag) {
        build.getFileUpload().fileComplete(tag,build.getMap());
    }

    static class Build{
       private File directory = new File(".");
       private long loopTime = 30 * 1000L;
       private long idleTime = 5 * 30 * 1000L;
       private IFileUpload fileUpload = new FileUploadDefault();
       private final HashMap<String,FileUp> map = new HashMap<>();

        Build setDirectory(File directory) {
            this.directory = directory;
            return this;
        }

        public Build setLoopTime(long loopTime) {
            this.loopTime = loopTime;
            return this;
        }

        public Build setIdleTime(long idleTime) {
            this.idleTime = idleTime;
            return this;
        }

        public Build setFileUploadImp(IFileUpload fileUploadImp){
            this.fileUpload = fileUploadImp;
            return this;
        }

       public File getDirectory() {
           return directory;
       }

       public long getLoopTime() {
           return loopTime;
       }

       public long getIdleTime() {
           return idleTime;
       }

       public IFileUpload getFileUpload() {
           return fileUpload;
       }

       public HashMap<String, FileUp> getMap() {
           return map;
       }

       public FileUploadManager  create(){
            return new FileUploadManager(this);
        }

    }

    final Build build;

    private FileUploadManager(Build build) {
        this.build = build;
        setName("t-file-upload-manage");
        setDaemon(true);
        start();
    }

    @Override
    public void run() {
        while (true){
            try {
                sleep(build.loopTime);
                build.getFileUpload().watchCacheFileUp(build.idleTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
