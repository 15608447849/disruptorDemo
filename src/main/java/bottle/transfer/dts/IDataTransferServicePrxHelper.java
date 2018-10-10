// **********************************************************************
//
// Copyright (c) 2003-2016 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************
//
// Ice version 3.6.3
//
// <auto-generated>
//
// Generated from file `DataTransferService.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package bottle.transfer.dts;

/**
 * Provides type-specific helper functions.
 **/
public final class IDataTransferServicePrxHelper extends Ice.ObjectPrxHelperBase implements IDataTransferServicePrx
{
    private static final String __complete_name = "complete";

    public void complete(String tag)
    {
        complete(tag, null, false);
    }

    public void complete(String tag, java.util.Map<String, String> __ctx)
    {
        complete(tag, __ctx, true);
    }

    private void complete(String tag, java.util.Map<String, String> __ctx, boolean __explicitCtx)
    {
        end_complete(begin_complete(tag, __ctx, __explicitCtx, true, null));
    }

    public Ice.AsyncResult begin_complete(String tag)
    {
        return begin_complete(tag, null, false, false, null);
    }

    public Ice.AsyncResult begin_complete(String tag, java.util.Map<String, String> __ctx)
    {
        return begin_complete(tag, __ctx, true, false, null);
    }

    public Ice.AsyncResult begin_complete(String tag, Ice.Callback __cb)
    {
        return begin_complete(tag, null, false, false, __cb);
    }

    public Ice.AsyncResult begin_complete(String tag, java.util.Map<String, String> __ctx, Ice.Callback __cb)
    {
        return begin_complete(tag, __ctx, true, false, __cb);
    }

    public Ice.AsyncResult begin_complete(String tag, Callback_IDataTransferService_complete __cb)
    {
        return begin_complete(tag, null, false, false, __cb);
    }

    public Ice.AsyncResult begin_complete(String tag, java.util.Map<String, String> __ctx, Callback_IDataTransferService_complete __cb)
    {
        return begin_complete(tag, __ctx, true, false, __cb);
    }

    public Ice.AsyncResult begin_complete(String tag, 
                                          IceInternal.Functional_VoidCallback __responseCb, 
                                          IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb)
    {
        return begin_complete(tag, null, false, false, __responseCb, __exceptionCb, null);
    }

    public Ice.AsyncResult begin_complete(String tag, 
                                          IceInternal.Functional_VoidCallback __responseCb, 
                                          IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb, 
                                          IceInternal.Functional_BoolCallback __sentCb)
    {
        return begin_complete(tag, null, false, false, __responseCb, __exceptionCb, __sentCb);
    }

    public Ice.AsyncResult begin_complete(String tag, 
                                          java.util.Map<String, String> __ctx, 
                                          IceInternal.Functional_VoidCallback __responseCb, 
                                          IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb)
    {
        return begin_complete(tag, __ctx, true, false, __responseCb, __exceptionCb, null);
    }

    public Ice.AsyncResult begin_complete(String tag, 
                                          java.util.Map<String, String> __ctx, 
                                          IceInternal.Functional_VoidCallback __responseCb, 
                                          IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb, 
                                          IceInternal.Functional_BoolCallback __sentCb)
    {
        return begin_complete(tag, __ctx, true, false, __responseCb, __exceptionCb, __sentCb);
    }

    private Ice.AsyncResult begin_complete(String tag, 
                                           java.util.Map<String, String> __ctx, 
                                           boolean __explicitCtx, 
                                           boolean __synchronous, 
                                           IceInternal.Functional_VoidCallback __responseCb, 
                                           IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb, 
                                           IceInternal.Functional_BoolCallback __sentCb)
    {
        return begin_complete(tag, 
                              __ctx, 
                              __explicitCtx, 
                              __synchronous, 
                              new IceInternal.Functional_OnewayCallback(__responseCb, __exceptionCb, __sentCb));
    }

    private Ice.AsyncResult begin_complete(String tag, 
                                           java.util.Map<String, String> __ctx, 
                                           boolean __explicitCtx, 
                                           boolean __synchronous, 
                                           IceInternal.CallbackBase __cb)
    {
        IceInternal.OutgoingAsync __result = getOutgoingAsync(__complete_name, __cb);
        try
        {
            __result.prepare(__complete_name, Ice.OperationMode.Normal, __ctx, __explicitCtx, __synchronous);
            IceInternal.BasicStream __os = __result.startWriteParams(Ice.FormatType.DefaultFormat);
            __os.writeString(tag);
            __result.endWriteParams();
            __result.invoke();
        }
        catch(Ice.Exception __ex)
        {
            __result.abort(__ex);
        }
        return __result;
    }

    public void end_complete(Ice.AsyncResult __iresult)
    {
        __end(__iresult, __complete_name);
    }

    private static final String __request_name = "request";

    public FileUploadRespond request(FileUploadRequest request)
    {
        return request(request, null, false);
    }

    public FileUploadRespond request(FileUploadRequest request, java.util.Map<String, String> __ctx)
    {
        return request(request, __ctx, true);
    }

    private FileUploadRespond request(FileUploadRequest request, java.util.Map<String, String> __ctx, boolean __explicitCtx)
    {
        __checkTwowayOnly(__request_name);
        return end_request(begin_request(request, __ctx, __explicitCtx, true, null));
    }

    public Ice.AsyncResult begin_request(FileUploadRequest request)
    {
        return begin_request(request, null, false, false, null);
    }

    public Ice.AsyncResult begin_request(FileUploadRequest request, java.util.Map<String, String> __ctx)
    {
        return begin_request(request, __ctx, true, false, null);
    }

    public Ice.AsyncResult begin_request(FileUploadRequest request, Ice.Callback __cb)
    {
        return begin_request(request, null, false, false, __cb);
    }

    public Ice.AsyncResult begin_request(FileUploadRequest request, java.util.Map<String, String> __ctx, Ice.Callback __cb)
    {
        return begin_request(request, __ctx, true, false, __cb);
    }

    public Ice.AsyncResult begin_request(FileUploadRequest request, Callback_IDataTransferService_request __cb)
    {
        return begin_request(request, null, false, false, __cb);
    }

    public Ice.AsyncResult begin_request(FileUploadRequest request, java.util.Map<String, String> __ctx, Callback_IDataTransferService_request __cb)
    {
        return begin_request(request, __ctx, true, false, __cb);
    }

    public Ice.AsyncResult begin_request(FileUploadRequest request, 
                                         IceInternal.Functional_GenericCallback1<FileUploadRespond> __responseCb, 
                                         IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb)
    {
        return begin_request(request, null, false, false, __responseCb, __exceptionCb, null);
    }

    public Ice.AsyncResult begin_request(FileUploadRequest request, 
                                         IceInternal.Functional_GenericCallback1<FileUploadRespond> __responseCb, 
                                         IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb, 
                                         IceInternal.Functional_BoolCallback __sentCb)
    {
        return begin_request(request, null, false, false, __responseCb, __exceptionCb, __sentCb);
    }

    public Ice.AsyncResult begin_request(FileUploadRequest request, 
                                         java.util.Map<String, String> __ctx, 
                                         IceInternal.Functional_GenericCallback1<FileUploadRespond> __responseCb, 
                                         IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb)
    {
        return begin_request(request, __ctx, true, false, __responseCb, __exceptionCb, null);
    }

    public Ice.AsyncResult begin_request(FileUploadRequest request, 
                                         java.util.Map<String, String> __ctx, 
                                         IceInternal.Functional_GenericCallback1<FileUploadRespond> __responseCb, 
                                         IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb, 
                                         IceInternal.Functional_BoolCallback __sentCb)
    {
        return begin_request(request, __ctx, true, false, __responseCb, __exceptionCb, __sentCb);
    }

    private Ice.AsyncResult begin_request(FileUploadRequest request, 
                                          java.util.Map<String, String> __ctx, 
                                          boolean __explicitCtx, 
                                          boolean __synchronous, 
                                          IceInternal.Functional_GenericCallback1<FileUploadRespond> __responseCb, 
                                          IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb, 
                                          IceInternal.Functional_BoolCallback __sentCb)
    {
        return begin_request(request, __ctx, __explicitCtx, __synchronous, 
                             new IceInternal.Functional_TwowayCallbackArg1<bottle.transfer.dts.FileUploadRespond>(__responseCb, __exceptionCb, __sentCb)
                                 {
                                     public final void __completed(Ice.AsyncResult __result)
                                     {
                                         IDataTransferServicePrxHelper.__request_completed(this, __result);
                                     }
                                 });
    }

    private Ice.AsyncResult begin_request(FileUploadRequest request, 
                                          java.util.Map<String, String> __ctx, 
                                          boolean __explicitCtx, 
                                          boolean __synchronous, 
                                          IceInternal.CallbackBase __cb)
    {
        __checkAsyncTwowayOnly(__request_name);
        IceInternal.OutgoingAsync __result = getOutgoingAsync(__request_name, __cb);
        try
        {
            __result.prepare(__request_name, Ice.OperationMode.Normal, __ctx, __explicitCtx, __synchronous);
            IceInternal.BasicStream __os = __result.startWriteParams(Ice.FormatType.DefaultFormat);
            FileUploadRequest.__write(__os, request);
            __result.endWriteParams();
            __result.invoke();
        }
        catch(Ice.Exception __ex)
        {
            __result.abort(__ex);
        }
        return __result;
    }

    public FileUploadRespond end_request(Ice.AsyncResult __iresult)
    {
        IceInternal.OutgoingAsync __result = IceInternal.OutgoingAsync.check(__iresult, this, __request_name);
        try
        {
            if(!__result.__wait())
            {
                try
                {
                    __result.throwUserException();
                }
                catch(Ice.UserException __ex)
                {
                    throw new Ice.UnknownUserException(__ex.ice_name(), __ex);
                }
            }
            IceInternal.BasicStream __is = __result.startReadParams();
            FileUploadRespond __ret = null;
            __ret = FileUploadRespond.__read(__is, __ret);
            __result.endReadParams();
            return __ret;
        }
        finally
        {
            if(__result != null)
            {
                __result.cacheMessageBuffers();
            }
        }
    }

    static public void __request_completed(Ice.TwowayCallbackArg1<FileUploadRespond> __cb, Ice.AsyncResult __result)
    {
        bottle.transfer.dts.IDataTransferServicePrx __proxy = (bottle.transfer.dts.IDataTransferServicePrx)__result.getProxy();
        FileUploadRespond __ret = null;
        try
        {
            __ret = __proxy.end_request(__result);
        }
        catch(Ice.LocalException __ex)
        {
            __cb.exception(__ex);
            return;
        }
        catch(Ice.SystemException __ex)
        {
            __cb.exception(__ex);
            return;
        }
        __cb.response(__ret);
    }

    private static final String __transfer_name = "transfer";

    public void transfer(String tag, TransferSequence ts, byte[] data)
    {
        transfer(tag, ts, data, null, false);
    }

    public void transfer(String tag, TransferSequence ts, byte[] data, java.util.Map<String, String> __ctx)
    {
        transfer(tag, ts, data, __ctx, true);
    }

    private void transfer(String tag, TransferSequence ts, byte[] data, java.util.Map<String, String> __ctx, boolean __explicitCtx)
    {
        end_transfer(begin_transfer(tag, ts, data, __ctx, __explicitCtx, true, null));
    }

    public Ice.AsyncResult begin_transfer(String tag, TransferSequence ts, byte[] data)
    {
        return begin_transfer(tag, ts, data, null, false, false, null);
    }

    public Ice.AsyncResult begin_transfer(String tag, TransferSequence ts, byte[] data, java.util.Map<String, String> __ctx)
    {
        return begin_transfer(tag, ts, data, __ctx, true, false, null);
    }

    public Ice.AsyncResult begin_transfer(String tag, TransferSequence ts, byte[] data, Ice.Callback __cb)
    {
        return begin_transfer(tag, ts, data, null, false, false, __cb);
    }

    public Ice.AsyncResult begin_transfer(String tag, TransferSequence ts, byte[] data, java.util.Map<String, String> __ctx, Ice.Callback __cb)
    {
        return begin_transfer(tag, ts, data, __ctx, true, false, __cb);
    }

    public Ice.AsyncResult begin_transfer(String tag, TransferSequence ts, byte[] data, Callback_IDataTransferService_transfer __cb)
    {
        return begin_transfer(tag, ts, data, null, false, false, __cb);
    }

    public Ice.AsyncResult begin_transfer(String tag, TransferSequence ts, byte[] data, java.util.Map<String, String> __ctx, Callback_IDataTransferService_transfer __cb)
    {
        return begin_transfer(tag, ts, data, __ctx, true, false, __cb);
    }

    public Ice.AsyncResult begin_transfer(String tag, 
                                          TransferSequence ts, 
                                          byte[] data, 
                                          IceInternal.Functional_VoidCallback __responseCb, 
                                          IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb)
    {
        return begin_transfer(tag, ts, data, null, false, false, __responseCb, __exceptionCb, null);
    }

    public Ice.AsyncResult begin_transfer(String tag, 
                                          TransferSequence ts, 
                                          byte[] data, 
                                          IceInternal.Functional_VoidCallback __responseCb, 
                                          IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb, 
                                          IceInternal.Functional_BoolCallback __sentCb)
    {
        return begin_transfer(tag, ts, data, null, false, false, __responseCb, __exceptionCb, __sentCb);
    }

    public Ice.AsyncResult begin_transfer(String tag, 
                                          TransferSequence ts, 
                                          byte[] data, 
                                          java.util.Map<String, String> __ctx, 
                                          IceInternal.Functional_VoidCallback __responseCb, 
                                          IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb)
    {
        return begin_transfer(tag, ts, data, __ctx, true, false, __responseCb, __exceptionCb, null);
    }

    public Ice.AsyncResult begin_transfer(String tag, 
                                          TransferSequence ts, 
                                          byte[] data, 
                                          java.util.Map<String, String> __ctx, 
                                          IceInternal.Functional_VoidCallback __responseCb, 
                                          IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb, 
                                          IceInternal.Functional_BoolCallback __sentCb)
    {
        return begin_transfer(tag, ts, data, __ctx, true, false, __responseCb, __exceptionCb, __sentCb);
    }

    private Ice.AsyncResult begin_transfer(String tag, 
                                           TransferSequence ts, 
                                           byte[] data, 
                                           java.util.Map<String, String> __ctx, 
                                           boolean __explicitCtx, 
                                           boolean __synchronous, 
                                           IceInternal.Functional_VoidCallback __responseCb, 
                                           IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb, 
                                           IceInternal.Functional_BoolCallback __sentCb)
    {
        return begin_transfer(tag, 
                              ts, 
                              data, 
                              __ctx, 
                              __explicitCtx, 
                              __synchronous, 
                              new IceInternal.Functional_OnewayCallback(__responseCb, __exceptionCb, __sentCb));
    }

    private Ice.AsyncResult begin_transfer(String tag, 
                                           TransferSequence ts, 
                                           byte[] data, 
                                           java.util.Map<String, String> __ctx, 
                                           boolean __explicitCtx, 
                                           boolean __synchronous, 
                                           IceInternal.CallbackBase __cb)
    {
        IceInternal.OutgoingAsync __result = getOutgoingAsync(__transfer_name, __cb);
        try
        {
            __result.prepare(__transfer_name, Ice.OperationMode.Normal, __ctx, __explicitCtx, __synchronous);
            IceInternal.BasicStream __os = __result.startWriteParams(Ice.FormatType.DefaultFormat);
            __os.writeString(tag);
            TransferSequence.__write(__os, ts);
            DataBytesHelper.write(__os, data);
            __result.endWriteParams();
            __result.invoke();
        }
        catch(Ice.Exception __ex)
        {
            __result.abort(__ex);
        }
        return __result;
    }

    public void end_transfer(Ice.AsyncResult __iresult)
    {
        __end(__iresult, __transfer_name);
    }

    /**
     * Contacts the remote server to verify that the object implements this type.
     * Raises a local exception if a communication error occurs.
     * @param __obj The untyped proxy.
     * @return A proxy for this type, or null if the object does not support this type.
     **/
    public static IDataTransferServicePrx checkedCast(Ice.ObjectPrx __obj)
    {
        return checkedCastImpl(__obj, ice_staticId(), IDataTransferServicePrx.class, IDataTransferServicePrxHelper.class);
    }

    /**
     * Contacts the remote server to verify that the object implements this type.
     * Raises a local exception if a communication error occurs.
     * @param __obj The untyped proxy.
     * @param __ctx The Context map to send with the invocation.
     * @return A proxy for this type, or null if the object does not support this type.
     **/
    public static IDataTransferServicePrx checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        return checkedCastImpl(__obj, __ctx, ice_staticId(), IDataTransferServicePrx.class, IDataTransferServicePrxHelper.class);
    }

    /**
     * Contacts the remote server to verify that a facet of the object implements this type.
     * Raises a local exception if a communication error occurs.
     * @param __obj The untyped proxy.
     * @param __facet The name of the desired facet.
     * @return A proxy for this type, or null if the object does not support this type.
     **/
    public static IDataTransferServicePrx checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        return checkedCastImpl(__obj, __facet, ice_staticId(), IDataTransferServicePrx.class, IDataTransferServicePrxHelper.class);
    }

    /**
     * Contacts the remote server to verify that a facet of the object implements this type.
     * Raises a local exception if a communication error occurs.
     * @param __obj The untyped proxy.
     * @param __facet The name of the desired facet.
     * @param __ctx The Context map to send with the invocation.
     * @return A proxy for this type, or null if the object does not support this type.
     **/
    public static IDataTransferServicePrx checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        return checkedCastImpl(__obj, __facet, __ctx, ice_staticId(), IDataTransferServicePrx.class, IDataTransferServicePrxHelper.class);
    }

    /**
     * Downcasts the given proxy to this type without contacting the remote server.
     * @param __obj The untyped proxy.
     * @return A proxy for this type.
     **/
    public static IDataTransferServicePrx uncheckedCast(Ice.ObjectPrx __obj)
    {
        return uncheckedCastImpl(__obj, IDataTransferServicePrx.class, IDataTransferServicePrxHelper.class);
    }

    /**
     * Downcasts the given proxy to this type without contacting the remote server.
     * @param __obj The untyped proxy.
     * @param __facet The name of the desired facet.
     * @return A proxy for this type.
     **/
    public static IDataTransferServicePrx uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        return uncheckedCastImpl(__obj, __facet, IDataTransferServicePrx.class, IDataTransferServicePrxHelper.class);
    }

    public static final String[] __ids =
    {
        "::Ice::Object",
        "::dts::IDataTransferService"
    };

    /**
     * Provides the Slice type ID of this type.
     * @return The Slice type ID.
     **/
    public static String ice_staticId()
    {
        return __ids[1];
    }

    public static void __write(IceInternal.BasicStream __os, IDataTransferServicePrx v)
    {
        __os.writeProxy(v);
    }

    public static IDataTransferServicePrx __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            IDataTransferServicePrxHelper result = new IDataTransferServicePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }

    public static final long serialVersionUID = 0L;
}
