package bottle.transfer.imps;

import Ice.Current;
import IceUtilInternal.StringUtil;
import bottle.transfer.dts.FileUploadRequest;
import bottle.transfer.dts.FileUploadRespond;
import bottle.transfer.dts.TransferSequence;
import bottle.transfer.dts._IDataTransferServiceDisp;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.io.File;
import java.util.concurrent.ThreadFactory;

public class FileTransferServerImp extends _IDataTransferServiceDisp {

    private static class DataCarrier{
        String tag;
        long start;
        byte[] data;
    }

    private static class DataEventHandler implements EventHandler<DataCarrier> {
        @Override
        public void onEvent(DataCarrier dataCarrier, long l, boolean b) throws Exception {
            manager.build.getFileUpload().saveFileSliceData(dataCarrier.tag,dataCarrier.start,dataCarrier.data,manager.build.getMap());
        }
    }

    private static final FileUploadManager manager;

    private static final Disruptor<DataCarrier> disruptor;

    static{
        //读取配置文件中 文件存放的目录信息 -pass
        manager = new FileUploadManager.Build()
                .setDirectory(new File("C:/upload/ping"))
                .create();


        ThreadFactory executor = r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            t.setName("PIO-"+t.getId());
            return t;
        };

        EventFactory<DataCarrier> eventFactory = DataCarrier::new;

        final int  BUFFERSIZE = 1024 * 1024;

        disruptor = new Disruptor<>(eventFactory,BUFFERSIZE,executor, ProducerType.MULTI, new YieldingWaitStrategy());
        disruptor.handleEventsWith(new DataEventHandler(),new DataEventHandler(),new DataEventHandler(),new DataEventHandler());
        disruptor.start();


    }

    private static String str(String v,String def){
        if (v == null || v.length() == 0) return def;
        return v;
    }


    @Override
    public FileUploadRespond request(FileUploadRequest request, Current __current) {
        String tag = manager.build.getFileUpload().createEmptyFileAndBindTag(
                manager.build.getDirectory(),
                str(request.path,"/default/"),
                str(request.name,System.nanoTime()+".unknown"),
                request.size,
                manager.build.getMap()
        );

        if (tag != null){
            TransferSequence[] array = manager.build.getFileUpload().calculationFileSlice(request.size);
            return new FileUploadRespond(tag,array);
        }

        return null;
    }

    @Override
    public void transfer(String tag, TransferSequence ts, byte[] data, Current __current) {
        long index = disruptor.getRingBuffer().next();
        try {
            DataCarrier dataCarrier = disruptor.getRingBuffer().get(index);
                dataCarrier.tag = tag;
                dataCarrier.start = ts.start;
                dataCarrier.data = data;
        } finally {
            disruptor.getRingBuffer().publish(index);
        }
    }

    @Override
    public void complete(String tag, Current __current) {
        manager.build.getFileUpload().settingComplete(tag,manager.build.getMap(),manager.build.getLock());
    }
}
