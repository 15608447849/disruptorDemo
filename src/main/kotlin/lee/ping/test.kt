package lee.ping

import bottle.transfer.dts.FileUploadInfo
import bottle.transfer.dts.IFileUploadServicePrx
import bottle.transfer.dts.IFileUploadServicePrxHelper
import bottle.transfer.imps.FileUploadServiceImps
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

//    iceDemoService(args);
    val param = arrayOf("--Ice.MessageSizeMax=4096")
    iceDemoClient(param);
//    Lunc()
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
        val servant = FileUploadServiceImps()
        // 将Servant增加到ObjectAdapter中,并将Servant关联到ID为 fileUploadService 的Ice Object
        adapter?.add(servant, Ice.Util.stringToIdentity("fileUploadService"));
        // 激活ObjectAdapter
        adapter?.activate();
        // 让服务在退出之前,一直持续对请求的监听
        println("上传服务启动成功,ID: fileUploadService ,端口: default -p 9999");
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
//        val base = ic.stringToProxy("fileUploadService")
        val base = ic.stringToProxy("fileUploadService:tcp -h 192.168.1.120 -p 9999")
        // 通过checkCast向下转型，获取MyService接口的远程，并同时检测根据传入的名称获取服务单元是否OnlineBook的代理接口
        val proxy = IFileUploadServicePrxHelper.checkedCast(base) ?: throw Error("Invalid proxy")
        singleUp(proxy)
    }catch (e:Exception){
        e.printStackTrace()
    }finally {
        ic?.destroy();
    }
    System.exit(status);
}

fun singleUp(prxy: IFileUploadServicePrx) {
//    val dir = File("C:\\GameDownload")
    val dir = File("C:\\Users\\Administrator\\Downloads\\0")
    val list = dir.listFiles()
    val countDownLatch = CountDownLatch(list.size)
    for (i in 0 until list.size) {

        object : WorkThread(countDownLatch){
            override fun action() {
                uploadFile(prxy,File(list[i].canonicalPath))
            }
        }.start()
    }

    try {
        //调用await方法阻塞当前线程，等待子线程完成后在继续执行
        countDownLatch.await();
    } catch (e:InterruptedException ) {
        e.printStackTrace();
    }


}

fun uploadFile(proxy: IFileUploadServicePrx, file: File) {
    if (!file.exists()) return;
//    println("上传文件: $file ,文件大小:${file.length()}")
    val request = FileUploadInfo("/ping/",file.name,file.length())
    // 调用服务方法
    val tag = proxy.request(request)
    if(tag.isEmpty()) return
    if (tag == "wait"){
        Thread.sleep(10 * 1000)
        uploadFile(proxy, file)
        return
    }
    val time = System.currentTimeMillis()
    var bytes : ByteArray = ByteArray(1024)

    val  raf = RandomAccessFile(file,"r")

    var pos  = 0L
    var len = file.length();

    while (pos < len){
        val size = if (len - pos >= bytes.size) {
            bytes.size
        }else{
            (len - pos).toInt()
        }
        //移动到起点
        raf.seek(pos)
        //读取数据
        raf.read(bytes,0,size)
        //写入数据
        proxy.transfer(tag,pos,bytes)

//        println("${Thread.currentThread()} ${file} ${String.format("进度: %.2f",  pos.toDouble() /len.toDouble())}")

        //起点下移
        pos+=size
    }
    raf.close()
    proxy.complete(tag)
    println("${Thread.currentThread()} ${file} , ${len.toDouble()/1024f} / ${(System.currentTimeMillis() -time).toDouble()/1000f} = ${  (len.toDouble()/1024f) / ((System.currentTimeMillis() -time).toDouble()/1000f)  } kb/s")
}

