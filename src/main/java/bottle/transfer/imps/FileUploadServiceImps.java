package bottle.transfer.imps;

import Ice.Current;
import IceUtilInternal.StringUtil;
import bottle.transfer.dts.FileUploadInfo;
import bottle.transfer.dts.TransferSequence;
import bottle.transfer.dts._IFileUploadServiceDisp;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.io.File;
import java.util.concurrent.ThreadFactory;

public class FileUploadServiceImps extends _IFileUploadServiceDisp {

    private static class DataCarrier{
        public String tag;

        public long start;

        public byte[] bytes;
    }

    private static class DataEventHandler implements EventHandler<DataCarrier> {
        @Override
        public void onEvent(DataCarrier dataCarrier, long l, boolean b) {
            manager.saveFileData(dataCarrier.tag,dataCarrier.start,dataCarrier.bytes);
        }
    }

    private static final FileUploadManager manager;

    //多线程调度队列
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

        int availProcessors = Runtime.getRuntime().availableProcessors();

        final int  BUFFERSIZE =  (int)Math.pow(2, availProcessors-1) ;

        disruptor = new Disruptor<>(eventFactory,BUFFERSIZE,executor, ProducerType.MULTI, new YieldingWaitStrategy());

        DataEventHandler[] handlerArr = new DataEventHandler[1];
        for (int i = 0 ;i<handlerArr.length ;i++) {
            handlerArr[i] = new DataEventHandler();
        }
        disruptor.handleEventsWith(handlerArr);
        disruptor.start();
    }






    @Override
    public String request(FileUploadInfo fui, Current __current) {
        return manager.createEmptyFileAndBindTag(fui.path, fui.name, fui.size);
    }

    @Override
    public void transfer(String tag, long start, byte[] bytes, Current __current) {
        long index = disruptor.getRingBuffer().next();
        try {
            DataCarrier dataCarrier = disruptor.getRingBuffer().get(index);
                dataCarrier.tag = tag;
                dataCarrier.start = start;
                dataCarrier.bytes = bytes;
        } finally {
            disruptor.getRingBuffer().publish(index);
        }
    }


    @Override
    public void complete(String tag, Current __current) {
        manager.fileComplete(tag);
    }
}
