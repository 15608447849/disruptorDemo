package cice;

import Ice.Communicator;
import Ice.Object;
import Ice.ObjectAdapter;
import IceBox.Service;

import java.util.Arrays;

public abstract class IceBoxServerAbs  implements Service {

    //服务名
    private String _serverName;
    private ObjectAdapter _adapter;

    @Override
    public void start(String name, Communicator communicator, String[] args) {
        System.out.println("准备启动服务:" + name +" 参数集:"+ Arrays.toString(args));
        //创建objectAdapter 和service同名
        _adapter = communicator.createObjectAdapter(name);
        //创建servant并激活
        Ice.Object object = specificServices(args);
        _adapter.add(object,communicator.stringToIdentity(name));
        _adapter.activate();
        System.out.println("成功启动服务:" + name);
        communicator.getLogger().print("-----------------------");
    }

    protected abstract Object specificServices(String[] args);

    @Override
    public void stop() {
        _adapter.destroy();
        System.out.println("销毁服务:" + _serverName);
    }
}
