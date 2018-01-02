package alex.com.gdxdemo.awesome;

import android.util.Log;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by QJoy on 2017.12.25.
 */
@SuppressWarnings("All")
public class AwesomeEffectView implements ApplicationListener {

	public static boolean openDEBUGLog = false;
	private static final String TAG = AwesomeEffectView.class.getSimpleName();

	//基础绘制资源
	SpriteBatch mBatch;

	//	Asset资源加载管理
	AssetManager mAssetManager = new AssetManager();

	int mWidth = 0;

	private boolean forceOver = false;

	private boolean mIsLand = false;

	private boolean m_candraw = true;


	public interface OnStateListener {
		public void OnBegin(boolean isSelf);

		public void OnFinish(boolean isSelf);
	}



	public void forceOver() {

		if (openDEBUGLog)
			Log.d(TAG, "forceOver");

		forceOver = true;

//		缓冲50ms，解决退出时绘制闪动的问题
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void closeforceOver() {

		if (openDEBUGLog)
			Log.d(TAG, "closeforceOver");

		forceOver = false;

		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void create() {

		if (openDEBUGLog)
			Log.d(TAG, "create");

		mBatch = new SpriteBatch();

		mWidth = Gdx.graphics.getWidth() > Gdx.graphics.getHeight() ? Gdx.graphics.getHeight() : Gdx.graphics.getWidth();

		//放置 init 方法
	}

	@Override
	public void resize(int i, int i2) {

	}

	public void setCanDraw(boolean candraw) {

		if (openDEBUGLog)
			Log.d(TAG, "setCanDraw:"+candraw);

		m_candraw = candraw;

		if (!m_candraw) {
//			放置不可见时手动销毁的内容
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
			//			放置不可见时手动销毁的内容
			return;
		}

//		防止动画逻辑

		mBatch.begin();
//		效果绘制
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

		if (openDEBUGLog)
			Log.d(TAG, "dispose");

		mBatch.dispose();

		//遍历释放所有资源

	}

}
