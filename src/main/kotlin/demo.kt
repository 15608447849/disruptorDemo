import bottle.transfer.dts.FileUploadRequest
import bottle.transfer.dts.IDataTransferServicePrx
import bottle.transfer.dts.IDataTransferServicePrxHelper
import com.lmax.disruptor.*
import java.io.File
import com.lmax.disruptor.dsl.Disruptor
import com.lmax.disruptor.dsl.ProducerType
import sun.security.util.Length
import java.io.RandomAccessFile
import java.lang.String.join
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import java.util.concurrent.CountDownLatch




//定义事件
//Disruptor 进行交换的数据类型
data class Data(var path :String, var length: Long)

//定义了如何实例化定义的事件
//Disruptor 通过 EventFactory 在 RingBuffer 中预创建 Event 的实例
//一个 Event 实例实际上被用作一个“数据槽”
//发布者发布前，先从 RingBuffer 获得一个 Event 的实例，然后往 Event 实例中填充数据，之后再发布到 RingBuffer 中，之后由 Consumer 获得该 Event 实例并从中读取数据
class DataEventFactory() : EventFactory<Data> {

    override fun newInstance(): Data {
//        println("创建数据载体")
        return Data("未知文件路径",0)
    }
}
//消费者–定义事件处理的具体实现
class DataEventHandler(val id:Int) : EventHandler<Data> {
    override fun onEvent(event: Data?, sequence: Long, endOfBatch: Boolean){
        println("消费者:${id}, ${Thread.currentThread()}, sequence=${sequence}, Event=${event} endOfBatch=${endOfBatch}");
    }
}

//事件源生产者
class DataEventProducer(dir:String,val ringBuffer: RingBuffer<Data>): Thread() {
    private val files: Array<File> = File(dir).listFiles();
    override fun run(){
        for (file in files){

                val sequence = ringBuffer.next();
                try{
                    val data = ringBuffer[sequence]
                    data.path = file.canonicalPath
                    data.length = file.length()
//                    println("${sequence}, 生产数据: -  ${data}")
                }finally {
                    ringBuffer.publish(sequence)
                }
                sleep(1000)
        }
    }
}


fun demo() {
    val executor: ThreadFactory = object :ThreadFactory {
        override fun newThread(r: Runnable): Thread {
            val t = Thread(r)
            t.name = "pingThread-${t.id}"
            return t
        }
    }
    //val executor = Executors.newCachedThreadPool();
    val factory = DataEventFactory()
    val BUFFERSIZE = 1024
    val disruptor = Disruptor(factory,BUFFERSIZE,executor, ProducerType.MULTI, YieldingWaitStrategy())

    disruptor.handleEventsWith(DataEventHandler(1000),DataEventHandler(2000))
    disruptor.start()
    val ringBuffer = disruptor.ringBuffer

    DataEventProducer("C:\\Users\\Administrator\\Downloads\\disruptor-master",ringBuffer).start()
    DataEventProducer("C:\\Users\\Default",ringBuffer).start()

    val obj = Object()
    synchronized(obj){
        try {
            obj.wait()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}

fun main(args: Array<String>) {
    println("开始运行")
//    demo();
    val param = arrayOf("--Ice.MessageSizeMax=40960")
//    iceDemoService(param);
    iceDemoClient(param);
}


abstract class WorkThread(val lock:CountDownLatch):Thread(){
    abstract fun action();

    override fun run() {
        action();
        lock.countDown();
    }
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
        val servant = IceFileTransferServer()
        // 将Servant增加到ObjectAdapter中,并将Servant关联到ID为 fileUploadService 的Ice Object
        adapter?.add(servant, Ice.Util.stringToIdentity("fileUploadService"));
        // 激活ObjectAdapter
        adapter?.activate();
        // 让服务在退出之前,一直持续对请求的监听
        println("ice 启动成功");
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
        val base = ic.stringToProxy("fileUploadService:default -p 9999")
        // 通过checkCast向下转型，获取MyService接口的远程，并同时检测根据传入的名称获取服务单元是否OnlineBook的代理接口
        val prxy = IDataTransferServicePrxHelper.checkedCast(base) ?: throw Error("Invalid proxy")

        val countDownLatch = CountDownLatch(1)

//        object : WorkThread(countDownLatch){
//            override fun action() {
//                    uploadFile(prxy,File("C:\\Users\\Administrator\\Downloads\\jre-8u171-windows-x64.exe"))
//            }
//        }.start()
//
//        object : WorkThread(countDownLatch){
//            override fun action() {
//                uploadFile(prxy,File("C:\\Users\\Administrator\\Downloads\\1.jpg"))
//            }
//        }.start()

        object : WorkThread(countDownLatch){
            override fun action() {
                uploadFile(prxy,File("C:\\Users\\Administrator\\Downloads\\Ice-3.6.3.msi"))
            }
        }.start()

        try {
            //调用await方法阻塞当前线程，等待子线程完成后在继续执行
            countDownLatch.await();
        } catch (e:InterruptedException ) {
            e.printStackTrace();
        }

    }catch (e:Exception){
        e.printStackTrace()
    }finally {
        ic?.destroy();
    }
    System.exit(status);
}

fun uploadFile(prxy: IDataTransferServicePrx, file: File) {
    if (!file.exists()) return;
    println("上传文件: $file ,文件大小:${file.length()}")
    val request = FileUploadRequest("/ping/",file.name,file.length())
    // 调用服务方法
    val rt = prxy.requestFileUpload(request)
    val time = System.currentTimeMillis()
    if (rt.isNotEmpty()){
        val  raf = RandomAccessFile(file,"r")
        var cSize  = 0L
        rt.forEach {
            it.data = ByteArray(it.size.toInt())
            raf.seek(it.sPoint)
            raf.read(it.data)
            cSize+=it.size
            prxy.uploadSequence(it)
        }
        raf.close()
        prxy.uploadComplete(rt[0].tag)
        println("上传成功: $file,平均速度:${  (file.length().toDouble()/1024f) / ((System.currentTimeMillis() - time).toDouble()/1000f)  } kb/m")

    }
}
