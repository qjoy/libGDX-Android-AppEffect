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
import android.view.View;
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
import alex.com.gdxdemo.testcode.utils;


public class MainActivity extends FragmentActivity implements AndroidFragmentApplication.Callbacks ,View.OnClickListener{

    private GiftParticleFragment m_libgdxFgm;
    private TextView m_tvLog;
    private ReceiveBroadCast m_receiveBroadCast;
    private SystemReceiveBroadCast m_systemreceiveBroadCast;
    private WeakHandler m_weakHandler = new WeakHandler();
    private ScrollView m_scrollv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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

        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);

        findViewById(R.id.add1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_libgdxFgm.PlayAdd(GiftParticleContants.GIFT_PATHTYPE_EXTEND, getRandomGift(), GiftParticleContants.GIFT_PARTICLETYPE_FIRE, 200);
            }
        });

        findViewById(R.id.add2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_libgdxFgm.PlayAdd(GiftParticleContants.GIFT_PATHTYPE_EXTEND, getRandomGift(), GiftParticleContants.GIFT_PARTICLETYPE_WATER_BOX1, 1000);
            }
        });

        findViewById(R.id.add3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_libgdxFgm.PlayAdd(GiftParticleContants.GIFT_PATHTYPE_EXTEND, getRandomGift(), GiftParticleContants.GIFT_PARTICLETYPE_WATER_BOX2, 1500);
            }
        });

        findViewById(R.id.add4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_libgdxFgm.PlayAdd(GiftParticleContants.GIFT_PATHTYPE_EXTEND, getRandomGift(), GiftParticleContants.GIFT_PARTICLETYPE_WATER_BOX3, 2000);
            }
        });

        findViewById(R.id.add5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_libgdxFgm.PlayAdd(GiftParticleContants.GIFT_PATHTYPE_EXTEND, getRandomGift(), GiftParticleContants.GIFT_PARTICLETYPE_WATER_BOX4, 3000);
            }
        });

        findViewById(R.id.random).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_weakHandler.postDelayed(new SmallRunnable(), 1);
                m_weakHandler.postDelayed(new BigRunnable(), 1);
                findViewById(R.id.random).setEnabled(false);
            }
        });
    }

    private class SmallRunnable implements Runnable{

        @Override
        public void run() {
            m_libgdxFgm.PlayAdd(GiftParticleContants.GIFT_PATHTYPE_EXTEND, getRandomGift(), GiftParticleContants.GIFT_PARTICLETYPE_FIRE, 200);
            m_weakHandler.postDelayed(new SmallRunnable(), 250);
        }
    }

    private class BigRunnable implements Runnable{

        @Override
        public void run() {
            int index = (int)(Math.random() * 3 + 1);
            m_libgdxFgm.PlayAdd(GiftParticleContants.GIFT_PATHTYPE_EXTEND, getRandomGift(), index, 2000);
            m_weakHandler.postDelayed(new BigRunnable(), 10000);
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

            Log.d(MainActivity.class.getSimpleName(), "ReceiveBroadCast[^^^^^^^]play Particle Receive: " + intent.getAction());
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

            Log.d(MainActivity.class.getSimpleName(), "SystemReceiveBroadCast[^^^^^^^]play Particle Receive: " + intent.getAction());
            if (intent.getAction().equals(GiftParticleContants.BROADCAST_GIFTPARTICLE_BACKKEY)) {
                checkquit();
            }
        }
    }

    @Override
    public void onClick(View v) {
        String name = "";
        switch (v.getId()){
            case R.id.btn1:
                NormalActivity.launch(this);
                break;
            case R.id.btn2:
                HalfScreenActivity.launch(this);
                break;
            case R.id.btn3:
                dialogTest();
                break;
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

    protected void dialogTest() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确认退出应用吗？");

        builder.setTitle("提示");

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                MainActivity.this.finish();
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

                MainActivity.this.finish();
            }
        });

        builder.create().show();
    }

    private void setAssetes(){
        File sd= Environment.getExternalStorageDirectory();
        String path=sd.getPath()+"/libgdxDemo";
        File file=new File(path);
        if(!file.exists()) {
            file.mkdir();
            for (int i=1; i<18;i++){
                String filename = "/"+i;
                utils.copy("gifts"+filename, path + filename);
            }
        }
        else{

        }
    }

    private String getRandomGift(){

        String index = String.valueOf((int)(Math.random() * 17)+1);
        Log.d("MainActivity", "gift index:"+index);

        final  String externalPath = "libgdxDemo" + File.separator + GiftParticleContants.GIFT_BASE + index;

        if (!GiftParticleEffectView.fileIsExist(externalPath))
        {
            dialog("图片资源文件不存在或者路径不正确，请查看测试代码:"+utils.getLineInfo());
        }

        return externalPath;
    }

}
