[["java:package:bottle.transfer"]]
module dts{

    /** 文件上传请求 */
    struct FileUploadInfo
    {
        string path;//远程路径
        string name;//远程文件名
        long size;//文件总大小
    };

    /** 字节数据 */
    sequence<byte> DataBytes;



    /**
    思路:
    1 客户端 通知服务器  需要上传 保存的文件的路径,文件名 ,文件大小,服务器成功创建此文件大小空间,则返回标识码
    2 服务端 开始接受文件
       使用 RandomAccessFile ,并保存对象
       绑定唯一标识 TAG - RandomAccessFile对象, 存入Map
     3 客户端 使用标识与 服务端进行数据传输
    */

    interface IFileUploadService{

         /** 请求上传文件 */
        string request(FileUploadInfo fui);

        /** 上传文件数据片段
                tag 文件远程标识
                start 起点
                bytes 字节数据
         */
        void transfer(string tag,long start,DataBytes bytes);

        /** 上传完成 */
        void complete(string tag);
    };

};
