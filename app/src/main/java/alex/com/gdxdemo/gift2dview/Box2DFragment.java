package alex.com.gdxdemo.gift2dview;

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
import android.util.DisplayMetrics;
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
import alex.com.gdxdemo.gift2dview.Tools.GiftParticleContants;


/**
 * Created by alex_xq on 15/6/11.
 */
@SuppressWarnings("All")
public class Box2DFragment extends AndroidFragmentApplication implements InputProcessor {

	private static final String TAG = "Box2DFragment";

	public static float s_scale = 1.0f;

	private View m_viewRooter = null;
	//粒子效果UI容器层
	private InterceptableViewGroup mContainer;
	//粒子效果绘制层
	private Box2dEffectView box2dEffectView;
	//Fragment 处于销毁过程中标志位
	private boolean m_isDestorying = false;
	//Fragment 处于OnStop标志位
	private boolean m_isStoping = false;
	//Screen 是否需要重建播放
	private boolean m_isNeedBuild = true;

	private WeakHandler mHandler = new WeakHandler();
	//是否已被清除
	private boolean m_cleaned = false;

	PowerManager pm;

	public void preDestory() {
		m_isDestorying = true;
		box2dEffectView.setCanDraw(false);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		m_viewRooter = inflater.inflate(R.layout.lf_layout_giftparticle, null);
		pm = (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);

		DisplayMetrics dm = getActivity().getApplicationContext().getResources().getDisplayMetrics();
		s_scale = dm.density/4.0f;

		return m_viewRooter;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		buildGDX();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		box2dEffectView.release();
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);

		cleanGDX();
		buildGDX();
	}

	public void addStar(boolean isLeft, boolean isSelf) {
		try {
			box2dEffectView.addStar(isLeft, isSelf);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addGift(int index) {
		try {
			box2dEffectView.addGift(index);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void cleanGDX() {
		try {
			box2dEffectView.dispose();
		}catch (Exception e){}
		mContainer.removeAllViews();
		box2dEffectView = null;
	}

	public void buildGDX() {
		box2dEffectView = new Box2dEffectView(getActivity());
		View effectview = CreateGLAlpha(box2dEffectView);
		mContainer = (InterceptableViewGroup) m_viewRooter.findViewById(R.id.container);
		mContainer.addView(effectview);
		Gdx.input.setInputProcessor(this);
		Gdx.input.setCatchBackKey(true);
	}

	public void openDebugRenderer(boolean open) {
		box2dEffectView.openDebugRenderer(open);
	}

	@Override
	public void onStart() {
		Log.d(TAG, "onStart");

		box2dEffectView.setCanDraw(true);
		super.onStart();

	}

	@Override
	public void onStop() {
		Log.d(TAG, "onStop");

		box2dEffectView.setCanDraw(false);
		super.onStop();
	}

	@Override
	public void onResume() {
		Log.d(TAG, "onResume");
		try {
			super.onResume();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onPause() {
		Log.d(TAG, "onPause");

		try {
			super.onPause();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (mContainer != null && mContainer.getChildCount() <= 0)
			return;

		if (!m_isDestorying && !isScreenLock())
			super.onResume();
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

	private boolean isScreenLock() {
		boolean isScreenOn = pm.isScreenOn();//如果为true，则表示屏幕“亮”了，否则屏幕“暗”了。
//        Log.d(TAG, "isScreenLock:" + !isScreenOn);
		return !isScreenOn;
	}
}
