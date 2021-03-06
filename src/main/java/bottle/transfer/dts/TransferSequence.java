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
// Generated from file `FileTransferService.ice'
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
    public String tag;

    public long start;

    public byte[] bytes;

    public TransferSequence()
    {
        tag = "";
    }

    public TransferSequence(String tag, long start, byte[] bytes)
    {
        this.tag = tag;
        this.start = start;
        this.bytes = bytes;
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
                if(tag == null || _r.tag == null || !tag.equals(_r.tag))
                {
                    return false;
                }
            }
            if(start != _r.start)
            {
                return false;
            }
            if(!java.util.Arrays.equals(bytes, _r.bytes))
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
        __h = IceInternal.HashUtil.hashAdd(__h, start);
        __h = IceInternal.HashUtil.hashAdd(__h, bytes);
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
        __os.writeString(tag);
        __os.writeLong(start);
        DataBytesHelper.write(__os, bytes);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        tag = __is.readString();
        start = __is.readLong();
        bytes = DataBytesHelper.read(__is);
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

    public static final long serialVersionUID = 722407088L;
}
