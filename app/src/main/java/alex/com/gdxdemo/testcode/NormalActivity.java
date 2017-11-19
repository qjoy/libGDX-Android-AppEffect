package alex.com.gdxdemo.testcode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import alex.com.gdxdemo.R;

/**
 * @author AleXQ
 * @Date 15/12/4
 */

public class NormalActivity extends Activity {

	public static void launch(Context context){
		Intent intent = new Intent();
		intent.setClass(context, NormalActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_activity_normalscreen);
	}
}
