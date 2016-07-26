package alex.com.gdxdemo.balloonparticlewidget;

import android.util.Log;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;

import alex.com.gdxdemo.testcode.utils;

/**
 * Created by alex_xq on 14/11/2.
 */
@SuppressWarnings("All")
public class BalloonParticleEffectView implements ApplicationListener {

	private static final String TAG = "BalloonParticleEffectView";

	//基础绘制资源
	SpriteBatch mBatch;

	//	Asset资源加载管理
	AssetManager mAssetManager = new AssetManager();

	private ParticleEffectPool mParticleEffectPool = null;

	ParticleEffect mParticle;

	int mWidth = 0;

	private boolean forceOver = false;

	private boolean mIsLand = false;

	private boolean m_candraw = true;

	private boolean m_soundOpen = false;

//	Object mLockRenderOpt = new Object();

	private List<Sound> m_listWaterPopSounds = new ArrayList<>();



	class ParticleInfo {
		public ParticleEffect particle = null;
		public int playstate = 0;
		public int duration = 1000;
		public float stateTime = 0;
		public Array<TextureRegion> beginAnimTexList = new Array<TextureRegion>();
		public Array<TextureRegion> endAnimTexList = new Array<TextureRegion>();
		public Animation beginAnimation = null;
		public Animation endAnimation = null;
		public boolean isSelf = false;
	}

	List<ParticleInfo> mParticles = new ArrayList<ParticleInfo>();

	private boolean isSelfCheckFromParticle(ParticleEffect particle){
		for (int i=0 ;i<mParticles.size(); i++){
			ParticleInfo info = mParticles.get(i);
			if (info.particle == particle){
				return info.isSelf;
			}
		}
		return false;
	}

	private class PutRenderInfo {
		public String extentPath;
		public int duration;
		public float R;
		public float G;
		public float B;
		public boolean isSelf;
	}

	List<PutRenderInfo> mPutRenderInfos = new ArrayList<PutRenderInfo>();


	public interface OnStateListener {
		public void OnBegin(boolean isSelf);

		public void OnFinish(boolean isSelf);
	}

	private OnStateListener onStateListener;

	public void forceOver() {
		forceOver = true;
	}

	public void switchSound(boolean open){
		m_soundOpen = open;
	}

	@Override
	public void create() {

		mBatch = new SpriteBatch();

		mWidth = Gdx.graphics.getWidth() > Gdx.graphics.getHeight() ? Gdx.graphics.getHeight() : Gdx.graphics.getWidth();

		balloonParticleInit();

		soundInit();
	}

	@Override
	public void resize(int i, int i2) {

	}

	public void setCanDraw(boolean candraw) {
		m_candraw = candraw;

		if (!m_candraw) {
			for (int i = 0; i < mParticles.size(); i++) {

				ParticleInfo particleInfo = mParticles.get(i);

				mParticles.remove(particleInfo);
				particleInfo.particle.dispose();
				i--;

				onStateListener.OnFinish(particleInfo.isSelf);

			}
			return;
		}
	}

	@Override
	public void render() {

		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		if (forceOver)
			return;

		if (!m_candraw) {
			for (int i = 0; i < mParticles.size(); i++) {

				ParticleInfo particleInfo = mParticles.get(i);

				mParticles.remove(particleInfo);
				particleInfo.particle.dispose();
				i--;

				onStateListener.OnFinish(particleInfo.isSelf);

			}
			return;
		}

//		动态加入需要展示的粒子效果
		dataLogicGet();


		mBatch.begin();
//		粒子效果绘制
		for (int i = 0; i < mParticles.size(); i++) {

			ParticleInfo particleInfo = mParticles.get(i);

			boolean bover = false;

			bover = renderParticle(particleInfo);

			if (bover == true) {
				mParticles.remove(particleInfo);
				particleInfo.particle.dispose();
				i--;

				onStateListener.OnFinish(particleInfo.isSelf);
			}
		}
		mBatch.end();

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		mBatch.dispose();
		//遍历释放所有particle的资源


		for (ParticleInfo info : mParticles) {
			if (info.particle != null)
				info.particle.dispose();
		}

		if (mParticleEffectPool != null)
			mParticleEffectPool.clear();

	}

	public void Add(String extentPath, int duration, boolean isLand, float[] rgb, boolean isSelf) {
		if (extentPath == null ||
				extentPath.equals("") ||
				duration <= 0) {
			Log.e(TAG, "Param invalid");
			return;
		}

		mIsLand = isLand;

		PutRenderInfo info = new PutRenderInfo();
		info.extentPath = extentPath;
		info.duration = duration;
		info.R = rgb[0];
		info.G = rgb[1];
		info.B = rgb[2];
		info.isSelf = isSelf;
		mPutRenderInfos.add(info);
	}

	private void AddParticle(String extentPath, int duration, float R, float G, float B, boolean isSelf) {
		ParticleInfo particleInfo = new ParticleInfo();

		particleInfo.isSelf = isSelf;
//		particleInfo.duration = duration;

		String particleFileName = "particle/heartballoon.p";

		//创建粒子系统
		//放大系数
		float scale_lowMin = utils.DpToPx(0);
		float scale_lowMax = utils.DpToPx(0);
		float scale_highMin = utils.DpToPx(43);
		float scale_highMax = utils.DpToPx(48);
		//移动系数
		float move_lowMin = utils.DpToPx(15);
		float move_lowMax = utils.DpToPx(25);
		float move_highMin = utils.DpToPx(95);
		float move_highMax = utils.DpToPx(125);

		if (Gdx.files.internal(extentPath).exists())
			mParticle.loadEmitterImages( Gdx.files.internal(extentPath));
		else
			Log.e(TAG, "filePath is not exists:" + extentPath);

		try{
			mParticle.getEmitters().get(0).getScale().setLow(scale_lowMin, scale_lowMax);
			mParticle.getEmitters().get(0).getScale().setHigh(scale_highMin, scale_highMax);
		}
		catch (Exception e){
			e.printStackTrace();
		}

		try {
			mParticle.getEmitters().get(0).getVelocity().setLow(move_lowMin, move_lowMax);
			mParticle.getEmitters().get(0).getVelocity().setHigh(move_highMin, move_highMax);
		}
		catch (Exception e){
			e.printStackTrace();
		}


		if (mParticleEffectPool == null)
			mParticleEffectPool = new ParticleEffectPool(mParticle, 3, 3);

		ParticleEffect particleTmp = mParticleEffectPool.obtain();


		int randomX = 0;
		int randomY = 0;

		if (mIsLand) {
			randomX = (int) (Math.random() * 20) + (Gdx.graphics.getWidth() / 2);
			randomY = 100;
		} else {
			randomX = Gdx.graphics.getWidth() - 120 - (int) (Math.random() * 20);
			randomY =  -30;
		}

		particleTmp.setPosition(randomX, randomY);


		onStateListener.OnBegin(particleInfo.isSelf);

		int randomSoundIndex = (int)((float)Math.random()*10);
		randomSoundIndex = randomSoundIndex>6?1:0;
		if (m_soundOpen)
			m_listWaterPopSounds.get(randomSoundIndex).play();

		particleTmp.setDuration(duration);

		if (R>=0 && G>=0 && B>= 0)
			setColor(particleTmp, R, G, B);

		particleInfo.particle = particleTmp;

		mParticles.add(particleInfo);
	}

	void setColor(ParticleEffect pf, float R, float G, float B){
		Array<ParticleEmitter> emitters = pf.getEmitters();
		int i = 0;

		for(int n = emitters.size; i < n; ++i) {
			float[] color = {R, G, B};
			((ParticleEmitter)emitters.get(i)).getTint().setColors(color);
		}
	}

	public void setOnStateListener(OnStateListener onStateListener) {
		this.onStateListener = onStateListener;
	}

	private boolean renderParticle(ParticleInfo particleInfo) {
		boolean bres = false;
		if (particleInfo.playstate == 0) {

			particleInfo.particle.draw(mBatch, Gdx.graphics.getDeltaTime());

			//清除已经播放完成的粒子系统
			if (particleInfo.particle.isComplete()) {

				particleInfo.playstate = 1;

			}

		} else if (particleInfo.playstate == 1) {

			particleInfo.playstate = 2;
			bres = true;

		}
		return bres;
	}

	private void dataLogicGet() {
		int size = mPutRenderInfos.size();
		for (int i = 0; i < size; i++) {
			PutRenderInfo info = mPutRenderInfos.get(i);

			AddParticle(info.extentPath, info.duration, info.R, info.G, info.B, info.isSelf);
			mPutRenderInfos.remove(i);
			i--;
			size = mPutRenderInfos.size();
		}
	}

	private void balloonParticleInit(){
		mParticle = new ParticleEffect();

		String particleFileName = "particle/heartballoon.p";
		if ( Gdx.files.internal(particleFileName).exists())
			mParticle.load(Gdx.files.internal(particleFileName), Gdx.files.internal("particle/balloon/1.png"));
	}

	private void soundInit(){
		Sound waterPop1 = Gdx.audio.newSound(Gdx.files.internal("audio/waterpop/waterpop.wav"));
		Sound waterPop2 = Gdx.audio.newSound(Gdx.files.internal("audio/waterpop/waterpop2.wav"));
		m_listWaterPopSounds.add(waterPop1);
		m_listWaterPopSounds.add(waterPop2);
	}
}
