package lee.ping

import bottle.transfer.imps.FileUploadServiceImps

class Lunc() {
    init {
        iceDemoService()
    }
    public fun iceDemoService() {
        val status = 0
        var ic: Ice.Communicator? = null
        try {
            // 初始化Communicator对象,args可以传一些初始化参数,如连接超时,初始化客户端连接池的数量等
            ic = Ice.Util.initialize();
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
}