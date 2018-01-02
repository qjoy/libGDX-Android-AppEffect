package alex.com.gdxdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;

import alex.com.gdxdemo.fireworks.GiftParticleContants;
import alex.com.gdxdemo.spine.LibgdxSpineFragment;
import alex.com.gdxdemo.testcode.SpringEffect;

/**
 * Created by QJoy on 2017.12.25.
 */

public class SpineActivity extends FragmentActivity implements AndroidFragmentApplication.Callbacks
{

    private LibgdxSpineFragment m_libgdxFgm;

	private SystemReceiveBroadCast m_systemreceiveBroadCast;

	public static void launch(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, SpineActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spine_activity);

	    m_systemreceiveBroadCast = new SystemReceiveBroadCast();
	    IntentFilter filter1 = new IntentFilter();
	    filter1.setPriority(800);
	    filter1.addAction(GiftParticleContants.BROADCAST_GIFTPARTICLE_BACKKEY);
	    registerReceiver(m_systemreceiveBroadCast, filter1);

        m_libgdxFgm = (LibgdxSpineFragment) getSupportFragmentManager().findFragmentById(R.id.libgdxFrag);

	    SpringEffect.doEffectSticky(findViewById(R.id.jump), new Runnable() {
		    @Override
		    public void run() {
			    m_libgdxFgm.setAction("jump");
		    }
	    });

	    SpringEffect.doEffectSticky(findViewById(R.id.walk), new Runnable() {
		    @Override
		    public void run() {
			    m_libgdxFgm.setAction("walk");
		    }
	    });

	    SpringEffect.doEffectSticky(findViewById(R.id.run), new Runnable() {
		    @Override
		    public void run() {
			    m_libgdxFgm.setAction("run");
		    }
	    });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

	    unregisterReceiver(m_systemreceiveBroadCast);
    }

    @Override
    public void exit() {

    }

	public class SystemReceiveBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d(SpineActivity.class.getSimpleName(), "SystemReceiveBroadCast[^^^^^^^]play Particle Receive: " + intent.getAction());
            if (intent.getAction().equals(GiftParticleContants.BROADCAST_GIFTPARTICLE_BACKKEY)) {
                checkquit();
            }
        }
    }

    private long m_exitTime;
    private boolean checkquit() {

        if ((System.currentTimeMillis() - m_exitTime) > 2000) {
            Toast.makeText(this, "再次点击退出", Toast.LENGTH_SHORT).show();
            m_exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
        return true;
    }

    @Override
    public void finish() {
        m_libgdxFgm.preDestory();
        super.finish();
    }

}
