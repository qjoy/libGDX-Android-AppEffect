package alex.com.gdxdemo;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.badoo.mobile.util.WeakHandler;

import java.util.ArrayList;

import alex.com.gdxdemo.balloonparticlewidget.BalloonParticleContants;
import alex.com.gdxdemo.balloonparticlewidget.BalloonParticleEvents;
import alex.com.gdxdemo.balloonparticlewidget.BalloonParticleFragment;
import alex.com.gdxdemo.giftparticlewidget.GiftParticleContants;
import alex.com.gdxdemo.testcode.SpringEffect;
import de.greenrobot.event.EventBus;


public class BalloonParticleEffectActivity extends FragmentActivity implements AndroidFragmentApplication.Callbacks{

    private BalloonParticleFragment m_libgdxFgm;
    private TextView m_tvLog;
    private WeakHandler m_weakHandler = new WeakHandler();
    private ScrollView m_scrollv;
    private boolean isdestoryed = false;
	private boolean m_bOpenCrazyMode = false;
	private Button mLikeBtn;
	private Button mRandomBtn;
	private SwitchCompat mSoundSwitch;
	private BigRunnable m_bigRunnable = new BigRunnable();

	private SystemReceiveBroadCast m_systemreceiveBroadCast;

	ArrayList<float[]> mRandomColors = new ArrayList<>();


	ArrayList<String> mRandomPng = new ArrayList<>();

	public static void launch(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, BalloonParticleEffectActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balloon_activity);

	    m_systemreceiveBroadCast = new SystemReceiveBroadCast();
	    IntentFilter filter1 = new IntentFilter();
	    filter1.setPriority(800);
	    filter1.addAction(GiftParticleContants.BROADCAST_GIFTPARTICLE_BACKKEY);
	    registerReceiver(m_systemreceiveBroadCast, filter1);

		initRandomColors();

//        setAssetes();

        m_libgdxFgm = (BalloonParticleFragment) getSupportFragmentManager().findFragmentById(R.id.libgdxFrag);

        m_scrollv = (ScrollView) findViewById(R.id.scrollv);
        m_tvLog = (TextView) findViewById(R.id.log);

	    mLikeBtn = (Button) findViewById(R.id.addballoon);
	    final float[] no = {-1f,-1f,-1f};

	    SpringEffect.doEffectSticky(findViewById(R.id.addballoon), new Runnable() {
		    @Override
		    public void run() {
			    m_libgdxFgm.PlayAdd(BalloonParticleContants.BALLOON_PATHTYPE_EXTEND, "particle/balloon/1.png", 1000, no, true);
		    }
	    });

	    mRandomBtn = (Button) findViewById(R.id.random);
	    SpringEffect.doEffectSticky(findViewById(R.id.random), new Runnable() {
		    @Override
		    public void run() {
			    if (m_bOpenCrazyMode == false) {
				    m_weakHandler.postDelayed(m_bigRunnable, 1);

				    mRandomBtn.setText("close CrazyMode");
			    } else {
				    m_weakHandler.removeCallbacks(m_bigRunnable);
				    mRandomBtn.setText("open CrazyMode");
			    }

			    m_bOpenCrazyMode = !m_bOpenCrazyMode;
		    }
	    });

	    EventBus.getDefault().register(this);

	    mSoundSwitch = (SwitchCompat) findViewById(R.id.swichSound);
	    mSoundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		    @Override
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			    switch (buttonView.getId()) {
				    case R.id.swichSound: {
					    Log.i("swichSound", isChecked + "");
					    if (m_libgdxFgm != null){
						    m_libgdxFgm.switchSound(isChecked);
					    }
					    break;
				    }
			    }
		    }
	    });
    }

	private class BigRunnable implements Runnable{

		@Override
		public void run() {
			if (isdestoryed)
				return;

//			float[] randomColor = mRandomColors.get( (int)(Math.random()*(mRandomColors.size()) ) );
			float[] no = {-1f,-1f,-1f};
			m_libgdxFgm.PlayAdd(BalloonParticleContants.BALLOON_PATHTYPE_EXTEND, getHeartBalloon(), 1000, no, false);
			int dif = (int) ((float)Math.random()*200f+150f);
			m_weakHandler.postDelayed(m_bigRunnable, dif);
		}
	}

    @Override
    protected void onDestroy() {
        super.onDestroy();

	    EventBus.getDefault().unregister(this);

	    unregisterReceiver(m_systemreceiveBroadCast);
    }

    @Override
    public void exit() {

    }

    public class SystemReceiveBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d(BalloonParticleEffectActivity.class.getSimpleName(), "SystemReceiveBroadCast[^^^^^^^]play Particle Receive: " + intent.getAction());
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
        isdestoryed = true;
        m_libgdxFgm.preDestory();
        super.finish();
    }

    protected void dialog(String tip) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(tip);

        builder.setTitle("提示");

        builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {

	        @Override
	        public void onClick(DialogInterface dialog, int which) {
		        dialog.dismiss();

		        BalloonParticleEffectActivity.this.finish();
	        }
        });

        builder.create().show();
    }

//    private void setAssetes(){
//        File sd= Environment.getExternalStorageDirectory();
//        String path=sd.getPath()+"/libgdxDemo";
//        File file=new File(path);
//        if(!file.exists()) {
//            file.mkdir();
//        }
//    }

    private String getHeartBalloon(){


	    int index = (int)(Math.random() * 5 + 1);
//	    Log.d("test", "index:"+index );

        final  String externalPath = "particle/balloon/" +index+".png";

//        if (!GiftParticleEffectView.fileIsExist(externalPath))
//        {
//            dialog("图片资源文件不存在或者路径不正确，请查看测试代码:" + utils.getLineInfo());
//        }

        return externalPath;
    }

	private void initRandomColors(){
		float[] colorForestGreen = {108/255f, 225/255f, 249/255f};
		mRandomColors.add(colorForestGreen);

		float[] colorGoldenrod = {224/255f, 253/255f, 96/255f};
		mRandomColors.add(colorGoldenrod);

		float[] colorDarkGoldenrod = {163/255f, 146/255f, 247/255f};
		mRandomColors.add(colorDarkGoldenrod);

		float[] colorSteelBlue1 = {254/255f, 210/255f, 112/255f};
		mRandomColors.add(colorSteelBlue1);

		float[] colorBlueViolet = {253/255f, 157/255f, 247/255f};
		mRandomColors.add(colorBlueViolet);

	}


	private int mOtherLikeCounter = 0;
	public void onEventMainThread(BalloonParticleEvents.BalloonParticleLifeCircleBegin event) {
		if (!event.isSelf()){
			mOtherLikeCounter++;
			Log.d("likecount", "mOtherLikeCounter:"+mOtherLikeCounter);
		}
	}

	public void onEventMainThread(BalloonParticleEvents.BalloonParticleLifeCircleEnd event) {
		if (!event.isSelf()){
			mOtherLikeCounter--;
			Log.d("likecount", "mOtherLikeCounter:"+mOtherLikeCounter);
		}
	}


}
