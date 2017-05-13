package alex.com.gdxdemo.box2d.Tools;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class ScreenParamUtil {
	
	//Get pixels according to screen density
	public static int GetAdaptivePixels(final Context context, int orgPixels) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		return (int)(orgPixels * dm.density + 0.5f);
	}
	
	//Get width of screen in px
	public static int GetScreenWidthPx(final Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}
	
	//Get height of screen in px
	public static int GetScreenHeightPx(final Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}
	
	public static int GetScreenWidthDp(final Context context){
		return px2dip(context, GetScreenWidthPx(context));
	}
	
	public static int GetScreenHeightDp(final Context context){
		return px2dip(context, GetScreenHeightPx(context));
	}
	
	
	public static int getItemCountPerline(final Context context, int itemWidth) {
		if(null == context) {
			return 1;
		}
		int screenWidth = ScreenParamUtil.GetScreenWidthPx(context);
		int itemWidth1 = itemWidth;
		return screenWidth / itemWidth1;
	}
	
	/**
	 * 根据手机分辨率从dp转成px
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static  int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
	  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
	public static  int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f)-15;  
    } 
	
}
