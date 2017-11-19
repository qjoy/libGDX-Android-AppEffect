package alex.com.gdxdemo.box2d.Tools;

/**
 * @author AleXQ
 * @Date 15/12/26
 * @Description:
 */


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by alex_xq on 15/6/19.
 */
public class Transform {
	/**
	 * @param x_px				图片所在x坐标
	 * @param y_px				图片所在y坐标
	 * @param width_px			图片宽度
	 * @param height_px			图片高度
	 * @param scale				缩放比例
	 * @return					（x,y）直接设置为body的position可使body与图片重合
	 */
	public static Vector2 ptm(float x_px, float y_px, float width_px, float height_px, float scale){
		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();
		Vector2 vector2 = new Vector2();
		vector2.x = -(screenWidth - x_px * 2 - width_px) / scale / 2;
		vector2.y = -(screenHeight - y_px * 2 - height_px) / scale / 2;
		return vector2;
	}

	/**
	 * @param x_m				body所在x坐标
	 * @param y_m				body所在y坐标
	 * @param wh				(x,y)body的宽高
	 * @param scale				缩放比例
	 * @return					（x,y）直接设置为图片的position可使图片与body重合
	 */
	public static Vector2 mtp(float x_m, float y_m, Vector2 wh, float scale){
		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();
		Vector2 vector2 = new Vector2();
		vector2.x = x_m * scale + screenWidth / 2 - wh.x * scale;
		vector2.y = y_m * scale + screenHeight / 2 - wh.y * scale;
		return vector2;
	}
}