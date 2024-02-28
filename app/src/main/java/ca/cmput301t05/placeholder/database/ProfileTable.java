package ca.cmput301t05.placeholder.database;

import android.content.Context;

public class ProfileTable extends Table{
    private DeviceIDManager idManager;

    public ProfileTable(Context context) {
        super(context);
        idManager = new DeviceIDManager(context);
    }

    public boolean deviceHasProfile(){
        if(idManager.deviceHasIDStored()){
            return true;
        }

        return false;
    }

    public boolean profileExistsOnDatabase(String deviceId){
        return false;
    }
}
