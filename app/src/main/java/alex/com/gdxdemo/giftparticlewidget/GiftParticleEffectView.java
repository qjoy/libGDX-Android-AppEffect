package alex.com.gdxdemo.giftparticlewidget;

import android.util.Log;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex_xq on 14/11/2.
 */
@SuppressWarnings("All")
public class GiftParticleEffectView implements ApplicationListener {

	private static final String TAG = "giftParticleEffectView";

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

//	Object mLockRenderOpt = new Object();

	public enum ParticleAnimationType {
		ANIMATION_TYPE_NULL,
		ANIMATION_TYPE_BOX1,
		ANIMATION_TYPE_BOX2,
		ANIMATION_TYPE_BOX3,
		ANIMATION_TYPE_BOX4,
	}

	public enum ParticleType {
		PARTICLE_TYPE_WATER,
		PARTICLE_TYPE_FIRE,
	}

	class ParticleInfo {
		public ParticleEffect particle = null;
		public int playstate = 0;
		public int duration = 1000;
		public float stateTime = 0;
		public Array<TextureRegion> beginAnimTexList = new Array<TextureRegion>();
		public Array<TextureRegion> endAnimTexList = new Array<TextureRegion>();
		public Animation beginAnimation = null;
		public Animation endAnimation = null;
		public ParticleAnimationType animationType = ParticleAnimationType.ANIMATION_TYPE_NULL;
	}

	List<ParticleInfo> mParticles = new ArrayList<ParticleInfo>();

	private class PutRenderInfo {
		public String extentPath;
		public int duration;
		public ParticleAnimationType animationType;
		public ParticleType particleType;
	}

	List<PutRenderInfo> mPutRenderInfos = new ArrayList<PutRenderInfo>();


	public interface OnStateListener {
		public void OnBegin(ParticleAnimationType type);

		public void OnFinish(ParticleAnimationType type);
	}

	private OnStateListener onStateListener;

	public void forceOver() {
		forceOver = true;
	}

	@Override
	public void create() {

		mBatch = new SpriteBatch();

		mParticle = new ParticleEffect();

		mWidth = Gdx.graphics.getWidth() > Gdx.graphics.getHeight() ? Gdx.graphics.getHeight() : Gdx.graphics.getWidth();
		mWidth = mWidth * 2 / 3;
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

				onStateListener.OnFinish(particleInfo.animationType);

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

				onStateListener.OnFinish(particleInfo.animationType);

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

			if (particleInfo.animationType == ParticleAnimationType.ANIMATION_TYPE_NULL)
				bover = renderParticle(particleInfo);
			else if (particleInfo.animationType == ParticleAnimationType.ANIMATION_TYPE_BOX1)
				bover = renderBox1(particleInfo);
			else if (particleInfo.animationType == ParticleAnimationType.ANIMATION_TYPE_BOX2)
				bover = renderBox2(particleInfo);
			else if (particleInfo.animationType == ParticleAnimationType.ANIMATION_TYPE_BOX3)
				bover = renderVolcano(particleInfo);
			else if (particleInfo.animationType == ParticleAnimationType.ANIMATION_TYPE_BOX4)
				bover = renderVolcano(particleInfo);

			if (bover == true) {
				mParticles.remove(particleInfo);
				particleInfo.particle.dispose();
				i--;

				onStateListener.OnFinish(particleInfo.animationType);
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

	public void Add(String extentPath, int duration, ParticleType particleType, ParticleAnimationType animationType, boolean isLand) {
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
		info.animationType = animationType;
		info.particleType = particleType;
		mPutRenderInfos.add(info);
	}

	private void AddParticle(String extentPath, int duration, ParticleType particleType, ParticleAnimationType animationType) {
		ParticleInfo particleInfo = new ParticleInfo();

		particleInfo.duration = duration;
		particleInfo.animationType = animationType;

		String particleFileName = "";
		if (particleType == ParticleType.PARTICLE_TYPE_FIRE) {
			particleFileName = "particle/firebomb.p";
		} else if (particleType == ParticleType.PARTICLE_TYPE_WATER) {
			particleFileName = "particle/waterfall.p";
		}

		//创建粒子系统

		if (Gdx.files.internal(particleFileName).exists()) {
			if (Gdx.files.external(extentPath).exists())
				mParticle.load(Gdx.files.internal(particleFileName), Gdx.files.external(extentPath));
		} else
			Log.e(TAG, "storePath is not exists:" + extentPath);

		createAnimation(animationType);

		if (mParticleEffectPool == null)
			mParticleEffectPool = new ParticleEffectPool(mParticle, 3, 3);

		ParticleEffect particleTmp = mParticleEffectPool.obtain();

		if (particleType == ParticleType.PARTICLE_TYPE_FIRE) {
			int randomX = 0;
			int randomY = 0;

			if (mIsLand) {
				randomX = (int) (Math.random() * Gdx.graphics.getWidth() * 3 / 5) + (Gdx.graphics.getWidth() / 5);
				randomY = (int) (Math.random() * Gdx.graphics.getHeight() * 3 / 5) + (Gdx.graphics.getHeight() / 5);
			} else {
				int newHeight = Gdx.graphics.getWidth() * 3 / 4;
				randomX = (int) (Math.random() * Gdx.graphics.getWidth() * 3 / 5) + (Gdx.graphics.getWidth() / 5);
				randomY = Gdx.graphics.getHeight() - ((int) (Math.random() * newHeight * 3 / 5) + (newHeight / 5));

			}

			particleTmp.setPosition(randomX, randomY);

		} else if (particleType == ParticleType.PARTICLE_TYPE_WATER) {


			if (animationType == ParticleAnimationType.ANIMATION_TYPE_BOX3) {
				int yPos = mIsLand ? 210 : 475;
				particleTmp.setPosition(Gdx.graphics.getWidth() / 2 - 68, yPos);
			} else if (animationType == ParticleAnimationType.ANIMATION_TYPE_BOX1) {
				int yPos = mIsLand ? 200 : 470;
				particleTmp.setPosition(Gdx.graphics.getWidth() / 2 - 60, yPos);
			} else if (animationType == ParticleAnimationType.ANIMATION_TYPE_BOX2) {
				int yPos = mIsLand ? 130 : 450;
				particleTmp.setPosition(Gdx.graphics.getWidth() / 2 - 60, yPos);
			} else if (animationType == ParticleAnimationType.ANIMATION_TYPE_BOX4) {
				int yPos = mIsLand ? 210 : 475;
				particleTmp.setPosition(Gdx.graphics.getWidth() / 2 - 68, yPos);
			}

		}

		onStateListener.OnBegin(animationType);

		particleTmp.setDuration(duration);
		particleInfo.particle = particleTmp;

		mParticles.add(particleInfo);
	}

	public void setOnStateListener(OnStateListener onStateListener) {
		this.onStateListener = onStateListener;
	}

	private void createAnimation(ParticleAnimationType type) {

		if (type == ParticleAnimationType.ANIMATION_TYPE_NULL) {

		} else if (type == ParticleAnimationType.ANIMATION_TYPE_BOX1) {

			for (int i = 0; i <= 12; i++) {
				mAssetManager.load("particle/boxone/frame" + i + ".png", Texture.class);
			}

		} else if (type == ParticleAnimationType.ANIMATION_TYPE_BOX2) {

			for (int i = 0; i <= 12; i++) {
				mAssetManager.load("particle/boxtwo/frame" + i + ".png", Texture.class);
			}

		} else if (type == ParticleAnimationType.ANIMATION_TYPE_BOX3) {

			for (int i = 0; i <= 12; i++) {
				mAssetManager.load("particle/boxthree/frame" + i + ".png", Texture.class);
			}

		} else if (type == ParticleAnimationType.ANIMATION_TYPE_BOX4) {

			for (int i = 0; i <= 10; i++) {
				mAssetManager.load("particle/boxfour/frame" + i + ".png", Texture.class);
			}

		}
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

	private boolean renderBox1(ParticleInfo particleInfo) {
		boolean bres = false;
		int yPos = mIsLand ? -28 : 260;
		if (particleInfo.playstate == 0) {

			if (!mAssetManager.update())
				return bres;
			else if (particleInfo.beginAnimation == null)
				initResource(particleInfo);

			particleInfo.stateTime += Gdx.graphics.getDeltaTime();
			TextureRegion currentTextureR = particleInfo.beginAnimation.getKeyFrame(particleInfo.stateTime, false);
			mBatch.draw(currentTextureR, (Gdx.graphics.getWidth() - mWidth) / 2, yPos, mWidth, mWidth);
			if (particleInfo.beginAnimation.isAnimationFinished(particleInfo.stateTime)) {

				particleInfo.playstate = 1;

			}

		} else if (particleInfo.playstate == 1) {

			TextureRegion lastTextureR = particleInfo.beginAnimTexList.get(particleInfo.beginAnimTexList.size - 1);

			mBatch.draw(lastTextureR, (Gdx.graphics.getWidth() - mWidth) / 2, yPos, mWidth, mWidth);



			particleInfo.particle.draw(mBatch, Gdx.graphics.getDeltaTime());


			//清除已经播放完成的粒子系统
			if (particleInfo.particle.isComplete()) {

				particleInfo.playstate = 2;
				particleInfo.stateTime = 0;
			}
		} else if (particleInfo.playstate == 2) {

			particleInfo.stateTime += Gdx.graphics.getDeltaTime();
			TextureRegion currentTextureR = particleInfo.endAnimation.getKeyFrame(particleInfo.stateTime, false);

			mBatch.draw(currentTextureR, (Gdx.graphics.getWidth() - mWidth) / 2, yPos, mWidth, mWidth);


			if (particleInfo.endAnimation.isAnimationFinished(particleInfo.stateTime)) {
				particleInfo.playstate = 3;
			}
		} else if (particleInfo.playstate == 3) {

//			TextureRegion lastTextureR = particleInfo.beginAnimTexList.get(particleInfo.beginAnimTexList.size - 1);
//			mBatch.begin();
//			mBatch.draw(lastTextureR, (Gdx.graphics.getWidth() - mWidth) / 2, yPos, mWidth, mWidth);
//			mBatch.end();

			particleInfo.playstate = 4;
			bres = true;

		}

		return bres;
	}

	private boolean renderBox2(ParticleInfo particleInfo) {
		boolean bres = false;
		int yPos = mIsLand ? -28 : 250;
		if (particleInfo.playstate == 0) {

			if (!mAssetManager.update())
				return bres;
			else if (particleInfo.beginAnimation == null)
				initResource(particleInfo);

			particleInfo.stateTime += Gdx.graphics.getDeltaTime();
			TextureRegion currentTextureR = particleInfo.beginAnimation.getKeyFrame(particleInfo.stateTime, false);


			mBatch.draw(currentTextureR, (Gdx.graphics.getWidth() - mWidth) / 2, yPos, mWidth, mWidth);

			if (particleInfo.beginAnimation.isAnimationFinished(particleInfo.stateTime)) {

				particleInfo.playstate = 1;

			}

		} else if (particleInfo.playstate == 1) {

			TextureRegion lastTextureR = particleInfo.beginAnimTexList.get(particleInfo.beginAnimTexList.size - 1);

			mBatch.draw(lastTextureR, (Gdx.graphics.getWidth() - mWidth) / 2, yPos, mWidth, mWidth);

			particleInfo.particle.draw(mBatch, Gdx.graphics.getDeltaTime());

			//清除已经播放完成的粒子系统
			if (particleInfo.particle.isComplete()) {

				particleInfo.playstate = 2;
				particleInfo.stateTime = 0;
			}
		} else if (particleInfo.playstate == 2) {
			particleInfo.stateTime += Gdx.graphics.getDeltaTime();
			TextureRegion currentTextureR = particleInfo.endAnimation.getKeyFrame(particleInfo.stateTime, false);
			mBatch.draw(currentTextureR, (Gdx.graphics.getWidth() - mWidth) / 2, yPos, mWidth, mWidth);

			if (particleInfo.endAnimation.isAnimationFinished(particleInfo.stateTime)) {
				particleInfo.playstate = 3;
			}
		} else if (particleInfo.playstate == 3) {

//			TextureRegion lastTextureR = particleInfo.beginAnimTexList.get(particleInfo.beginAnimTexList.size - 1);
//			mBatch.draw(lastTextureR, (Gdx.graphics.getWidth() - mWidth) / 2, yPos, mWidth, mWidth);


			particleInfo.playstate = 4;
			bres = true;

		}

		return bres;
	}

	private boolean renderVolcano(ParticleInfo particleInfo) {

		boolean bres = false;

		int yPos = mIsLand ? 0 : 280;

		if (particleInfo.playstate == 0) {

			if (!mAssetManager.update())
				return bres;
			else if (particleInfo.beginAnimation == null)
				initResource(particleInfo);

			particleInfo.stateTime += Gdx.graphics.getDeltaTime();
			TextureRegion currentTextureR = particleInfo.beginAnimation.getKeyFrame(particleInfo.stateTime, false);
			mBatch.draw(currentTextureR, (Gdx.graphics.getWidth() - mWidth) / 2, yPos, mWidth, mWidth);

			if (particleInfo.beginAnimation.isAnimationFinished(particleInfo.stateTime)) {
				particleInfo.playstate = 1;
				return bres;
			}

		} else if (particleInfo.playstate == 1) {

			TextureRegion lastTextureR = particleInfo.beginAnimTexList.get(particleInfo.beginAnimTexList.size - 1);
			mBatch.draw(lastTextureR, (Gdx.graphics.getWidth() - mWidth) / 2, yPos, mWidth, mWidth);

			particleInfo.particle.draw(mBatch, Gdx.graphics.getDeltaTime());

			//清除已经播放完成的粒子系统
			if (particleInfo.particle.isComplete()) {

				particleInfo.playstate = 2;
				particleInfo.stateTime = 0;
			}
		} else if (particleInfo.playstate == 2) {
			particleInfo.stateTime += Gdx.graphics.getDeltaTime();
			TextureRegion currentTextureR = particleInfo.endAnimation.getKeyFrame(particleInfo.stateTime, false);
			mBatch.draw(currentTextureR, (Gdx.graphics.getWidth() - mWidth) / 2, yPos, mWidth, mWidth);
			if (particleInfo.endAnimation.isAnimationFinished(particleInfo.stateTime)) {
				particleInfo.playstate = 3;
			}
		} else if (particleInfo.playstate == 3) {

//			TextureRegion lastTextureR = particleInfo.beginAnimTexList.get(particleInfo.beginAnimTexList.size - 1);
//			mBatch.draw(lastTextureR, (Gdx.graphics.getWidth() - mWidth) / 2, yPos, mWidth, mWidth);

			particleInfo.playstate = 4;
			bres = true;

		}
		return bres;
	}

	private void initResource(ParticleInfo particleInfo) {

		if (particleInfo.animationType == ParticleAnimationType.ANIMATION_TYPE_BOX1) {
			for (int i = 0; i <= 5; i++) {
				TextureRegion temtex = new TextureRegion(mAssetManager.get("particle/boxone/frame" + i + ".png", Texture.class));
				particleInfo.beginAnimTexList.add(temtex);
			}
			particleInfo.beginAnimation = new Animation(0.1f, particleInfo.beginAnimTexList);

			for (int i = 5; i <= 12; i++) {
				TextureRegion temtex = new TextureRegion(mAssetManager.get("particle/boxone/frame" + i + ".png", Texture.class));
				particleInfo.endAnimTexList.add(temtex);
			}
			particleInfo.endAnimation = new Animation(0.08f, particleInfo.endAnimTexList);
		} else if (particleInfo.animationType == ParticleAnimationType.ANIMATION_TYPE_BOX2) {
			for (int i = 0; i <= 5; i++) {
				TextureRegion temtex = new TextureRegion(mAssetManager.get("particle/boxtwo/frame" + i + ".png", Texture.class));
				particleInfo.beginAnimTexList.add(temtex);
			}
			particleInfo.beginAnimation = new Animation(0.1f, particleInfo.beginAnimTexList);

			for (int i = 5; i <= 12; i++) {
				TextureRegion temtex = new TextureRegion(mAssetManager.get("particle/boxtwo/frame" + i + ".png", Texture.class));
				particleInfo.endAnimTexList.add(temtex);
			}
			particleInfo.endAnimation = new Animation(0.08f, particleInfo.endAnimTexList);
		} else if (particleInfo.animationType == ParticleAnimationType.ANIMATION_TYPE_BOX3) {
			for (int i = 0; i <= 6; i++) {
				TextureRegion temtex = new TextureRegion(mAssetManager.get("particle/boxthree/frame" + i + ".png", Texture.class));
				particleInfo.beginAnimTexList.add(temtex);
			}
			particleInfo.beginAnimation = new Animation(0.1f, particleInfo.beginAnimTexList);

			for (int i = 6; i <= 12; i++) {
				TextureRegion temtex = new TextureRegion(mAssetManager.get("particle/boxthree/frame" + i + ".png", Texture.class));
				particleInfo.endAnimTexList.add(temtex);
			}
			particleInfo.endAnimation = new Animation(0.08f, particleInfo.endAnimTexList);
		} else if (particleInfo.animationType == ParticleAnimationType.ANIMATION_TYPE_BOX4) {
			for (int i = 0; i <= 6; i++) {
				TextureRegion temtex = new TextureRegion(mAssetManager.get("particle/boxfour/frame" + i + ".png", Texture.class));
				particleInfo.beginAnimTexList.add(temtex);
			}
			particleInfo.beginAnimation = new Animation(0.1f, particleInfo.beginAnimTexList);

			for (int i = 6; i <= 10; i++) {
				TextureRegion temtex = new TextureRegion(mAssetManager.get("particle/boxfour/frame" + i + ".png", Texture.class));
				particleInfo.endAnimTexList.add(temtex);
			}
			particleInfo.endAnimation = new Animation(0.08f, particleInfo.endAnimTexList);
		}
	}

	private void dataLogicGet() {
		int size = mPutRenderInfos.size();
		for (int i = 0; i < size; i++) {
			PutRenderInfo info = mPutRenderInfos.get(i);

			AddParticle(info.extentPath, info.duration, info.particleType, info.animationType);
			mPutRenderInfos.remove(i);
			i--;
			size = mPutRenderInfos.size();
		}
	}

    public static boolean giftIsExist(String id){
//        String externalPath = "Crazy Together/Gifts" + File.separator + Gifts.GIFT_BASE + id;
        String externalPath = "Crazy Together/Gifts" + File.separator + GiftParticleContants.GIFT_BASE + id;
        boolean bres = true;
        try {
            bres = Gdx.files.external(externalPath).exists();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return bres;
    }

	public static boolean fileIsExist(String abs){
//        String externalPath = "Crazy Together/Gifts" + File.separator + Gifts.GIFT_BASE + id;
		boolean bres = true;
		try {
			bres = Gdx.files.external(abs).exists();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return bres;
	}
}
