package alex.com.gdxdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.badoo.mobile.util.WeakHandler;

import alex.com.gdxdemo.animation.AnimationFragment;
import alex.com.gdxdemo.box2d.Tools.GiftParticleContants;
import butterknife.Bind;
import butterknife.ButterKnife;

public class AnimationActivity extends FragmentActivity implements AndroidFragmentApplication.Callbacks{

    private AnimationFragment m_AnimationFgm;
    private WeakHandler m_weakHandler = new WeakHandler();
    private SystemReceiveBroadCast m_systemreceiveBroadCast;

	@Bind(R.id.lyt_container)
	public FrameLayout m_container;

	public static void launch(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, AnimationActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation_activity);

        ButterKnife.bind(this);

	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        m_systemreceiveBroadCast = new SystemReceiveBroadCast();
        IntentFilter filter1 = new IntentFilter();
        filter1.setPriority(800);
        filter1.addAction(GiftParticleContants.BROADCAST_GIFTPARTICLE_BACKKEY);
        registerReceiver(m_systemreceiveBroadCast, filter1);

	    m_AnimationFgm = new AnimationFragment();
	    getSupportFragmentManager().beginTransaction().add(R.id.lyt_container, m_AnimationFgm).commit();
	    showBox2dFgmFullScreen();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(m_systemreceiveBroadCast);
        ButterKnife.unbind(this);
    }

    @Override
    public void exit() {

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

    public class SystemReceiveBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d(MainActivity.class.getSimpleName(), "SystemReceiveBroadCast[^^^^^^^]play Particle Receive: " + intent.getAction());
            if (intent.getAction().equals(GiftParticleContants.BROADCAST_GIFTPARTICLE_BACKKEY)) {
                checkquit();
            }
        }
    }

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		showBox2dFgmFullScreen();
	}

	private void showBox2dFgmFullScreen(){
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)m_container.getLayoutParams();
		params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
		params.height = RelativeLayout.LayoutParams.MATCH_PARENT;
		m_container.setLayoutParams(params);
	}

	@Override
	public void finish() {
		m_AnimationFgm.preDestory();
		super.finish();
	}
}
