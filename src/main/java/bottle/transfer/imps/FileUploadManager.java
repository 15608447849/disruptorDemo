package bottle.transfer.imps;

import bottle.transfer.interfaces.FileUp;
import bottle.transfer.interfaces.IFileUpload;

import java.io.File;
import java.util.HashMap;

public class FileUploadManager extends Thread {

   static class Build{
       private File directory = new File(".");
       private long loopTime = 30*1000L;
       private long idleTime = 15 * 1000L;
       private IFileUpload fileUpload = new FileUploadDefault();
       private final HashMap<String,FileUp> map = new HashMap<>();
       private final Object lock = new Object();

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

       public Object getLock() {
           return lock;
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
                synchronized (build.lock){
                        build.lock.wait(build.loopTime);
                }
                build.fileUpload.watchFileComplete(build.map,build.idleTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
