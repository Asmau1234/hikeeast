package com.lityum.hikeeast;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
@SuppressWarnings("unused")
public class FullImage extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_full_image);

		Intent intent = getIntent();

		ImageView imageView = (ImageView) findViewById(R.id.fullimage);

		imageView.setLayoutParams(new ViewGroup.LayoutParams(
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT));

		/* imageView.setImageResource(); */
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.full_image, menu);
		return true;
	}

}
