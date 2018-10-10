[["java:package:bottle.transfer"]]
module dts{
    /** 文件上传请求 */
    struct FileUploadRequest
    {
        string path;
        string name;
        long size;
    };

    /** 传输序列 */
    struct TransferSequence
    {
        long start;//起点
        long size;//数据段大小
    };

    sequence<TransferSequence> SliceArrays;//传输序列数组

    sequence<byte> DataBytes;//字节数据

    struct FileUploadRespond
    {
        string tag;//标识
        SliceArrays array; //文件传输分片信息
    };


    /**
    思路:
    1 客户端 请求服务器  -  保存的文件路径, 保存的文件名 ,文件大小
    2 服务端 开辟文件空间 如果不够 则返回失败
       使用 RandomAccessFile ,并保存对象
       绑定唯一标识 TAG - RandomAccessFile对象- 存入Map等数据结构
       返回一个 包含唯一标识 TAG的分片对象(list) - 对象包含: 文件数据片段起点,大小,数据片段Tag,数据Byte(空)
     3 客户端获取 分片对象List ,单线程上传 - 循环list 填充数据,使用RandomAccessFile  (多线程上传- 线程池+片段数据上传即可)
     4 客户端调用文件传送成功结构- 服务端关闭Raf (服务端存在间隔监听线程,定时检测关闭Raf,避免资源占用未释放)
    */

    interface IDataTransferService{
         /** 请求上传文件 */
        FileUploadRespond request(FileUploadRequest request);

        /** 上传已经填充的文件数据片段 */
        void transfer(string tag,TransferSequence ts,DataBytes data);

        /** 上传完成 */
        void complete(string tag);
    };

};
