package alex.com.gdxdemo;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.badoo.mobile.util.WeakHandler;

import java.io.File;

import alex.com.gdxdemo.giftparticlewidget.GiftParticleContants;
import alex.com.gdxdemo.giftparticlewidget.GiftParticleEffectView;
import alex.com.gdxdemo.giftparticlewidget.GiftParticleFragment;
import alex.com.gdxdemo.testcode.HalfScreenActivity;
import alex.com.gdxdemo.testcode.NormalActivity;
import alex.com.gdxdemo.testcode.SpringEffect;
import alex.com.gdxdemo.testcode.utils;


public class GiftParticleEffectActivity extends FragmentActivity implements AndroidFragmentApplication.Callbacks{

    private GiftParticleFragment m_libgdxFgm;
    private TextView m_tvLog;
    private ReceiveBroadCast m_receiveBroadCast;
    private SystemReceiveBroadCast m_systemreceiveBroadCast;
    private WeakHandler m_weakHandler = new WeakHandler();
    private ScrollView m_scrollv;
    private boolean isdestoryed = false;
	private boolean m_bOpenCrazyMode = false;
	private SmallRunnable m_smallRunnable = new SmallRunnable();
	private BigRunnable m_bigRunnable = new BigRunnable();

	public static void launch(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, GiftParticleEffectActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gift_activity);


        setAssetes();
        m_receiveBroadCast = new ReceiveBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(800);
        filter.addAction(GiftParticleContants.BROADCAST_PARTICLE_OVER);
        filter.addAction(GiftParticleContants.BROADCAST_PARTICLE_BEGIN);
        registerReceiver(m_receiveBroadCast, filter);

        m_systemreceiveBroadCast = new SystemReceiveBroadCast();
        IntentFilter filter1 = new IntentFilter();
        filter1.setPriority(800);
        filter1.addAction(GiftParticleContants.BROADCAST_GIFTPARTICLE_BACKKEY);
        registerReceiver(m_systemreceiveBroadCast, filter1);

        m_libgdxFgm = (GiftParticleFragment) getSupportFragmentManager().findFragmentById(R.id.libgdxFrag);

        m_scrollv = (ScrollView) findViewById(R.id.scrollv);
        m_tvLog = (TextView) findViewById(R.id.log);

        SpringEffect.doEffectSticky(findViewById(R.id.btn1), new Runnable() {
            @Override
            public void run() {
                NormalActivity.launch(GiftParticleEffectActivity.this);
            }
        });

        SpringEffect.doEffectSticky(findViewById(R.id.btn2), new Runnable() {
            @Override
            public void run() {
                HalfScreenActivity.launch(GiftParticleEffectActivity.this);
            }
        });

        SpringEffect.doEffectSticky(findViewById(R.id.btn3), new Runnable() {
            @Override
            public void run() {
                dialogTest();
            }
        });

        SpringEffect.doEffectSticky(findViewById(R.id.add1), new Runnable() {
            @Override
            public void run() {
                m_libgdxFgm.PlayAdd(GiftParticleContants.GIFT_PATHTYPE_EXTEND, getRandomGift(), GiftParticleContants.GIFT_PARTICLETYPE_FIRE, 200);
            }
        });

        SpringEffect.doEffectSticky(findViewById(R.id.add2), new Runnable() {
            @Override
            public void run() {
                m_libgdxFgm.PlayAdd(GiftParticleContants.GIFT_PATHTYPE_EXTEND, getRandomGift(), GiftParticleContants.GIFT_PARTICLETYPE_WATER_BOX1, 1000);
            }
        });

        SpringEffect.doEffectSticky(findViewById(R.id.add3), new Runnable() {
            @Override
            public void run() {
                m_libgdxFgm.PlayAdd(GiftParticleContants.GIFT_PATHTYPE_EXTEND, getRandomGift(), GiftParticleContants.GIFT_PARTICLETYPE_WATER_BOX2, 1500);
            }
        });

        SpringEffect.doEffectSticky(findViewById(R.id.add4), new Runnable() {
	        @Override
	        public void run() {
		        m_libgdxFgm.PlayAdd(GiftParticleContants.GIFT_PATHTYPE_EXTEND, getRandomGift(), GiftParticleContants.GIFT_PARTICLETYPE_WATER_BOX3, 2000);
	        }
        });

        SpringEffect.doEffectSticky(findViewById(R.id.add5), new Runnable() {
	        @Override
	        public void run() {
		        m_libgdxFgm.PlayAdd(GiftParticleContants.GIFT_PATHTYPE_EXTEND, getRandomGift(), GiftParticleContants.GIFT_PARTICLETYPE_WATER_BOX4, 3000);
	        }
        });

	    final TextView crazymodeBtn = (TextView)findViewById(R.id.random);
        SpringEffect.doEffectSticky(findViewById(R.id.random), new Runnable() {
	        @Override
	        public void run() {
		        if (m_bOpenCrazyMode ==false) {
			        m_weakHandler.postDelayed(m_smallRunnable, 1);
			        m_weakHandler.postDelayed(m_bigRunnable, 1);

			        crazymodeBtn.setText("close CrazyMode");
		        }
		        else{
			        m_weakHandler.removeCallbacks(m_bigRunnable);
			        m_weakHandler.removeCallbacks(m_smallRunnable);
			        crazymodeBtn.setText("open CrazyMode");
		        }

		        m_bOpenCrazyMode = !m_bOpenCrazyMode;
	        }
        });

//	    VideoView videoView = (VideoView) findViewById(R.id.videoview);
//	    /**
//	     * VideoView控制视频播放的功能相对较少，具体而言，它只有start和pause方法。为了提供更多的控制，
//	     * 可以实例化一个MediaController，并通过setMediaController方法把它设置为VideoView的控制器。
//	     */
//	    videoView.setMediaController(new MediaController(this));
//	    Uri uri = Uri.parse("http://7xox5k.com1.z0.glb.clouddn.com/box2d-demo-total.mp4");
//	    videoView.setVideoURI(uri);
//	    videoView.start();
    }

    private class SmallRunnable implements Runnable{

        @Override
        public void run() {
            if (isdestoryed)
                return;
            m_libgdxFgm.PlayAdd(GiftParticleContants.GIFT_PATHTYPE_EXTEND, getRandomGift(), GiftParticleContants.GIFT_PARTICLETYPE_FIRE, 200);
            m_weakHandler.postDelayed(m_smallRunnable, 250);
        }
    }

    private class BigRunnable implements Runnable{

        @Override
        public void run() {
            if (isdestoryed)
                return;
            int index = (int)(Math.random() * 3 + 1);
            m_libgdxFgm.PlayAdd(GiftParticleContants.GIFT_PATHTYPE_EXTEND, getRandomGift(), index, 2000);
            m_weakHandler.postDelayed(m_bigRunnable, 10000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(m_systemreceiveBroadCast);
        unregisterReceiver(m_receiveBroadCast);
    }

    @Override
    public void exit() {

    }

    int linecount = 0;
    public class ReceiveBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            linecount++;

            Log.d(GiftParticleEffectActivity.class.getSimpleName(), "ReceiveBroadCast[^^^^^^^]play Particle Receive: " + intent.getAction());
            if (intent.getAction().equals(GiftParticleContants.BROADCAST_PARTICLE_OVER)) {
                m_tvLog.append("line:" + linecount + "    broadcast_particle_OVER\n");
            }
            else if(intent.getAction().equals(GiftParticleContants.BROADCAST_PARTICLE_BEGIN)){
                m_tvLog.append("line:" + linecount + "    broadcast_particle_BEGIN\n");
            }

            m_weakHandler.post(new Runnable() {
                @Override
                public void run() {
                    m_scrollv.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
        }
    }

    public class SystemReceiveBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d(GiftParticleEffectActivity.class.getSimpleName(), "SystemReceiveBroadCast[^^^^^^^]play Particle Receive: " + intent.getAction());
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

    protected void dialogTest() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确认退出应用吗？");

        builder.setTitle("提示");

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                GiftParticleEffectActivity.this.finish();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    protected void dialog(String tip) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(tip);

        builder.setTitle("提示");

        builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                GiftParticleEffectActivity.this.finish();
            }
        });

        builder.create().show();
    }

    private void setAssetes(){
        File sd= Environment.getExternalStorageDirectory();
        String path=sd.getPath()+"/libgdxDemo/particle.gifts";
        File file=new File(path);
        if(!file.exists()) {
            file.mkdir();
            for (int i=1; i<24;i++){
                String filename = "/"+i;
                utils.copy("particle/gifts" +filename, path + filename);
            }
        }
        else{

        }
    }

    private String getRandomGift(){

        String index = String.valueOf((int)(Math.random() * 23)+1);
        Log.d("MainActivity", "gift index:"+index);

        final  String externalPath = "libgdxDemo/particle.gifts" + File.separator + GiftParticleContants.GIFT_BASE + index;

        if (!GiftParticleEffectView.fileIsExist(externalPath))
        {
            dialog("图片资源文件不存在或者路径不正确，请查看测试代码:"+utils.getLineInfo());
        }

        return externalPath;
    }

}
