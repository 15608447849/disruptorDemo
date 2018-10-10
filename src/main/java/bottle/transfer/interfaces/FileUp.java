package bottle.transfer.interfaces;

import java.io.File;
import java.io.RandomAccessFile;

public class FileUp {
    public long time;
    public File file;
    public RandomAccessFile raf;
    public FileUp next;
}
