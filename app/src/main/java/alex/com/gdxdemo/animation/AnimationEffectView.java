package alex.com.gdxdemo.animation;

import android.content.Context;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import javax.microedition.khronos.opengles.GL10;


/**
 * @author AleXQ
 * @Date 15/6/19
 * @Description:
 */
public class AnimationEffectView implements ApplicationListener {

	private static final String TAG = "AnimationEffectView";

	private Context m_context;

	private SpriteBatch m_spriteBatch;

	private boolean m_candraw = true;

	private int m_width = 0;
	private int m_height = 0;

	//	Array<TextureAtlas.AtlasRegion> regions;
	Animation AlienAnimation;
	float statetime = 0;

	public AnimationEffectView(Context context) {
		m_context = context;
	}


	public void release() {

	}

	@Override
	public void create() {


		m_spriteBatch = new SpriteBatch();

		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("animation/face.atlas"));

//		regions = atlas.findRegions("face-change");

		AlienAnimation = new Animation(0.25f, atlas.findRegions("face-change"));
	}

	@Override
	public void resize(int width, int height) {
		m_width = width;
		m_height = height;
	}

	@Override
	public void dispose() {

	}

	public void setCanDraw(boolean candraw) {
		m_candraw = candraw;

		if (!m_candraw) {

		}
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		statetime += Gdx.graphics.getDeltaTime();

		m_spriteBatch.begin();
//			m_spriteBatch.draw(regions.get(3), 0, 0);

		m_spriteBatch.draw((TextureAtlas.AtlasRegion) AlienAnimation.getKeyFrame(statetime, true), 0, 0, m_width, m_height);

		m_spriteBatch.end();

	}


	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}