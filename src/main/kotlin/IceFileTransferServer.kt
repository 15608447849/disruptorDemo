import Ice.Current
import bottle.transfer.dts.FileUploadRequest
import bottle.transfer.dts.SliceMax
import bottle.transfer.dts.TransferSequence
import bottle.transfer.dts._IDataTransferServiceDisp
import bottle.transfer.interfaces.IFileUpload
import java.io.File

class IceFileTransferServer: _IDataTransferServiceDisp() {


    companion object Helper{
        val manager = FileUploadManager(FileUploadManager.Param(true, 30 * 1000L, File("C:/Upload"), HashMap()))
    }

    fun str(value:String? , def:String):String{
        if (value==null || value.isEmpty()) return def
        return value
    }

    override fun requestFileUpload(request: FileUploadRequest?, __current: Ice.Current?): Array<TransferSequence?> {

       val tag =  manager.createEmptyFileAndBindTag(
                str(request?.filePath,"/default/"),
                str(request?.fileName,System.nanoTime().toString()+".unknown"),
                request?.fileSize!!,
               manager.param.map
        )

        val list = manager.calculationFileSlice(request.fileSize)

        val result = arrayOfNulls<TransferSequence?>(list.size)

        for (index in 0 until list.size){
            result[index] = TransferSequence(tag,list[index].start,list[index].size,null)
        }
        return result
    }

    override fun uploadSequence(ts: TransferSequence?, __current: Ice.Current?): Int {
        val flag = manager.saveFileSliceData(ts?.tag!!,ts.sPoint,ts.data, manager.param.map)
        if (flag) return 0
        return -1
    }


    override fun uploadComplete(tag: Long, __current: Current?) {
        manager.uploadOver(tag, manager.param.map);
    }
}