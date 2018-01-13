package alex.com.gdxdemo;

import android.app.Application;
import android.content.Context;
import android.hardware.SensorManager;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Created by alex_xq on 15/6/18.
 */
public class MyApplication extends Application{

	static final private String TAG = "MyApplication";

    static public SensorManager sm;

    private static MyApplication m_myApplication;

    private static int sDens = 0;

    public static MyApplication getInstance(){
        return m_myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        m_myApplication = this;

        sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

	    DisplayMetrics dm = getResources().getDisplayMetrics();
	    sDens = dm.densityDpi;
	    Log.d(TAG, "dens:" + sDens);
    }

	public static int getDens(){
		return sDens;
	}
}
