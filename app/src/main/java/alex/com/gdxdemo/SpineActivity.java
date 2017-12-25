package alex.com.gdxdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;

import alex.com.gdxdemo.fireworks.GiftParticleContants;
import alex.com.gdxdemo.spine.LibgdxSpineFragment;

/**
 * Created by QJoy on 2017.12.25.
 */

public class SpineActivity extends FragmentActivity implements AndroidFragmentApplication.Callbacks,
		View.OnClickListener
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

		Button jump = (Button) findViewById(R.id.jump);
	    Button walk = (Button) findViewById(R.id.walk);
	    Button run = (Button) findViewById(R.id.run);
	    jump.setOnClickListener(this);
	    walk.setOnClickListener(this);
	    run.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

	    unregisterReceiver(m_systemreceiveBroadCast);
    }

    @Override
    public void exit() {

    }

	@Override
	public void onClick(View v) {
		switch (v.getId())
		{
			case R.id.run:
				m_libgdxFgm.setAction("run");
				break;
			case R.id.walk:
				m_libgdxFgm.setAction("walk");
				break;
			case R.id.jump:
				m_libgdxFgm.setAction("jump");
				break;
		}
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
