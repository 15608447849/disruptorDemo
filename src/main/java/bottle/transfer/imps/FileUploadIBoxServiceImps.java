package bottle.transfer.imps;

import Ice.Communicator;
import Ice.Logger;
import Ice.Object;
import Ice.ObjectAdapter;
import IceBox.Service;
import cice.IceBoxServerAbs;

import java.util.Arrays;

public class FileUploadIBoxServiceImps extends IceBoxServerAbs {
    @Override
    protected Object specificServices(String[] args) {
        return new FileUploadServiceImps();
    }

}
