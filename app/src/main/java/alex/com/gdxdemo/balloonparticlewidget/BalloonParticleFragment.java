package alex.com.gdxdemo.balloonparticlewidget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.badoo.mobile.util.WeakHandler;

import alex.com.gdxdemo.R;
import alex.com.gdxdemo.base.InterceptableViewGroup;
import alex.com.gdxdemo.giftparticlewidget.GiftParticleContants;
import de.greenrobot.event.EventBus;


/**
 * Created by alex_xq on 15/6/11.
 */
@SuppressWarnings("All")
public class BalloonParticleFragment extends AndroidFragmentApplication implements InputProcessor {

    private static final String TAG = "BalloonParticleFragment";
    private View m_viewRooter = null;
    //粒子效果UI容器层
    private InterceptableViewGroup mContainer;
    //粒子效果绘制层
    private BalloonParticleEffectView particleEffectView;
    //Fragment 处于销毁过程中标志位
    private boolean m_isDestorying = false;
    //Fragment 处于OnStop标志位
    private boolean m_isStoping = false;
    //Screen 是否需要重建播放
    private boolean m_isNeedBuild =true;

    private WeakHandler m_WeakHandler = new WeakHandler();

    public void PlayAdd(int pathtype, String pathstring, int dur, float[] rgb, final boolean isSelf) {

        if (m_isStoping || isScreenLock())
            return;

        if (pathstring == null)
            return;
        if (pathstring.equals(""))
            return;

        boolean isLand = (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) ? true : false;

	    particleEffectView.Add(pathstring,
			    dur,
			    isLand,
			    rgb,
			    isSelf);
	    

	    particleEffectView.setOnStateListener(new BalloonParticleEffectView.OnStateListener() {
		    @Override
		    public void OnBegin(boolean isself) {

			    EventBus.getDefault().post(new BalloonParticleEvents.BalloonParticleLifeCircleBegin(isself));

		    }

		    @Override
		    public void OnFinish(boolean isself) {

			    EventBus.getDefault().post(new BalloonParticleEvents.BalloonParticleLifeCircleEnd(isself));

		    }
	    });

    }

	public void switchSound(boolean open)
	{
		if (particleEffectView != null)
		{
			particleEffectView.switchSound(open);
		}
	}

    public void preDestory(){
        m_isDestorying = true;
        m_isStoping = true;
	    particleEffectView.setCanDraw(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        m_viewRooter = inflater.inflate(R.layout.lf_layout_giftparticle, null);
        return m_viewRooter;
    }

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		buildGDX();
	}

	public void cleanGDX(){
        try {
            particleEffectView.dispose();
        }catch (Exception e){}
        mContainer.removeAllViews();
        particleEffectView = null;
    }

    public void buildGDX(){

        particleEffectView = new BalloonParticleEffectView();
        View effectview = CreateGLAlpha(particleEffectView);
        mContainer = (InterceptableViewGroup) m_viewRooter.findViewById(R.id.container);
        mContainer.addView(effectview);
	    mContainer.setIntercept(true);
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart");
        m_isStoping = false;
        super.onStart();

	    particleEffectView.setCanDraw(true);
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop");
        m_isStoping = true;
	    particleEffectView.setCanDraw(false);
        super.onStop();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
        if (!m_isDestorying && !isScreenLock())
            super.onResume();
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);

        mContainer.removeAllViews();
        buildGDX();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private View CreateGLAlpha(ApplicationListener application) {
        //	    GLSurfaceView透明相关
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.r = cfg.g = cfg.b = cfg.a = 8;

        View view = initializeForView(application, cfg);

        if (view instanceof SurfaceView) {
            GLSurfaceView glView = (GLSurfaceView) graphics.getView();
            glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
            glView.setZOrderMediaOverlay(true);
            glView.setZOrderOnTop(true);
        }

        return view;
    }

    @Override
    public boolean keyDown(int i) {
        if (i == Input.Keys.BACK) {
            Intent intent = new Intent();
            intent.setAction(GiftParticleContants.BROADCAST_GIFTPARTICLE_BACKKEY);
	        if (getActivity() != null)
                getActivity().sendBroadcast(intent);
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(int i) {
        return false;
    }

    private boolean isScreenLock(){
        try {
            PowerManager pm = (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);
            boolean isScreenOn = pm.isScreenOn();//如果为true，则表示屏幕“亮”了，否则屏幕“暗”了。
            Log.d(TAG, "isScreenLock:" + !isScreenOn);
            return !isScreenOn;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
