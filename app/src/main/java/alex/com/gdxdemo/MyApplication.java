package alex.com.gdxdemo;

import android.app.Application;
import android.content.Context;
import android.hardware.SensorManager;

/**
 * Created by alex_xq on 15/6/18.
 */
public class MyApplication extends Application{

    static public SensorManager sm;

    private static MyApplication m_myApplication;

    public static MyApplication getInstance(){
        return m_myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        m_myApplication = this;

        sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
    }
}
