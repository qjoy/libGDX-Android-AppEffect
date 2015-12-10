package alex.com.gdxdemo.testcode;

import android.os.Build;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;

/**
 * Created by alex_xq on 14/12/31.
 */
public class SpringEffect {

    private static final String TAG = "SpringEffect";

    /***
     * down/up时展示spring效果，up成功时出发Runnable操作
     *
     * @param view     动画view对象
     * @param runnable invoke操作
     */
    public static void doEffectSticky(final View view, final Runnable runnable) {

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            if (runnable != null)
                runnable.run();
        } else {

            // Create a system to run the physics loop for a set of springs.
            SpringSystem springSystem = SpringSystem.create();

// Add a spring to the system.
            final Spring spring = springSystem.createSpring();

// Add a listener to observe the motion of the spring.
            spring.addListener(new SimpleSpringListener() {

                @Override
                public void onSpringUpdate(Spring spring) {
                    // You can observe the updates in the spring
                    // state by asking its current value in onSpringUpdate.
                    float value = (float) spring.getCurrentValue();
                    float scale = 1f - (value * 0.2f);
                    view.setScaleX(scale);
                    view.setScaleY(scale);
                }


            });

            view.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    int key = event.getAction();

                    switch (key) {

                        case MotionEvent.ACTION_DOWN:
                            spring.setEndValue(1f);
                            break;

                        case MotionEvent.ACTION_UP:
                            if (runnable != null)
                                runnable.run();
                            spring.setEndValue(0f);
                            view.playSoundEffect(SoundEffectConstants.CLICK);
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            spring.setEndValue(0f);
                            break;

                        case MotionEvent.ACTION_OUTSIDE:
                            spring.setEndValue(0f);
                            break;

                        default:
                            break;
                    }
                    return true;
                }
            });
        }
    }

}
