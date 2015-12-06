package alex.com.gdxdemo;

import android.app.Application;
import android.content.Context;
import android.hardware.SensorManager;

/**
 * Created by alex_xq on 15/6/18.
 */
public class MyApplication extends Application{

    static public SensorManager sm;

    @Override
    public void onCreate() {
        super.onCreate();

        sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
    }
}
