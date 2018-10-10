
import bottle.transfer.dts.FileUploadRequest
import bottle.transfer.dts.IDataTransferServicePrx
import bottle.transfer.dts.IDataTransferServicePrxHelper
import bottle.transfer.imps.FileTransferServerImp
import java.io.File
import java.io.RandomAccessFile
import java.util.concurrent.CountDownLatch

abstract class WorkThread(val lock:CountDownLatch):Thread(){
    abstract fun action();

    override fun run() {
        action();
        lock.countDown();
    }
}

fun main(args: Array<String>) {
    println("开始运行")
    val param = arrayOf("--Ice.Default.Locator=LBXTMS/Locator:tcp -h 192.168.1.120 -p 4061","--Ice.MessageSizeMax=40960")
//    iceDemoService(param);
    iceDemoClient(param);

}

fun iceDemoService(args: Array<String>) {
    val status = 0
    var ic: Ice.Communicator? = null
    try {
        // 初始化Communicator对象,args可以传一些初始化参数,如连接超时,初始化客户端连接池的数量等
        ic = Ice.Util.initialize(args);
        // 创建名为MyServiceAdapter的ObjectAdapter,使用缺省的通信协议(TCP/IP端口为10001的请求)
        val adapter = ic?.createObjectAdapterWithEndpoints("FileUploadServiceAdapter", "default -p 9999")
        // 实例化一个 服务对象(Servant)
        val servant = FileTransferServerImp()
        // 将Servant增加到ObjectAdapter中,并将Servant关联到ID为 fileUploadService 的Ice Object
        adapter?.add(servant, Ice.Util.stringToIdentity("fileUploadService"));
        // 激活ObjectAdapter
        adapter?.activate();
        // 让服务在退出之前,一直持续对请求的监听
        println("ice 上传服务启动成功");
        ic?.waitForShutdown();
    }catch (e:Exception){
        e.printStackTrace()
    }finally {
        ic?.destroy();
    }
    System.exit(status);
}

fun iceDemoClient(args: Array<String>) {
    val status = 0
    var ic: Ice.Communicator? = null
    try {
        // 初始化Communicator对象,args可以传一些初始化参数,如连接超时,初始化客户端连接池的数量等
        ic = Ice.Util.initialize(args);
        // 传入远程服务单元的名称、网络协议、IP及端口，构造一个Proxy对象
//        val base = ic.stringToProxy("fileUploadService:tcp -h 192.168.1.120 -p 9999")
        val base = ic.stringToProxy("FileUploadService")
//        val base = ic.stringToProxy("fileUploadService:default -p 9999")
        // 通过checkCast向下转型，获取MyService接口的远程，并同时检测根据传入的名称获取服务单元是否OnlineBook的代理接口
        val prxy = IDataTransferServicePrxHelper.checkedCast(base) ?: throw Error("Invalid proxy")

        singleUp(prxy)

    }catch (e:Exception){
        e.printStackTrace()
    }finally {
        ic?.destroy();
    }
    System.exit(status);
}

fun singleUp(prxy: IDataTransferServicePrx) {
    val countDownLatch = CountDownLatch(1)

        object : WorkThread(countDownLatch){
            override fun action() {
                uploadFile(prxy,File("C:\\Users\\Administrator\\Downloads\\23.jpg"))
            }
        }.start()

//    object : WorkThread(countDownLatch){
//        override fun action() {
//            uploadFile(prxy,File("C:\\Users\\Administrator\\Downloads\\android-studio-ide-173.4907809-windows.exe"))
//        }
//    }.start()

//        object : WorkThread(countDownLatch){
//            override fun action() {
//                uploadFile(prxy,File("C:\\Users\\Administrator\\Downloads\\1.jpg"))
//            }
//        }.start()
//
//        object : WorkThread(countDownLatch){
//            override fun action() {
//                uploadFile(prxy,File("C:\\Users\\Administrator\\Downloads\\Ice-3.6.3.msi"))
//            }
//        }.start()

    try {
        //调用await方法阻塞当前线程，等待子线程完成后在继续执行
        countDownLatch.await();
    } catch (e:InterruptedException ) {
        e.printStackTrace();
    }
}

fun uploadFile(prxy: IDataTransferServicePrx, file: File) {
    if (!file.exists()) return;
    println("上传文件: $file ,文件大小:${file.length()}")
    val request = FileUploadRequest("/ping/",file.name,file.length())
    // 调用服务方法
    val rt = prxy.request(request)
    val time = System.currentTimeMillis()
    var bytes : ByteArray? = null
    if (rt.array.isNotEmpty()){
        val  raf = RandomAccessFile(file,"r")
        var cSize  = 0L

        rt.array.forEach {
            if (bytes==null) bytes =  ByteArray(it.size.toInt())
            raf.seek(it.start)
            raf.read(bytes,0,it.size.toInt())
            cSize+=it.size
            val t = System.currentTimeMillis()
            prxy.transfer(rt.tag,it,bytes)
            val c = (System.currentTimeMillis() - t);
            if (c>0) println("${(cSize.toDouble() / file.length().toDouble()) * 100} 速度: ${  (it.size.toDouble()/1024f) / (c.toDouble()/1000f)  } kb/s")

        }
        raf.close()
        prxy.complete(rt.tag)
        println("上传成功: $file,时长:${((System.currentTimeMillis() - time).toDouble()/1000f)}秒,平均速度:${  (file.length().toDouble()/1024f) / ((System.currentTimeMillis() - time).toDouble()/1000f)  } kb/s")

    }

}

