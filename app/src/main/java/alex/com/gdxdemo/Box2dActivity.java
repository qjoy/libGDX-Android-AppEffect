package alex.com.gdxdemo;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.badoo.mobile.util.WeakHandler;

import alex.com.gdxdemo.gift2dview.Box2DFragment;
import alex.com.gdxdemo.gift2dview.Tools.GiftParticleContants;
import alex.com.gdxdemo.gift2dview.Tools.ScreenParamUtil;
import alex.com.gdxdemo.testcode.SpringEffect;
import butterknife.Bind;
import butterknife.ButterKnife;

public class Box2dActivity extends FragmentActivity implements AndroidFragmentApplication.Callbacks{

    private Box2DFragment m_box2dFgm;
    private WeakHandler m_weakHandler = new WeakHandler();
    private SystemReceiveBroadCast m_systemreceiveBroadCast;
    private boolean m_bCrazyMode = false;

	private int m_giftIndex = 1;
	private int m_giftCounter = 0;

    @Bind(R.id.random)
    public Button m_random;

	@Bind(R.id.countEdit)
	public EditText m_countEditText;

	@Bind(R.id.countEdit1)
	public EditText m_countEditText1;


	@Bind(R.id.lyt_container)
	public FrameLayout m_container;

	public static void launch(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, Box2dActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box2d_activity);

        ButterKnife.bind(this);

	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        m_systemreceiveBroadCast = new SystemReceiveBroadCast();
        IntentFilter filter1 = new IntentFilter();
        filter1.setPriority(800);
        filter1.addAction(GiftParticleContants.BROADCAST_GIFTPARTICLE_BACKKEY);
        registerReceiver(m_systemreceiveBroadCast, filter1);

        m_box2dFgm = new Box2DFragment();
	    getSupportFragmentManager().beginTransaction().add(R.id.lyt_container, m_box2dFgm).commit();
	    showBox2dFgmFullScreen();
        SpringEffect.doEffectSticky(findViewById(R.id.random), new Runnable() {
            @Override
            public void run() {

                if (m_bCrazyMode == false) {
                    m_weakHandler.postDelayed(m_runnableCrazyMode, 50);
                    m_random.setText("Stop crazyMode");
                } else {
                    m_weakHandler.removeCallbacks(m_runnableCrazyMode);
                    m_random.setText("Start crazyMode");
                }
                m_bCrazyMode = !m_bCrazyMode;
            }
        });

	    SpringEffect.doEffectSticky(findViewById(R.id.minusBtn), new Runnable() {
		    @Override
		    public void run() {
			    int count = Integer.valueOf(m_countEditText.getText().toString());
			    if (count==1)
				    return;
			    count--;
			    m_countEditText.setText(count);
		    }
	    });
	    SpringEffect.doEffectSticky(findViewById(R.id.addBtn), new Runnable() {
		    @Override
		    public void run() {
			    int count = Integer.valueOf(m_countEditText.getText().toString());
			    if (count==147)
				    return;
			    count++;
			    m_countEditText.setText(""+count);
		    }
	    });

	    SpringEffect.doEffectSticky(findViewById(R.id.minusBtn1), new Runnable() {
		    @Override
		    public void run() {
			    int count = Integer.valueOf(m_countEditText1.getText().toString());
			    if (count==1)
				    return;
			    count--;
			    m_countEditText1.setText(count);
		    }
	    });
	    SpringEffect.doEffectSticky(findViewById(R.id.addBtn1), new Runnable() {
		    @Override
		    public void run() {
			    int count = Integer.valueOf(m_countEditText1.getText().toString());
			    if (count==100)
				    return;
			    count++;
			    m_countEditText1.setText(""+count);
		    }
	    });

	    SpringEffect.doEffectSticky(findViewById(R.id.sendGiftBtn), new Runnable() {
		    @Override
		    public void run() {
			    m_giftIndex = Integer.valueOf(m_countEditText.getText().toString());
			    m_giftCounter  = Integer.valueOf(m_countEditText1.getText().toString());

			    m_weakHandler.postDelayed(m_runnableSendGift, 20);
		    }
	    });

	    SpringEffect.doEffectSticky(findViewById(R.id.sendStarBtn), new Runnable() {
		    @Override
		    public void run() {
			    m_weakHandler.postDelayed(m_runnableSendStar, 50);
		    }
	    });
    }

    private boolean m_testleft = false;
    private Runnable m_runnableCrazyMode = new Runnable() {
        @Override
        public void run() {
            m_box2dFgm.addGift(15);
            m_testleft = !m_testleft;
            m_weakHandler.postDelayed(m_runnableCrazyMode, 50);
        }
    };

	private boolean m_testleft1 = false;
	private int counter = 0;
	private Runnable m_runnableSendGift = new Runnable() {
		@Override
		public void run() {
			if (counter == m_giftCounter)
			{
				counter = 0;
				return;
			}
			counter++;
			m_box2dFgm.addGift(m_giftIndex);
			m_testleft1 = !m_testleft1;
			m_weakHandler.postDelayed(m_runnableSendGift, 50);
		}
	};

	private boolean m_testleft2 = false;
	private Runnable m_runnableSendStar = new Runnable() {
		@Override
		public void run() {
			counter++;
			m_box2dFgm.addStar(m_testleft2, true);
			m_testleft2 = !m_testleft2;
//			m_weakHandler.postDelayed(m_runnableSendStar, 50);
		}
	};

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(m_systemreceiveBroadCast);
        m_weakHandler.removeCallbacks(m_runnableCrazyMode);
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

    protected void dialogTest() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确认退出应用吗？");

        builder.setTitle("提示");

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                Box2dActivity.this.finish();
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

                Box2dActivity.this.finish();
            }
        });

        builder.create().show();
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

	private void showBox2dFgmNormalScreen(){
		int width = ScreenParamUtil.GetScreenWidthPx(this);
		int height = ScreenParamUtil.GetScreenHeightDp(this);
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)m_container.getLayoutParams();
		params.width = width;
		params.height = height;
		m_container.setLayoutParams(params);
	}

	@Override
	public void finish() {
		m_box2dFgm.preDestory();
		super.finish();
	}
}
