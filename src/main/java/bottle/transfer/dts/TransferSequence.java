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
 * 传输序列
 **/
public class TransferSequence implements java.lang.Cloneable, java.io.Serializable
{
    public long tag;

    public long sPoint;

    public long size;

    public byte[] data;

    public TransferSequence()
    {
    }

    public TransferSequence(long tag, long sPoint, long size, byte[] data)
    {
        this.tag = tag;
        this.sPoint = sPoint;
        this.size = size;
        this.data = data;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        TransferSequence _r = null;
        if(rhs instanceof TransferSequence)
        {
            _r = (TransferSequence)rhs;
        }

        if(_r != null)
        {
            if(tag != _r.tag)
            {
                return false;
            }
            if(sPoint != _r.sPoint)
            {
                return false;
            }
            if(size != _r.size)
            {
                return false;
            }
            if(!java.util.Arrays.equals(data, _r.data))
            {
                return false;
            }

            return true;
        }

        return false;
    }

    public int
    hashCode()
    {
        int __h = 5381;
        __h = IceInternal.HashUtil.hashAdd(__h, "::dts::TransferSequence");
        __h = IceInternal.HashUtil.hashAdd(__h, tag);
        __h = IceInternal.HashUtil.hashAdd(__h, sPoint);
        __h = IceInternal.HashUtil.hashAdd(__h, size);
        __h = IceInternal.HashUtil.hashAdd(__h, data);
        return __h;
    }

    public TransferSequence
    clone()
    {
        TransferSequence c = null;
        try
        {
            c = (TransferSequence)super.clone();
        }
        catch(CloneNotSupportedException ex)
        {
            assert false; // impossible
        }
        return c;
    }

    public void
    __write(IceInternal.BasicStream __os)
    {
        __os.writeLong(tag);
        __os.writeLong(sPoint);
        __os.writeLong(size);
        DataBytesHelper.write(__os, data);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        tag = __is.readLong();
        sPoint = __is.readLong();
        size = __is.readLong();
        data = DataBytesHelper.read(__is);
    }

    static public void
    __write(IceInternal.BasicStream __os, TransferSequence __v)
    {
        if(__v == null)
        {
            __nullMarshalValue.__write(__os);
        }
        else
        {
            __v.__write(__os);
        }
    }

    static public TransferSequence
    __read(IceInternal.BasicStream __is, TransferSequence __v)
    {
        if(__v == null)
        {
             __v = new TransferSequence();
        }
        __v.__read(__is);
        return __v;
    }
    
    private static final TransferSequence __nullMarshalValue = new TransferSequence();

    public static final long serialVersionUID = 213510212L;
}
