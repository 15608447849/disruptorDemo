import bottle.transfer.dts.SliceMax
import bottle.transfer.interfaces.IFileUpload
import java.io.File
import java.io.RandomAccessFile
import java.util.HashMap



/**
 * 继承线程 - 定时检测map是否存在无效任务
 */
class FileUploadManager(var param:Param):Thread() , IFileUpload {



    data class Param(var isRun:Boolean , var interval:Long ,var directory:File,val map:HashMap<Long, IFileUpload.FileUp>)

    private val  lock = Object()

    init {
        name = "t-file-upload-manage"
        isDaemon = true
        start()
    }

    override fun run() {
        println("开始执行上传管理器监控线程")
        while (param.isRun){
            try {
                synchronized(lock){
                    lock.wait(param.interval)
                }
                watchUploadFileComplete(param.map,param.interval)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    override fun settingMainDirectory(file: File): Boolean {
        try {
            if (!file.exists()){
                file.mkdirs()
            }
            param.directory = file
            println("设置当前文件目录:$file")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    override fun calculationFileSlice(fileSize: Long): MutableList<IFileUpload.Slice> {
        val list = ArrayList<IFileUpload.Slice>()
        try {
            val number = fileSize / SliceMax.value
            for (index in 0 until number){
                list.add(IFileUpload.Slice(index*SliceMax.value, SliceMax.value.toLong()))
            }
            val remainder = fileSize % SliceMax.value
            if (remainder > 0){
                list.add(IFileUpload.Slice(number*SliceMax.value,remainder))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    override fun createEmptyFileAndBindTag(path: String, name: String, size: Long, map: HashMap<Long, IFileUpload.FileUp>): Long {

        try {

            val sb = StringBuffer(param.directory.canonicalPath)
            if (path.indexOf("/")==0) sb.append("/")
            sb.append(path)
            if (path.lastIndexOf("/") == 0) sb.append("/")
            val dir = File(sb.toString())
            if (!dir.exists()) dir.mkdirs()
            val fileUp = IFileUpload.FileUp()

            fileUp.file = File(dir,name)
            fileUp.raf = RandomAccessFile(fileUp.file,"rw")
            fileUp.raf.setLength(size)
            fileUp.cTime = System.currentTimeMillis()

            map.put(fileUp.cTime,fileUp)
            println("上传文件绑定: ${fileUp.cTime} - ${fileUp.file} ,文件大小: $size")

            return fileUp.cTime;
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return -1
    }

    override fun watchUploadFileComplete(map: HashMap<Long, IFileUpload.FileUp>, time: Long) {
        val it = map.entries.iterator()
        while (it.hasNext()){
            val fileUp = it.next().value;

            if (fileUp.isError || (System.currentTimeMillis() - fileUp.cTime) >  time){
                println("上传文件管理移除任务${fileUp.file}")
                it.remove()
                try {
                    fileUp.raf.close()
                } catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }
    }

    override fun saveFileSliceData(tag: Long, start: Long, data: ByteArray, map: HashMap<Long, IFileUpload.FileUp>): Boolean {
        val fileUp = map[tag] ?: return false
        fileUp.cTime = System.currentTimeMillis() //更新标识避免移除
        val raf = fileUp.raf
        raf.seek(start)
        raf.write(data)
        return true
    }

    override fun uploadOver(tag: Long, map: HashMap<Long, IFileUpload.FileUp>) {
        val fileUp = map[tag] ?: return
        fileUp.cTime = -1
        synchronized(lock){
            lock.notify()
        }
    }

}