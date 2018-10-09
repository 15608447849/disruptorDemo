[["java:package:bottle.transfer"]]
module dts{

    const int SliceMax = 1024;

    struct FileUploadRequest
    {
        string filePath;
        string fileName;
        long fileSize;
    };

    sequence<byte> DataBytes;//数据

    /** 传输序列 */
    struct TransferSequence
    {
        long tag;
        long sPoint;//起点
        long size;//数据段大小
        DataBytes data;//数据
    };

    sequence<TransferSequence> FileSlice;


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
        /** 请求文件上传,获取文件上传标识,文件片段序列,失败返回空序列
        *  文件路径(null则默认),文件名(null则默认),文件大小(必须大于0)
        */
        FileSlice requestFileUpload(FileUploadRequest request);

        /** 上传已经填充的文件数据片段 成功返回0 ,失败返回-1*/
        int uploadSequence(TransferSequence ts);

        /** 上传完成 */
        void uploadComplete(long tag);
    };

};
